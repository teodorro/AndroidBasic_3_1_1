package ru.netology.androidbasic_3_1_1.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.androidbasic_3_1_1.dto.Post
import ru.netology.androidbasic_3_1_1.model.FeedModel
import ru.netology.androidbasic_3_1_1.model.StateLiveData
import ru.netology.androidbasic_3_1_1.repository.GetAllCallback
import ru.netology.androidbasic_3_1_1.repository.PostRepository
import ru.netology.androidbasic_3_1_1.repository.PostRepositoryHttpImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    published = "",
    likedByMe = false,
    likes = 0,
    attachment = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryHttpImpl()
    private val _state = StateLiveData()
    val state: LiveData<FeedModel>
        get() = _state
    private val edited = MutableLiveData(empty)
    var draft: String = ""
    private var nextId: Long = 100500;

    init {
        getPostsAsync()
    }

    fun getPostsAsync() {
        _state.value = FeedModel(loading = true)
        repository.getAllAsync(object : GetAllCallback {

            override fun onSuccess(posts: List<Post>) {
                _state.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _state.postValue(FeedModel(errorReload = true))
            }
        })
    }

    fun likeById(id: Long){
        if (state.value == null)
            throw NullPointerException("state.value == null, that's impossible")

        _state.value =
            FeedModel(
                posts = _state.getPosts(),
                loading = true
            )
        val initialPosts = _state.getPosts()
        val post = initialPosts.first { x -> x.id == id }

        if (!post.likedByMe)
            repository.likeById(
                id,
                onSuccess = onLikeOrDislike(initialPosts, id),
                onFailure = {
                    _state.postValue(
                        FeedModel(
                            posts = initialPosts,
                            error = true,
                            errorMessage = it.message ?: ""
                        ))
                }
            )
        else
            repository.dislikeById(
                id,
                onSuccess = onLikeOrDislike(initialPosts, id),
                onFailure = {
                    _state.postValue(
                        FeedModel(
                            posts = initialPosts,
                            error = true,
                            errorMessage = it.message ?: ""
                        ))
                }
            )
    }

    private fun onLikeOrDislike(
        initialPosts: List<Post>,
        id: Long
    ): (Long) -> Unit = {
        val posts = initialPosts.map {
            if (it.id != id)
                it
            else
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                )
        }
        _state.postValue(
            FeedModel(posts, empty = posts.isEmpty())
        )
    }

    fun shareById(id: Long) = repository.shareById(id)

    fun edit(post: Post) {
        edited.value = post
    }

    fun removeById(id: Long) {
        _state.value =
            FeedModel(
                posts = _state.getPosts(),
                loading = true
            )
        val initialPosts = _state.getPosts()
        repository.removeById(
            id,
            onSuccess = {
                var posts = _state.getPosts()
                if (posts.any { x -> x.id == id }) {
                    posts = posts.minus(posts.first { x -> x.id == id })
                }
                _state.postValue(FeedModel(posts))
            },
            onFailure = {
                _state.postValue(
                    FeedModel(
                        posts = initialPosts,
                        error = true,
                        errorMessage = it.message ?: ""
                    ))
            }
        )
    }

    fun getById(id: Long) {
        _state.value =
            FeedModel(
                posts = _state.getPosts(),
                loading = true
            )
        val initialPosts = _state.getPosts()
        repository.getById(
            id,
            onSuccess = {
                _state.postValue(FeedModel(initialPosts))
            },
            onFailure = {
                _state.postValue(
                    FeedModel(
                        posts = initialPosts,
                        error = true,
                        errorMessage = it.message ?: ""
                    ))
            }
        )
    }

    fun save() {
        _state.value =
            FeedModel(
                posts = _state.getPosts(),
                loading = true
            )
        val initialPosts = _state.getPosts()
        edited.value?.let { it ->
            repository.save(
                it,
                onSuccess = {
                    var posts = _state.getPosts()
                    var post = it
                    if (post.id == 0L) {
                        posts = listOf(
                            post.copy(
                                id = nextId++,
                                author = "me",
                                likedByMe = false,
                                published = "now",
                                likes = 0,
                            )
                        ) + posts
                    }
                    else {
                        posts = posts.map {
                            if (it.id != post.id) it else post.copy(content = post.content)
                        }
                    }
                    _state.postValue(FeedModel(posts))
                },
                onFailure = {
                    _state.postValue(
                        FeedModel(
                            posts = initialPosts,
                            error = true,
                            errorMessage = it.message ?: ""
                        ))
                }
            )
        }
        edited.value = empty
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }
}
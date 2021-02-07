package ru.netology.androidbasic_3_1_1.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.androidbasic_3_1_1.dto.Post
import ru.netology.androidbasic_3_1_1.model.FeedModel
import ru.netology.androidbasic_3_1_1.repository.GetAllCallback
import ru.netology.androidbasic_3_1_1.repository.PostRepository
import ru.netology.androidbasic_3_1_1.repository.PostRepositoryHttpImpl
import ru.netology.androidbasic_3_1_1.repository.PostRepositorySqliteImpl
import java.io.IOException
import kotlin.concurrent.thread

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
    private val _state = MutableLiveData(FeedModel())
    val state: LiveData<FeedModel>
        get() = _state
    private val edited = MutableLiveData(empty)
    var draft: String = ""
    private var nextId: Long = 100500;


    init {
        getPostsAsync()
    }

//    fun getPosts() {
//        thread {
//            _state.postValue(FeedModel(loading = true))
//            try {
//                val posts = (repository as PostRepositoryHttpImpl).getAll()
//                _state.postValue(
//                    FeedModel(
//                        posts.value as List<Post>,
//                        empty = (posts.value as List<Post>).isEmpty()
//                    )
//                )
//            } catch (e: IOException) {
//                _state.postValue(FeedModel(error = true))
//            }
//        }
//    }

    fun getPostsAsync() {
        _state.value = FeedModel(loading = true)
        repository.getAllAsync(object : GetAllCallback {

            override fun onSuccess(posts: List<Post>) {
                _state.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _state.postValue(FeedModel(error = true))
            }
        })
    }

    fun likeById(id: Long) {
        if (state.value == null)
            throw NullPointerException("state.value == null, that's impossible")
        _state.value =
            FeedModel(
                posts = _state.value!!.posts,
                loading = true
            )
        val initialPosts = state.value!!.posts
        val post = initialPosts.first { x -> x.id == id }

        if (!post.likedByMe)
            (repository as PostRepositoryHttpImpl).likeById(
                id,
                onSuccess = onLikeOrDislike(initialPosts, id),
                onFailure = {
                    _state.postValue(FeedModel(error = true))
                }
            )
        else
            (repository as PostRepositoryHttpImpl).dislikeById(
                id,
                onSuccess = onLikeOrDislike(initialPosts, id),
                onFailure = {
                    _state.postValue(FeedModel(error = true))
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

//    fun removeById(id: Long) = repository.removeById(id)

    fun edit(post: Post) {
        edited.value = post
    }

    fun edit(id: Long, content: String) {
        repository.editPostContentById(id, content)
    }

    fun removeById(id: Long) {
        (repository as PostRepositoryHttpImpl).removeById(
            id,
            onSuccess = {
                _state.value =
                    FeedModel(
                        posts = _state.value!!.posts,
                        loading = true
                    )
                var posts = state.value!!.posts
                if (posts.any { x -> x.id == id }) {
                    posts = posts.minus(posts.first { x -> x.id == id })
                }
                _state.postValue(FeedModel(posts))
            },
            onFailure = {
                _state.postValue(FeedModel(error = true))
            }
        )
    }

    fun save() {
        edited.value?.let { it ->
            (repository as PostRepositoryHttpImpl).save(
                it,
                onSuccess = {
                    _state.value =
                        FeedModel(
                            posts = _state.value!!.posts,
                            loading = true
                        )
                    var posts = state.value!!.posts
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

//                    if (it.id.equals(0)){
//                        state.value!!.posts += it
//                    }
//                    else{
//                        val initialPosts = state.value!!.posts
//                        val post = initialPosts.first { x -> x.id == it.id }
//                        post.content = it.content
//                    }
                },
                onFailure = {
                    _state.postValue(FeedModel(error = true))
                }
            )
        }
        edited.value = empty
    }

//    fun save() {
//        edited.value?.let {
//            repository.save(it)
//        }
//        edited.value = empty
//    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }

    fun createSample() {
        val d = (repository as PostRepositorySqliteImpl).getSamplePosts()
        for (post in d) {
            edited.value = post
            save()
        }
    }
}
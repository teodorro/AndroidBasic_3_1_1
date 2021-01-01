package ru.netology.androidbasic_3_1_1.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.androidbasic_3_1_1.dto.Post
import ru.netology.androidbasic_3_1_1.model.FeedModel
import ru.netology.androidbasic_3_1_1.repository.PostRepository
import ru.netology.androidbasic_3_1_1.repository.PostRepositoryHttpImpl
import ru.netology.androidbasic_3_1_1.repository.PostRepositorySqliteImpl
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    published = "",
    likedByMe = false,
    likes = 0,
    views = 0,
    shares = 0,
    video = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryHttpImpl()
    private val _state = MutableLiveData(FeedModel())
    val state: LiveData<FeedModel>
        get() = _state
    private val edited = MutableLiveData(empty)
    var draft: String = ""


    init {
        getPosts()
    }

    fun getPosts() {
        thread {
            _state.postValue(FeedModel(loading = true))
            try {
                val posts = (repository as PostRepositoryHttpImpl).getAll()
                _state.postValue(
                    FeedModel(
                        posts.value as List<Post>,
                        empty = (posts.value as List<Post>).isEmpty()
                    )
                )
            } catch (e: IOException) {
                _state.postValue(FeedModel(error = true))
            }
        }
    }

    fun likeById(id: Long) {
        thread {
            if (state.value == null)
                throw NullPointerException("state.value == null, that's impossible")
            _state.postValue(
                FeedModel(
                    posts = _state.value!!.posts,
                    loading = true
                )
            )
            try {
                val initialPosts = state.value!!.posts
                val post = initialPosts.first { x -> x.id == id }

                if (!post.likedByMe)
                    repository.likeById(id)
                else
                    (repository as PostRepositoryHttpImpl).dislikeById(id)

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
                    FeedModel(
                        posts,
                        empty = posts.isEmpty()
                    )
                )

            } catch (e: Exception) {
                _state.postValue(FeedModel(error = true))
            }
        }
    }

    fun shareById(id: Long) = repository.shareById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun edit(post: Post) {
        edited.value = post
    }

    fun edit(id: Long, content: String) {
        repository.editPostContentById(id, content)
    }

    fun save() {
        edited.value?.let {
            repository.save(it)
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

    fun createSample() {
        val d = (repository as PostRepositorySqliteImpl).getSamplePosts()
        for (post in d) {
            edited.value = post
            save()
        }
    }
}
package ru.netology.androidbasic_3_1_1.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.androidbasic_3_1_1.dto.Post
import ru.netology.androidbasic_3_1_1.db.AppDb
import ru.netology.androidbasic_3_1_1.repository.PostRepository
import ru.netology.androidbasic_3_1_1.repository.PostRepositorySqliteImpl

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
    private val repository: PostRepository = PostRepositorySqliteImpl(
        AppDb.getInstance(application).postDao
    )
    var data = repository.getAll()

    private val edited = MutableLiveData(empty)
    var draft: String = ""

//    init {
////        if (data.value == null || !data.value?.any()!!) {
////            val d = (repository as PostRepositorySqliteImpl).getSamplePosts()
////            for (post in d){
////                edited.value = post
////                save()
////            }
////        }
//    }

    fun likeById(id: Long) = repository.likeById(id)

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
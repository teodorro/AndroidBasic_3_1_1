package ru.netology.androidbasic_3_1_1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.NullPointerException

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    published = "",
    likedByMe = false,
    likes = 0,
    views = 0,
    shares = 0,
    video = null
)

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)

    fun shareById(id: Long) = repository.shareById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun edit(post: Post) {
        edited.value = post
    }

    fun edit(id: Long, content: String) {
        repository.editPostContentById(id, content)
    }

    fun save(){
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun changeContent(content: String){
        edited.value?.let {
            val text = content.trim()
            if (it.content == text){
                return
            }
            edited.value = it.copy(content = text)
        }
    }
}
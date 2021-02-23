package ru.netology.androidbasic_3_1_1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.androidbasic_3_1_1.dto.Post

interface PostRepository {
    fun getAllAsync(callback: GetAllCallback)
    fun likeById(id: Long, onSuccess: (Long) -> Unit, onFailure: (Throwable) -> Unit)
    fun dislikeById(id: Long, onSuccess: (Long) -> Unit, onFailure: (Throwable) -> Unit)
    fun getById(id: Long, onSuccess: (Long) -> Unit, onFailure: (Throwable) -> Unit)
    fun shareById(id: Long)
    fun removeById(id: Long, onSuccess: (Long) -> Unit, onFailure: (Throwable) -> Unit)
    fun save(post: Post, onSuccess: (Post) -> Unit, onFailure: (Throwable) -> Unit)
    fun editPostContentById(id: Long, postContent: String)
}
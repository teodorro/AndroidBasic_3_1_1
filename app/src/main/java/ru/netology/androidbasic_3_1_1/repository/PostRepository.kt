package ru.netology.androidbasic_3_1_1.repository

import androidx.lifecycle.LiveData
import ru.netology.androidbasic_3_1_1.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun editPostContentById(id: Long, postContent: String)
}
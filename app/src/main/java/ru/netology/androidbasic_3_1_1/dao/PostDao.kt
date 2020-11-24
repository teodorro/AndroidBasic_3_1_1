package ru.netology.androidbasic_3_1_1.dao

import ru.netology.androidbasic_3_1_1.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun shareById(id: Long)
    fun editPostContentById(id: Long, postContent: String)
}
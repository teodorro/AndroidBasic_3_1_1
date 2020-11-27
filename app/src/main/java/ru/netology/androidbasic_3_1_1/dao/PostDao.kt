package ru.netology.androidbasic_3_1_1.dao

import ru.netology.androidbasic_3_1_1.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun removeById(id: Long)
    fun updateLikes(id: Long, likedByMe: Boolean, likes: Int)
    fun updateShares(id: Long, shares: Int)
    fun updatePostContent(id: Long, postContent: String)
}
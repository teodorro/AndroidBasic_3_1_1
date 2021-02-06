package ru.netology.androidbasic_3_1_1.dto

data class Post (
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int,
//    val shares: Int,
//    val views: Int,
//    val video: String?,
    val attachment: Attachment?,
)
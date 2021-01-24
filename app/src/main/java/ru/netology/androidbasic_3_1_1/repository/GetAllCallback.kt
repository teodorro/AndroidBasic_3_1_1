package ru.netology.androidbasic_3_1_1.repository

import ru.netology.androidbasic_3_1_1.dto.Post

interface GetAllCallback {
    fun onSuccess(posts: List<Post>) {}
    fun onError(e: Exception) {}
}
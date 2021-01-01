package ru.netology.androidbasic_3_1_1.model

import ru.netology.androidbasic_3_1_1.dto.Post

data class FeedModel (
    var posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,
)
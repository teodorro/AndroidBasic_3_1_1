package ru.netology.androidbasic_3_1_1

import ru.netology.androidbasic_3_1_1.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onPlay(post: Post) {}
    fun onSelectPost(post: Post) {}
}
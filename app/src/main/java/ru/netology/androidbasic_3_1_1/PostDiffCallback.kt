package ru.netology.androidbasic_3_1_1

import androidx.recyclerview.widget.DiffUtil
import ru.netology.androidbasic_3_1_1.dto.Post

class PostDiffCallback : DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
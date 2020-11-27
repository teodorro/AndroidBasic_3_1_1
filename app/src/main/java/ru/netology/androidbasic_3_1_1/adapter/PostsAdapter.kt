package ru.netology.androidbasic_3_1_1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.androidbasic_3_1_1.OnInteractionListener
import ru.netology.androidbasic_3_1_1.dto.Post
import ru.netology.androidbasic_3_1_1.PostDiffCallback
import ru.netology.androidbasic_3_1_1.viewHolder.PostViewHolder
import ru.netology.androidbasic_3_1_1.databinding.CardPostBinding


class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
)
    : ListAdapter<Post, PostViewHolder>(PostDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}
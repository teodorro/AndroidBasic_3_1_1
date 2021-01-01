package ru.netology.androidbasic_3_1_1.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.androidbasic_3_1_1.dto.Post


@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val video: String?
) {
    companion object {
        fun fromPost(post: Post): PostEntity =
            with(post) {
                PostEntity(
                    id,
                    author,
                    content,
                    published,
                    likedByMe,
                    likes,
                    shares,
                    views,
                    video
                )
            }
    }
}

fun PostEntity.toPost(): Post =
    Post(id, author, content, published, likedByMe, likes, shares, views, video)
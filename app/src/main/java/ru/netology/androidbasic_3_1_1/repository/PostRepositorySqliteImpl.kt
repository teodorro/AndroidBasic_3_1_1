package ru.netology.androidbasic_3_1_1.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.androidbasic_3_1_1.Post
import ru.netology.androidbasic_3_1_1.dao.PostDao

class PostRepositorySqliteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init{
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L){
            listOf(saved) + posts
        } else{
            posts.map { if (it.id == id) saved else it}
        }
        data.value = posts
    }

    override fun editPostContentById(id: Long, postContent: String) {
        TODO("Not yet implemented")
    }
}
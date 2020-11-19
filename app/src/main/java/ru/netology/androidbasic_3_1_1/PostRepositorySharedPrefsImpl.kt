package ru.netology.androidbasic_3_1_1

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostRepositorySharedPrefsImpl(
    private val context: Context
) : PostRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private var nextId = 1L
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let{
            posts = gson.fromJson(it, type)
            data.value = posts
        }
    }

    override fun getAll(): LiveData<List<Post>> {
        return data
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id)
                it
            else
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                )
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map{
            if (it.id != id)
                it
            else
                it.copy(
                    shares = it.shares + 1
                )
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "me",
                    likedByMe = false,
                    published = "now",
                    likes = 0,
                    shares = 0,
                    views = 0,
                    video = null
                )
            ) + posts
            data.value = posts
        }
        else {
            posts = posts.map {
                if (it.id != post.id) it else post.copy(content = post.content)
            }
            data.value = posts
        }
        sync()
    }

    override fun editPostContentById(id: Long, postContent: String) {
        posts = posts.map{
            if (it.id != id)
                it
            else
                it.copy(
                    content = postContent
                )
        }
        data.value = posts
        sync()
    }

    private fun sync(){
        with(prefs.edit()){
            putString(key, gson.toJson(posts))
            apply()
        }
    }
}
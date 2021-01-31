package ru.netology.androidbasic_3_1_1.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.androidbasic_3_1_1.dto.Post

class PostRepositoryFileImpl(
    private val context: Context
) : PostRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 1L
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use{
                posts = gson.fromJson(it, type)
                data.value = posts
            }
        } else{
            posts = getSamplePosts();
            context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use{
                it.write(gson.toJson(posts))
            }
            data.value = posts
        }
        nextId = posts.map { x -> x.id }.maxOrNull()!! + 1
    }

    override fun getAll(): LiveData<List<Post>> {
        return data
    }

    override fun getAllAsync(callback: GetAllCallback) {
        TODO("Not yet implemented")
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
                    video = ""
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
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use{
            it.write(gson.toJson(posts))
        }
    }

    private fun getSamplePosts(): List<Post>{
        return listOf<Post>(
            Post(
                id = 3,
                author = "Нетология. Университет интернет профессий",
                authorAvatar = "",
                content = ">.< Знаний не хватит.",
                published = "19 сентября в 10:12",
                likedByMe = false,
                likes = 9999,
                shares = 999,
                views = 9999999,
                video = "http://www.youtube.com/watch?v=TfXZ1n6HUeI",
                attachment = null
            ),
            Post(
                id = 2,
                author = "Нетология. Университет интернет профессий",
                authorAvatar = "",
                content = "Знаний хватит на всех. На следующей неделе разберемся.",
                published = "18 сентября в 10:12",
                likedByMe = false,
                likes = 9999,
                shares = 999,
                views = 9999999,
                video = "http://yandex.ru",
                attachment = null
            ),
            Post(
                id = 1,
                author = "Нетология. Университет интернет профессий",
                authorAvatar = "",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен -> http://netolo.gy/fyb Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен -> http://netolo.gy/fyb Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен -> http://netolo.gy/fyb Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен -> http://netolo.gy/fyb" ,
                published = "21 мая в 18:36",
                likedByMe = false,
                likes = 5,
                shares = 999,
                views = 1500000,
                video = "",
                attachment = null
            )
        )
    }
}
package ru.netology.androidbasic_3_1_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId: Long = 4;

    private var posts = listOf<Post>(
        Post(
            id = 3,
            author = "Нетология. Университет интернет профессий",
            content = ">.< Знаний не хватит.",
            published = "19 сентября в 10:12",
            likedByMe = false,
            likes = 9999,
            shares = 999,
            views = 9999999
        ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет профессий",
            content = "Знаний хватит на всех. На следующей неделе разберемся.",
            published = "18 сентября в 10:12",
            likedByMe = false,
            likes = 9999,
            shares = 999,
            views = 9999999
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет профессий",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен -> http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 5,
            shares = 999,
            views = 1500000
        )
    )

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

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
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
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
                    views = 0
                )
            ) + posts
            data.value = posts
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else post.copy(content = post.content)
        }
        data.value = posts
    }
}
package ru.netology.androidbasic_3_1_1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.androidbasic_3_1_1.dto.Post
import ru.netology.androidbasic_3_1_1.dao.PostDao
import ru.netology.androidbasic_3_1_1.entity.PostEntity
import ru.netology.androidbasic_3_1_1.entity.toPost

class PostRepositorySqliteImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = dao.getAll().map {
        it.map { x -> x.toPost() }
    }

    override fun getAllAsync(callback: GetAllCallback) {
        TODO("Not yet implemented")
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromPost(post))
    }

    override fun editPostContentById(id: Long, postContent: String) {
        dao.updatePostContent(id, postContent)
    }

    public fun getSamplePosts(): List<Post>{
        return listOf<Post>(
            Post(
                id = 3,
                author = "Нетология. Университет интернет профессий",
                authorAvatar = "",
                content = ">.< Знаний не хватит.",
                published = "19 сентября в 10:12",
                likedByMe = false,
                likes = 9999,
//                shares = 999,
//                views = 9999999,
//                video = "http://www.youtube.com/watch?v=TfXZ1n6HUeI",
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
//                shares = 999,
//                views = 9999999,
//                video = "http://yandex.ru",
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
//                shares = 999,
//                views = 1500000,
//                video = "",
                attachment = null
            )
        )
    }
}
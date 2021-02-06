package ru.netology.androidbasic_3_1_1.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.create
import ru.netology.androidbasic_3_1_1.BuildConfig
import ru.netology.androidbasic_3_1_1.dto.Post

private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"
//private val logging = HttpLogginInterception().apply{
//    if (BuildConfig.DEBUG){
//        level = HttpLogginInterception.Level.Body
//    }
//}
private val okhttp = OkHttpClient.Builder()
//    .addInterceptor(logging)
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface PostsApi {
    @GET("posts")
    fun getAll(): Call<List<Post>>
}

//object PostApiService{
//    val retrofitService : PostsApi by lazy {
//        retrofit.create(PostsApi::class.java)
//    }
//}

object PostApiService{
    val api : PostsApi by lazy(retrofit::create)
}
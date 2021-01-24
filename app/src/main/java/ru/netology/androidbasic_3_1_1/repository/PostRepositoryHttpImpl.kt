package ru.netology.androidbasic_3_1_1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.androidbasic_3_1_1.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryHttpImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>(){}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: GetAllCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    response.body?.use {
                        try {
                            callback.onSuccess(gson.fromJson(it.string(), typeToken.type))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun getAll(): LiveData<List<Post>> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return MutableLiveData(client.newCall(request)
            .execute()
            .use { it.body?.string() }
            .let{
                gson.fromJson(it, typeToken.type)
            })
    }

    override fun likeById(id: Long) {
        val body = "{\"id\": ${id}}"

        var requestBody = body.toRequestBody()

        val request: Request = Request.Builder()
            .method("POST", requestBody )
            .url("${BASE_URL}/api/slow/posts/${id}/likes")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("---------- Error like request")
            }

            override fun onResponse(call: Call, response: Response) {
                println("---------- Like request sent")
            }
        })
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun save(post: Post) {
        TODO("Not yet implemented")
    }

    override fun editPostContentById(id: Long, postContent: String) {
        TODO("Not yet implemented")
    }

    fun dislikeById(id: Long) {
        val body = "{\"id\": ${id}}"

        var requestBody = body.toRequestBody()

        val request: Request = Request.Builder()
            .method("DELETE", requestBody )
            .url("${BASE_URL}/api/slow/posts/${id}/likes")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("---------- Error dislike request")
            }

            override fun onResponse(call: Call, response: Response) {
                println("---------- Dislike request sent")
            }
        })
    }
}
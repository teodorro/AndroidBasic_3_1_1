package ru.netology.androidbasic_3_1_1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.androidbasic_3_1_1.api.PostApiService
import ru.netology.androidbasic_3_1_1.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryHttpImpl : PostRepository {
    override fun getAllAsync(callback: GetAllCallback) {
        PostApiService.api.getAll()
            .enqueue(object : retrofit2.Callback<List<Post>>{
                override fun onResponse(
                    call: retrofit2.Call<List<Post>>,
                    response: retrofit2.Response<List<Post>>
                ) {
                    if (response.isSuccessful){
                        callback.onSuccess(response.body().orEmpty())
                    } else{
                        callback.onError(RuntimeException(response.message()))
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t.message))
                }
            })
    }

    override fun likeById(id: Long, onSuccess: (Long) -> Unit, onFailure: (Throwable) -> Unit){
        PostApiService.api.likeById(id)
            .enqueue(object : retrofit2.Callback<Post>{
                override fun onResponse(
                    call: retrofit2.Call<Post>,
                    response: retrofit2.Response<Post>
                ) {
                    if (response.isSuccessful){
                        response.body()?.id?.let { onSuccess(it) }
                    } else{
                        onFailure(RuntimeException(response.message()))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                    onFailure(RuntimeException(t.message))
                }
            })
    }

    override fun save(post: Post, onSuccess: (Post) -> Unit, onFailure: (Throwable) -> Unit){
        PostApiService.api.save(post)
            .enqueue(object : retrofit2.Callback<Post>{
                override fun onResponse(
                    call: retrofit2.Call<Post>,
                    response: retrofit2.Response<Post>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let { onSuccess(it) }
                    } else{
                        onFailure(RuntimeException(response.message()))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                    onFailure(RuntimeException(t.message))
                }
            })
    }

    override fun removeById(id: Long, onSuccess: (Long) -> Unit, onFailure: (Throwable) -> Unit){
        PostApiService.api.removeById(id)
            .enqueue(object : retrofit2.Callback<Unit>{
                override fun onResponse(
                    call: retrofit2.Call<Unit>,
                    response: retrofit2.Response<Unit>
                ) {
                    if (response.isSuccessful){
                        onSuccess(id)
                    } else{
                        onFailure(RuntimeException(response.message()))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                    onFailure(RuntimeException(t.message))
                }
            })
    }

    override fun dislikeById(id: Long, onSuccess: (Long) -> Unit, onFailure: (Throwable) -> Unit){
        PostApiService.api.unlikeById(id)
            .enqueue(object : retrofit2.Callback<Post>{
                override fun onResponse(
                    call: retrofit2.Call<Post>,
                    response: retrofit2.Response<Post>
                ) {
                    if (response.isSuccessful){
                        response.body()?.id?.let { onSuccess(it) }
                    } else{
                        onFailure(RuntimeException(response.message()))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                    onFailure(RuntimeException(t.message))
                }
            })
    }

    override fun getById(id: Long, onSuccess: (Long) -> Unit, onFailure: (Throwable) -> Unit) {
        PostApiService.api.getById(id)
            .enqueue(object : retrofit2.Callback<Post>{
                override fun onResponse(
                    call: retrofit2.Call<Post>,
                    response: retrofit2.Response<Post>
                ) {
                    if (response.isSuccessful){
                        response.body()?.id?.let { onSuccess(it) }
                    } else{
                        onFailure(RuntimeException(response.message()))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                    onFailure(RuntimeException(t.message))
                }
            })
    }



    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }


    override fun editPostContentById(id: Long, postContent: String) {
        TODO("Not yet implemented")
    }

}
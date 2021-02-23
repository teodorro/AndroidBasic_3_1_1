package ru.netology.androidbasic_3_1_1.model

import androidx.lifecycle.MutableLiveData
import ru.netology.androidbasic_3_1_1.dto.Post

class StateLiveData : MutableLiveData<FeedModel>() {
    //    var model: FeedModel = FeedModel();
    fun getPosts(): List<Post> {
        return if (value != null) {
            value!!.posts
        } else
            emptyList()
    }
}
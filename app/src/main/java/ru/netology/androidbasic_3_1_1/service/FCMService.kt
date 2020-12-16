package ru.netology.androidbasic_3_1_1.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.androidbasic_3_1_1.R
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val gson = Gson()
    private val channelId = "myChannelId";


    override fun onMessageReceived(message: RemoteMessage) {
        println(Gson().toJson(message))
        message.data[action]?.let {
            var act: Action
            try {
                act = Action.valueOf(it)
            } catch (ex: IllegalArgumentException) {
                handleUnknown(getString(R.string.unknown_notification))
                return@let
            }
            when (act){
                Action.LIKE -> handleLike(gson.fromJson(message.data[content], Like::class.java))
                Action.NEW_POST -> handleNewPost(gson.fromJson(message.data[content], NewPost::class.java))
            }
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    //@SuppressLint("StringFormatMatches")
    private fun handleLike(content: Like){
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_liked_24)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100000), notification)
    }

    private fun handleUnknown(content: String){
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_error_24)
            .setContentTitle(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100000), notification)
    }

    //@SuppressLint("StringFormatMatches")
    private fun handleNewPost(content: NewPost){
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_new_releases_24)
            .setContentTitle(
                getString(
                    R.string.notification_new_post,
                    content.userName
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(content.content))
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100000), notification)
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.channel_remote_name)
            val desciptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = desciptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}


enum class Action {
    LIKE,
    NEW_POST
}


data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String
)

data class NewPost(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int,
    val shares: Int,
    val views: Int
)

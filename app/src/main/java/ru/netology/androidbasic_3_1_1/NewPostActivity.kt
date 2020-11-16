package ru.netology.androidbasic_3_1_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
    }
}
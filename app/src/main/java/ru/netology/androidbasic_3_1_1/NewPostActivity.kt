package ru.netology.androidbasic_3_1_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.androidbasic_3_1_1.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.materialButtonText.setOnClickListener {
            val res = Intent().putExtra(Intent.EXTRA_TEXT, binding.textInputEditText.text.toString())
            setResult(RESULT_OK, res)
            finish()
        }
    }
}
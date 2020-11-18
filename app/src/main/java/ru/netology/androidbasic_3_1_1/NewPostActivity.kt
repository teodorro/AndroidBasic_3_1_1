package ru.netology.androidbasic_3_1_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.netology.androidbasic_3_1_1.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.materialButtonText.setOnClickListener {
            if (binding.textInputEditText.text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.message_not_empty),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val res = Intent().putExtra(Intent.EXTRA_TEXT, binding.textInputEditText.text.toString())
            setResult(RESULT_OK, res)
            finish()
        }
    }
}
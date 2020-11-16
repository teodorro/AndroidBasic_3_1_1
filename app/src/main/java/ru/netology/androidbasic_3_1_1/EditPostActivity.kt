package ru.netology.androidbasic_3_1_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.androidbasic_3_1_1.databinding.ActivityEditPostBinding
import ru.netology.androidbasic_3_1_1.databinding.ActivityNewPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.textInputEditText.setText(this.intent.getStringExtra(Intent.EXTRA_TEXT))

        binding.materialButtonSave.setOnClickListener {
            val res = Intent()
                .putExtra(Intent.EXTRA_TEXT, binding.textInputEditText.text.toString())
            setResult(RESULT_OK, res)
            finish()
        }

        binding.materialButtonCancel.setOnClickListener {
            val res = Intent()
            setResult(RESULT_CANCELED, res)
            finish()
        }
    }
}
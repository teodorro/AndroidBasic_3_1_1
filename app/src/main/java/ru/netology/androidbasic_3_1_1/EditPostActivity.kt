package ru.netology.androidbasic_3_1_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import ru.netology.androidbasic_3_1_1.databinding.ActivityEditPostBinding
import ru.netology.androidbasic_3_1_1.databinding.ActivityNewPostBinding

class EditPostActivity : AppCompatActivity() {
    private var postId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.textInputEditText.setText(this.intent.getStringExtra(Intent.EXTRA_TEXT))
        postId = this.intent.getLongExtra(Intent.EXTRA_UID, -1)

        binding.materialButtonSave.setOnClickListener {
            if (binding.textInputEditText.text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.message_not_empty),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val res = Intent()
                    .putExtra(Intent.EXTRA_TEXT, binding.textInputEditText.text.toString())
                    .putExtra(Intent.EXTRA_UID, postId)
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
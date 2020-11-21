package ru.netology.androidbasic_3_1_1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.androidbasic_3_1_1.R
import ru.netology.androidbasic_3_1_1.databinding.FragmentEditPostBinding
import ru.netology.androidbasic_3_1_1.viewModel.PostViewModel

class EditPostFragment : Fragment() {
    private var postId: Long = -1
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)

        binding.textInputEditText.requestFocus()
//        binding.textInputEditText.setText(this.intent.getStringExtra(Intent.EXTRA_TEXT))
//        postId = this.intent.getLongExtra(Intent.EXTRA_UID, -1)
//
//        binding.materialButtonSave.setOnClickListener {
//            if (binding.textInputEditText.text.isNullOrEmpty()) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.message_not_empty),
//                    Toast.LENGTH_LONG
//                ).show()
//                return@setOnClickListener
//            }
//            val res = Intent()
//                .putExtra(Intent.EXTRA_TEXT, binding.textInputEditText.text.toString())
//                .putExtra(Intent.EXTRA_UID, postId)
//            setResult(RESULT_OK, res)
//            finish()
//        }
//
//        binding.materialButtonCancel.setOnClickListener {
//            val res = Intent()
//            setResult(RESULT_CANCELED, res)
//            finish()
//        }
        return binding.root
    }

}
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
import androidx.navigation.fragment.findNavController
import ru.netology.androidbasic_3_1_1.AndroidUtils
import ru.netology.androidbasic_3_1_1.R
import ru.netology.androidbasic_3_1_1.activity.FeedFragment.Companion.textArg
import ru.netology.androidbasic_3_1_1.databinding.FragmentNewPostBinding
import ru.netology.androidbasic_3_1_1.viewModel.PostViewModel

class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNewPostBinding.inflate(layoutInflater, container, false)
        binding.textInputEditText.requestFocus()
        binding.materialButtonText.setOnClickListener {
            val content = binding.textInputEditText.text.toString()
            if (content.isNullOrBlank()){
                Toast.makeText(
                    this.context,
                    getString(R.string.message_not_empty),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.changeContent(content)
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}
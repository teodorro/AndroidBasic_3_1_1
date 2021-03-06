package ru.netology.androidbasic_3_1_1.activity

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
import ru.netology.androidbasic_3_1_1.activity.FeedFragment.Companion.longArg
import ru.netology.androidbasic_3_1_1.activity.FeedFragment.Companion.textArg
import ru.netology.androidbasic_3_1_1.databinding.FragmentEditPostBinding
import ru.netology.androidbasic_3_1_1.viewModel.PostViewModel
import kotlin.NullPointerException

class EditPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)

        val id = arguments?.longArg
        val post = viewModel.state.value?.posts?.first { x -> x.id == id }
            ?: throw NullPointerException("null post")
        arguments?.textArg?.let(binding.textInputEditText::setText)

        binding.materialButtonSave.setOnClickListener {
            val content = binding.textInputEditText.text.toString()
            if (content.isNullOrBlank()){
                Toast.makeText(
                    this.context,
                    getString(R.string.message_not_empty),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.edit(post)
            viewModel.changeContent(binding.textInputEditText.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        binding.materialButtonCancel.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        binding.textInputEditText.requestFocus()

        return binding.root
    }

}
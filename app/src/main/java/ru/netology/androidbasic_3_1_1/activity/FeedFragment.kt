package ru.netology.androidbasic_3_1_1.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import ru.netology.androidbasic_3_1_1.OnInteractionListener
import ru.netology.androidbasic_3_1_1.Post
import ru.netology.androidbasic_3_1_1.R
import ru.netology.androidbasic_3_1_1.StringArgs
import ru.netology.androidbasic_3_1_1.adapter.PostsAdapter
import ru.netology.androidbasic_3_1_1.databinding.FragmentFeedBinding
import ru.netology.androidbasic_3_1_1.viewModel.PostViewModel


class FeedFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArgs
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {

                editPost(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, post.content)
                    .setType("text/plain")
                    .also {
                        if (context?.packageManager?.let { it1 -> it.resolveActivity(it1) } == null) {
                            Toast.makeText(
                                this@FeedFragment.context,
                                getString(R.string.app_not_found),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Intent.createChooser(it, getString(R.string.chooser_share_post))
                                .also(::startActivity);
                        }
                    }

                viewModel.shareById(post.id)
            }

            override fun onPlay(post: Post) {
                if (post.video.isNullOrBlank())
                    return
                val uri = Uri.parse(post.video)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        })

        binding.recylerViewPosts.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }
            binding.groupRevert.visibility = View.VISIBLE
            with(binding.textViewEditContent) {
                text = post.content
            }
        }

        binding.floatingButtonAddPost.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }

    private fun editPost(post: Post) {
        val text = post.content
        findNavController().navigate(R.id.action_feedFragment_to_editPostFragment,
            Bundle().apply { textArg = text })
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == NEW_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            val text = data.getStringExtra(Intent.EXTRA_TEXT)
//            viewModel.changeContent(text.toString())
//            viewModel.save()
//            AndroidUtils.hideKeyboard(binding.root)
//        }
//        if (requestCode == EDIT_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            val text = data.getStringExtra(Intent.EXTRA_TEXT)
//            val id = data.getLongExtra(Intent.EXTRA_UID, -1)
//            viewModel.edit(id, text.toString())
//            AndroidUtils.hideKeyboard(binding.root)
//        }
//    }


}
package ru.netology.androidbasic_3_1_1.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import ru.netology.androidbasic_3_1_1.OnInteractionListener
import ru.netology.androidbasic_3_1_1.Post
import ru.netology.androidbasic_3_1_1.R
import ru.netology.androidbasic_3_1_1.activity.FeedFragment.Companion.longArg
import ru.netology.androidbasic_3_1_1.activity.FeedFragment.Companion.textArg
import ru.netology.androidbasic_3_1_1.adapter.PostsAdapter
import ru.netology.androidbasic_3_1_1.databinding.FragmentSelectPostBinding
import ru.netology.androidbasic_3_1_1.viewModel.PostViewModel

class SelectPostFragment : Fragment() {


    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectPostBinding.inflate(layoutInflater, container, false)

        val id = arguments?.longArg
        val post = viewModel.data.value?.first { x -> x.id == id }

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) { editPost(post) }
            override fun onLike(post: Post) { viewModel.likeById(post.id) }
            override fun onRemove(post: Post) { removePost(post) }
            override fun onShare(post: Post) { sharePost(post) }
            override fun onPlay(post: Post) { playVideo(post) }
            override fun onSelectPost(post: Post) {  }
        })

        binding.recyclerSelectedPost.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { adapter.submitList(mutableListOf(post)) }

        return binding.root
    }

    private fun removePost(post: Post) {
        viewModel.removeById(post.id)
        findNavController().navigateUp()
    }

    private fun editPost(post: Post) {
        findNavController().navigate(
            R.id.action_selectPostFragment_to_editPostFragment,
            Bundle().apply {
                textArg = post.content
                longArg = post.id
            })
    }

    private fun playVideo(post: Post) {
        if (post.video.isNullOrBlank())
            return
        val uri = Uri.parse(post.video)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun sharePost(post: Post) {
        Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, post.content)
            .setType("text/plain")
            .also {
                if (context?.packageManager?.let { it1 -> it.resolveActivity(it1) } == null) {
                    Toast.makeText(
                        this@SelectPostFragment.context,
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
}
package ru.netology.androidbasic_3_1_1.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import ru.netology.androidbasic_3_1_1.*
import ru.netology.androidbasic_3_1_1.adapter.PostsAdapter
import ru.netology.androidbasic_3_1_1.args.LongArgs
import ru.netology.androidbasic_3_1_1.args.StringArgs
import ru.netology.androidbasic_3_1_1.databinding.FragmentFeedBinding
import ru.netology.androidbasic_3_1_1.dto.Post
import ru.netology.androidbasic_3_1_1.viewModel.PostViewModel


class FeedFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArgs
        var Bundle.longArg: Long by LongArgs
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) { editPost(post) }
            override fun onLike(post: Post) {viewModel.likeById(post.id) }
            override fun onRemove(post: Post) { viewModel.removeById(post.id) }
            override fun onShare(post: Post) { sharePost(post) }
            override fun onPlay(post: Post) { playVideo(post) }
            override fun onSelectPost(post: Post) { selectPost(post) }
        })

        binding.recyclerViewPosts.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts -> adapter.submitList(posts) }

        binding.floatingButtonAddPost.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }

    private fun selectPost(post: Post) {
        val postId = post.id
        findNavController().navigate(R.id.action_feedFragment_to_selectPostFragment,
            Bundle().apply { longArg = postId })
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

    private fun editPost(post: Post) {
        findNavController().navigate(R.id.action_feedFragment_to_editPostFragment,
            Bundle().apply {
                textArg = post.content
                longArg = post.id
            })
    }


}
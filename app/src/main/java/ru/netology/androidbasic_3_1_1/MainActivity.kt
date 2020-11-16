package ru.netology.androidbasic_3_1_1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.observe
import ru.netology.androidbasic_3_1_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val NEW_POST_REQUEST_CODE = 1
        private const val EDIT_POST_REQUEST_CODE = 2
    }

    private val viewModel: PostViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                        if (it.resolveActivity(packageManager) == null) {
                            Toast.makeText(
                                this@MainActivity,
                                "app not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Intent.createChooser(it, getString(R.string.chooser_share_post))
                                .also(::startActivity);
                        }
                    }

                viewModel.shareById(post.id)
            }
        })
        binding.recylerViewPosts.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
//            with (binding.editTextContent){
//                requestFocus()
//                setText(post.content)
//            }
            binding.groupRevert.visibility = View.VISIBLE
            with(binding.textViewEditContent) {
                text = post.content
            }
        }

//        binding.imageButtonSave.setOnClickListener {
//            with(binding.editTextContent){
//                if (TextUtils.isEmpty(text)){
//                    Toast.makeText(
//                        this@MainActivity,
//                        context.getString(R.string.error_empty_content),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    binding.groupRevert.visibility = View.GONE
//                    return@setOnClickListener
//                }
//
//                viewModel.changeContent(text.toString())
//                viewModel.save()
//
//                setText("")
//                clearFocus()
//                AndroidUtils.hideKeyboard(this)
//            }
//            binding.groupRevert.visibility = View.GONE
//        }
//
//        binding.imageButtonCloseEditing.setOnClickListener {
//            binding.groupRevert.visibility = View.GONE
//            with(binding.editTextContent){
//                if (TextUtils.isEmpty(text)){
//                    Toast.makeText(
//                        this@MainActivity,
//                        context.getString(R.string.error_empty_content),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
//                }
//
//                setText("")
//                clearFocus()
//                AndroidUtils.hideKeyboard(this)
//            }
//        }

        binding.floatingButtonAddPost.setOnClickListener {
            startActivityForResult(Intent(this, NewPostActivity::class.java), NEW_POST_REQUEST_CODE)
        }
    }

    private fun editPost(post: Post) {
        startActivityForResult(
            Intent(this, EditPostActivity::class.java)
                .putExtra(Intent.EXTRA_TEXT, post.content).putExtra(Intent.EXTRA_UID, post.id),
            EDIT_POST_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val text = data.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.error_empty_content),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            viewModel.changeContent(text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(binding.root)
        }
        if (requestCode == EDIT_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val text = data.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.error_empty_content),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            val id = data.getIntExtra(Intent.EXTRA_UID, -1)

            viewModel.edit(id.toLong(), text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(binding.root)
        }
    }

    object AndroidUtils {
        fun hideKeyboard(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
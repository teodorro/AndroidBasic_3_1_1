package ru.netology.androidbasic_3_1_1


import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.androidbasic_3_1_1.databinding.CardPostBinding

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            textViewAuthor.text = post.author
            textViewMessage.text = post.content
            textViewPublished.text = post.published
            textViewViews.text = convertIntToStr(post.views)
            materialButtonLikes.text = convertIntToStr(post.likes)
            materialButtonLikes.isChecked = post.likedByMe
            materialButtonLikes.setOnClickListener { onInteractionListener.onLike(post)}
            materialButtonShares.text = convertIntToStr(post.shares)
            materialButtonShares.setOnClickListener { onInteractionListener.onShare(post) }
            materialButtonMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId){
                            R.id.removeMenuItem -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.editMenuItem -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

    private fun convertIntToStr(value: Int): String{
        return when(value){
            in 0..999 -> value.toString()
            in 1000..1099 -> (value / 1000).toString() + "K"
            in 1100..9999 -> ((value / 100).toDouble() / 10).toString() + "K"
            in 10000..999000 -> (value / 1000).toString() + "K"
            in 1000000..1099999 -> (value / 1000000).toString() + "M"
            in 1100000..9999999 -> ((value / 100000).toDouble() / 10).toString() + "M"
            in 10000000..999999999 -> (value / 1000000).toString() + "M"
            else -> value.toString()
        }
    }
}

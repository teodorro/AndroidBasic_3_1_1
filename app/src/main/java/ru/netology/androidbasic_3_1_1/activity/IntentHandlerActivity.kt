package ru.netology.androidbasic_3_1_1.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.androidbasic_3_1_1.R
import ru.netology.androidbasic_3_1_1.activity.FeedFragment.Companion.textArg

class IntentHandlerActivity : AppCompatActivity(R.layout.activity_intent_handler) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        intent?.let{
//            if (it.action != Intent.ACTION_SEND)
//                return@let
//
//            val text = it.getStringExtra(Intent.EXTRA_TEXT)
//            if (text.isNullOrBlank()){
//                intent.removeExtra(Intent.EXTRA_TEXT)
//                findNavController(R.id.action_feedFragment_to_editPostFragment)
//                    .navigate(
//                        R.id.action_feedFragment_to_editPostFragment,
//                        Bundle().apply { textArg = text }
//                    )
////                Snackbar.make(binding.root, "content can't be ampty", LENGTH_INDEFINITIVE)
////                    .setAction(android.R.string.ok){
////                        finish()
////                    }
////                    .show()
//                return@let
//            }
//            // TODO: handle text
//        }
    }
}
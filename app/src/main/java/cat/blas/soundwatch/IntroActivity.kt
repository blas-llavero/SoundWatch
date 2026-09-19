package cat.blas.soundwatch

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import java.util.Locale

class IntroActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSharedPreferences(PREFERENCES, MODE_PRIVATE)
            .edit()
            .putBoolean(INTRO_SEEN, true)
            .apply()

        val density = resources.displayMetrics.density
        val padding = (16 * density).toInt()
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(padding, padding, padding, padding)
        }
        val title = TextView(this).apply {
            text = getString(R.string.introduction_title)
            textSize = 24f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, padding)
        }
        val video = VideoView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            contentDescription = getString(R.string.introduction_video_description)
        }
        val close = Button(this).apply {
            text = getString(R.string.continue_to_app)
            setOnClickListener { finish() }
        }

        container.addView(title)
        container.addView(video)
        container.addView(close)
        setContentView(container)

        val controller = MediaController(this)
        controller.setAnchorView(video)
        video.setMediaController(controller)
        val videoResource = when (Locale.getDefault().language) {
            "ar" -> R.raw.soundwatch_intro_ar
            "ca" -> R.raw.soundwatch_intro_ca
            "de" -> R.raw.soundwatch_intro_de
            "es" -> R.raw.soundwatch_intro_es
            "fr" -> R.raw.soundwatch_intro_fr
            "hi" -> R.raw.soundwatch_intro_hi
            "it" -> R.raw.soundwatch_intro_it
            "pt" -> R.raw.soundwatch_intro_pt
            "zh" -> R.raw.soundwatch_intro_zh
            else -> R.raw.soundwatch_intro
        }
        video.setVideoURI(
            Uri.parse("android.resource://$packageName/$videoResource")
        )
        video.setOnPreparedListener {
            video.seekTo(1)
            video.start()
        }
    }

    companion object {
        const val PREFERENCES = "onboarding"
        const val INTRO_SEEN = "introduction_seen"
    }
}

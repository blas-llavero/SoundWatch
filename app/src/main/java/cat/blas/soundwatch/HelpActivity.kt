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

class HelpActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val density = resources.displayMetrics.density
        val padding = (16 * density).toInt()
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(padding, padding, padding, padding)
        }
        val title = TextView(this).apply {
            text = getString(R.string.help_title)
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
            contentDescription = getString(R.string.help_video_description)
        }
        val close = Button(this).apply {
            text = getString(R.string.close)
            setOnClickListener { finish() }
        }

        container.addView(title)
        container.addView(video)
        container.addView(close)
        setContentView(container)

        val controller = MediaController(this)
        controller.setAnchorView(video)
        video.setMediaController(controller)
        video.setVideoURI(
            Uri.parse("android.resource://$packageName/${R.raw.soundwatch_help}")
        )
        video.setOnPreparedListener {
            video.seekTo(1)
            video.start()
        }
    }
}

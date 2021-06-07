package com.softaai.basicvideoplayer.binding

import android.R
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.VideoView
import androidx.databinding.BindingAdapter
import com.softaai.basicvideoplayer.videoplayer.VideoViewModel


object BindingAdapters {

    lateinit var runnbale: Runnable
    lateinit var handler: Handler

    @JvmStatic
    @BindingAdapter("visibility")
    fun View.setVisibility(visible: Boolean) {
        visibility = if (visible)
            View.VISIBLE
        else
            View.GONE
    }

    @JvmStatic
    @BindingAdapter("mediaPlayer")
    fun bindMediaPlayer(imageView: ImageView, mediaPlayer: MediaPlayer) {

        imageView.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                imageView.setImageResource(R.drawable.ic_media_play)
            } else {
                mediaPlayer.start()
                imageView.setImageResource(R.drawable.ic_media_pause)
            }
        }

    }

    @JvmStatic
    @BindingAdapter("textProgress")
    fun bindTextProgress(textProgress: TextView, progress: String) {
        textProgress.text = progress
    }

    @JvmStatic
    @BindingAdapter("textTotalTime")
    fun bindTextTotalTime(textTotalTime: TextView, totalTime: String) {
        textTotalTime.text = totalTime
    }


    @JvmStatic
    @BindingAdapter("mediaPlayer", "uriString", "context")
    fun bindSurfaceHolder(videoView: VideoView, mediaPlayer: MediaPlayer, uriString: String, context: Context) {
        videoView.holder.addCallback(object : Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                mediaPlayer.apply {
                    setDataSource(context.applicationContext, Uri.parse(uriString))
                    //setDataSource(applicationContext, selectedVideoUri)

                    //setDataSource(URL)
                    setDisplay(holder)
                    prepareAsync()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

        })
    }


    @JvmStatic
    @BindingAdapter("mediaPlayer", "viewModel")
    fun bindMediaPlayerOnPreparedInitialize(seekBar: SeekBar, mediaPlayer: MediaPlayer, viewModel: VideoViewModel) {


        mediaPlayer.setOnPreparedListener {
            // initialize
            val seconds = mediaPlayer.duration / 1000
            seekBar.max = seconds
            viewModel.textProgress.value = "0:0"
            viewModel.textTotalTime.value = timeInString(seconds)
            viewModel.isProgressBarVisible.value = false
            viewModel.isPlayButtonEnabled.value = true


            // update
            handler = Handler(Looper.getMainLooper())
            runnbale = Runnable {
                val currentSeconds = mediaPlayer.currentPosition / 1000
                viewModel.textProgress.value = timeInString(currentSeconds)
                seekBar.progress = currentSeconds
                handler.postDelayed(runnbale, seconds.toLong())
            }

            handler.postDelayed(runnbale, seconds.toLong())

        }


        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }


    private fun timeInString(seconds: Int): String {
        return String.format(
                "%02d:%02d",
                (seconds / 3600 * 60 + ((seconds % 3600) / 60)),
                (seconds % 60)
        )
    }
}





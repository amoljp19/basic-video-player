package com.softaai.basicvideoplayer.binding

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
    @BindingAdapter("playPauseImageView")
    fun bindPlayPauseImageView(playPauseImageView: ImageView, mediaPlayer: MediaPlayer) {

        playPauseImageView.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playPauseImageView.setImageResource(R.drawable.ic_media_play)
            } else {
                mediaPlayer.start()
                playPauseImageView.setImageResource(R.drawable.ic_media_pause)
            }
        }
    }


    @JvmStatic
    @BindingAdapter("playStopImageView")
    fun bindPlayStopImageView(playStopImageView: ImageView, mediaPlayer: MediaPlayer) {

        playStopImageView.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                playStopImageView.setImageResource(com.softaai.basicvideoplayer.R.mipmap.ic_media_stop)
            } else {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
                playStopImageView.setImageResource(R.drawable.ic_media_play)
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
    @BindingAdapter("mediaPlayer", "viewModel", "context")
    fun bindSurfaceHolder(videoView: VideoView, mediaPlayer: MediaPlayer, videoViewModel: VideoViewModel, context: Context) {
        videoView.holder.addCallback(object : Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                mediaPlayer.apply {
                    setDataSource(context.applicationContext, Uri.parse(videoViewModel.uriString.value))
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

            //Show Dialog
            val alertDialogBuilder = AlertDialog.Builder(seekBar.context)
            alertDialogBuilder.setTitle("Paused")
            alertDialogBuilder.setMessage("Still you want to play this video ")
            alertDialogBuilder.setPositiveButton("Yes", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    if(!mediaPlayer.isPlaying){
                        mediaPlayer.start()
                        dialog?.dismiss()
                    }
                }
            })

            val alertDialog = alertDialogBuilder.create()

            // update
            handler = Handler(Looper.getMainLooper())
            runnbale = Runnable {
                val currentSeconds = mediaPlayer.currentPosition / 1000
                viewModel.textProgress.value = timeInString(currentSeconds)
                seekBar.progress = currentSeconds
                if((currentSeconds > 0) && (currentSeconds % 60 == 0)){
                     if(mediaPlayer.isPlaying){
                         mediaPlayer.pause()
                         alertDialog.show()
                     }
                }
                handler.postDelayed(runnbale, 1000)
            }

            handler.postDelayed(runnbale, 1000)

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





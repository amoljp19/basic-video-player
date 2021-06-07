package com.softaai.basicvideoplayer.videoplayer

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.softaai.basicvideoplayer.R
import com.softaai.basicvideoplayer.databinding.ActivityVideoBinding

@RequiresApi(Build.VERSION_CODES.O)
class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    private val videoViewModel: VideoViewModel by viewModels()

    companion object {
        const val GET_VIDEO = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_video)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding.setLifecycleOwner(this)
        binding.viewmodel = videoViewModel
        binding.mediaPlayer = MediaPlayer()

        videoViewModel.isPlayButtonEnabled.value = true
        videoViewModel.isProgressBarVisible.value = false
        //videoViewModel.uriString.value = "android.resource://$packageName/raw/video"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select_file -> {
                val intent = Intent()
                intent.type = "video/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                getString(R.string.select_file)
                        ), GET_VIDEO
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_VIDEO) {
                videoViewModel.uriString.value = data?.data!!.toString()
            }
        }
    }

}
package com.softaai.basicvideoplayer.videoplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideoViewModel : ViewModel() {

    val isPlayButtonEnabled = MutableLiveData<Boolean>()

    val isProgressBarVisible = MutableLiveData<Boolean>()

    val uriString = MutableLiveData("")

    val textProgress = MutableLiveData("0:0")

    val textTotalTime = MutableLiveData("0:0")

}
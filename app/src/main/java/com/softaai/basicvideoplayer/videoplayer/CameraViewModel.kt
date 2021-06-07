package com.softaai.basicvideoplayer.videoplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {

    val takePicture = MutableLiveData<Boolean>()

    fun onTakePicture(){
        takePicture.postValue(true)
    }
}
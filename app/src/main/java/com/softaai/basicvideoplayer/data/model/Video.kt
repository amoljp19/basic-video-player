package com.softaai.basicvideoplayer.data.model

import androidx.room.PrimaryKey

data class Video(
    @PrimaryKey var id: Int,
    var videoName: String?,
    var path: String,
)
package com.example.tiktok.model

import com.google.firebase.Timestamp


data class VideoModel(
    var videoId:String="",
    var title:String="",
    var url:String="",
    var uploade:String="",
   var createdTime: Timestamp = Timestamp.now()
)

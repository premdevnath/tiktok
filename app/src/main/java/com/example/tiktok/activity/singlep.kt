package com.example.tiktok.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.tiktok.adapter.adapter
import com.example.tiktok.databinding.ActivitySinglepBinding
import com.example.tiktok.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


//jese ek frgament se dusre fragment me jane ke liye nav ka use karte hai vese hi activity ke liye viewpager ka use karte hai
class singlep : AppCompatActivity() {
    lateinit var binding: ActivitySinglepBinding
    lateinit var videoId:String
    lateinit var sadapter: adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySinglepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoId=intent.getStringExtra("videoId")!!
        setupviewpager()

    }

   fun setupviewpager() {
        val option= FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos")
                    .whereEqualTo("videoId",videoId),
                VideoModel::class.java
            ).build()
        sadapter= adapter(option)
        binding.viewpager.adapter=sadapter

    }

    override fun onStart() {
        super.onStart()
        sadapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        sadapter.stopListening()
    }

}
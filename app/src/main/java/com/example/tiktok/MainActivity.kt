package com.example.tiktok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tiktok.activity.Add
import com.example.tiktok.activity.profile
import com.example.tiktok.adapter.adapter
import com.example.tiktok.databinding.ActivityMainBinding
import com.example.tiktok.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//bottom nav with activity
        binding.bottomNavigationView.setOnItemSelectedListener {menuItem->
            when(menuItem.itemId){
                R.id.home2->{

                }
                R.id.add2->{
                    var intent=Intent(this,Add::class.java)
                    startActivity(intent)
                }
                R.id.profile->{
                    Log.d("hiby", "userdata: ")
                    var intent=Intent(this,profile::class.java)
                    //jis user ne login kiya uski id profile activity ko di
                    Log.d("hiby", "userdata: ")
                    intent.putExtra("id",FirebaseAuth.getInstance().currentUser?.uid)
                    Log.d("hiby", "userdata: ")
                    startActivity(intent)
                }
            }
            false
        }
        //
        setupviewpager()

    }

    private fun setupviewpager() {
        val option=FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos"),
                VideoModel::class.java
            ).build()
        adapter= adapter(option)
        binding.viewpager.adapter=adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }
}
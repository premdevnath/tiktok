package com.example.tiktok.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktok.databinding.VideoitemrowBinding
import com.example.tiktok.model.VideoModel
import com.example.tiktok.model.usermodal
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tiktok.R
import com.example.tiktok.activity.profile
import com.google.firebase.firestore.firestore

//fire store se direct video adapter me lekar ui me show karni hai to ye tarika use karte hai
class adapter(options: FirestoreRecyclerOptions<VideoModel>):FirestoreRecyclerAdapter<VideoModel,adapter.ViewHolder>(options ){


    inner class ViewHolder(var binding:VideoitemrowBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(videomodel:VideoModel)
        {
            Firebase.firestore.collection("users")
                .document(videomodel.uploade)
                .get().addOnSuccessListener {
                    val userModel = it?.toObject(usermodal::class.java)
                    userModel?.apply{
                        binding.usernameView.text = name
                        //bind profilepic
                        Glide.with(binding.profileIcon).load(profilepic)
                            .circleCrop()
                            .apply(RequestOptions().placeholder(R.drawable.profile))
                            .into(binding.profileIcon)

                        //

                        binding.userDetailLayout.setOnClickListener {
                            val intent = Intent(binding.userDetailLayout.context, profile::class.java)
                            intent.putExtra("id",id)
                            binding.userDetailLayout.context.startActivity(intent)
                        }
                    }
                }



            binding.videoView.apply {
                setVideoPath(videomodel.url)
                setOnPreparedListener{
                    binding.progress.visibility=View.GONE
                    it.start()
                    it.isLooping=true
                }
                //

                binding.messageView.text=videomodel.title
                binding.progress.visibility=View.VISIBLE

                //paly pause
                //(yaha se video play or satrt ho rha hai)
                setOnClickListener(){
                    if (isPlaying){
                        pause()
                        binding.pause.visibility=View.VISIBLE
                    }else{
                        start()
                        binding.pause.visibility=View.GONE
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater=LayoutInflater.from(parent.context)
        val binding=VideoitemrowBinding.inflate( layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: VideoModel) {
holder.bind(model)
    }


}
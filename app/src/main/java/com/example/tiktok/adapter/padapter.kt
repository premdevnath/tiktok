package com.example.tiktok.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tiktok.activity.singlep
import com.example.tiktok.databinding.ProitemBinding
import com.example.tiktok.databinding.VideoitemrowBinding
import com.example.tiktok.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class padapter (options: FirestoreRecyclerOptions<VideoModel>):
    FirestoreRecyclerAdapter<VideoModel, padapter.ViewHolder>(options ){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): padapter.ViewHolder {
        var layoutInflater= LayoutInflater.from(parent.context)
        val binding=ProitemBinding.inflate( layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: padapter.ViewHolder, position: Int, model: VideoModel) {
        holder.bind(model)
    }
    class ViewHolder(var binding: ProitemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(videomodel:VideoModel) {
            Glide.with(binding.timage).load(videomodel.url)
                .into(binding.timage)
            //
            binding.timage.setOnClickListener(){
                val intent= Intent(binding.timage.context,singlep::class.java)
                intent.putExtra("videoId",videomodel.videoId)
                binding.timage.context.startActivity(intent)
            }
        }
    }
}
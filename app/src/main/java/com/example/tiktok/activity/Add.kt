package com.example.tiktok.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.tiktok.databinding.ActivityAddBinding
import com.example.tiktok.model.VideoModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class Add : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    private var selectedVideo:Uri?=null

    //Rule agar ham ko gallry se kuch le hai video yaha iamge to aise luncher ka use hota hai
    lateinit var videoLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Rule agar ham ko gallry se kuch le hai video yaha iamge to aise luncher ka use hota hai
        //menifest me permission deni hoti hai read meadi video
        videoLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if (result.resultCode== RESULT_OK){
                selectedVideo=result.data?.data
                showpostview()
            }

        }
        binding.uploadView.setOnClickListener(){
            openvideopicker()
        }
        binding.submitPostBtn.setOnClickListener(){

            postvideo()
        }
        binding.cancelPostBtn.setOnClickListener(){
            finish()
        }
    }

    private fun postvideo() {
        if (binding.postcap.text.toString().isEmpty()) {
            binding.postcap.setError("write someting")
            return
        }

        setIntprogress(true)
        selectedVideo?.apply {
val videoRef=FirebaseStorage.getInstance()
    .reference
    .child("videos/"+ this.lastPathSegment)
            videoRef.putFile(this)
                .addOnSuccessListener {
                    videoRef.downloadUrl.addOnSuccessListener { downloadUrl->
posttofitestore(downloadUrl.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    setIntprogress(false)
                    Toast.makeText( this@Add, "Video upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //
    private fun posttofitestore(url:String){
    val videoModel=VideoModel(
    FirebaseAuth.getInstance().currentUser?.uid!! + "_"+Timestamp.now().toString(),
    binding.postcap.text.toString(),
    url,
    FirebaseAuth.getInstance().currentUser?.uid!!,
    Timestamp.now(),
)
        Firebase.firestore.collection("videos")
            .document(videoModel.videoId)
            .set(videoModel)
            .addOnSuccessListener {
                setIntprogress(false)
                Toast.makeText(this, "video uploaded", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                setIntprogress(false)
                Toast.makeText(this, "video uploaded faild", Toast.LENGTH_SHORT).show()
            }


    }
    private fun setIntprogress(inProgress:Boolean) {

        if (inProgress){
            binding.progressBar.visibility=View.VISIBLE
            binding.submitPostBtn.visibility=View.GONE
        }
        else{
            binding.progressBar.visibility=View.GONE
            binding.submitPostBtn.visibility=View.VISIBLE
        }
    }

    private fun showpostview() {
        selectedVideo.let {
            binding.postView.visibility = View.VISIBLE
            binding.uploadView.visibility = View.GONE
            Glide.with(binding.postThumbnailView).load(it).into(binding.postThumbnailView)
        }
    }

    private fun openvideopicker() {
        var readExternalVideo:String=""
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            readExternalVideo=android.Manifest.permission.READ_MEDIA_VIDEO

        }
        else{
            readExternalVideo=android.Manifest.permission.READ_EXTERNAL_STORAGE

        }
        if (ContextCompat.checkSelfPermission(this,readExternalVideo)== PackageManager.PERMISSION_GRANTED){
            videopicker()
        }
        else{
            ActivityCompat.requestPermissions(
                this, arrayOf(readExternalVideo),100
            )
        }
    }

    private fun videopicker() {
       // Toast.makeText(this, "video p", Toast.LENGTH_SHORT).show()
        var intent=Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type="video/*"
        videoLauncher.launch(intent)
    }
}
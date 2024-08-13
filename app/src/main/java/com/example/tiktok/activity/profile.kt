
package com.example.tiktok.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tiktok.R
import com.example.tiktok.adapter.adapter
import com.example.tiktok.adapter.padapter
import com.example.tiktok.databinding.ActivityProfileBinding
import com.example.tiktok.model.VideoModel
import com.example.tiktok.model.usermodal
import com.firebase.ui.firestore.FirestoreRecyclerOptions

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class profile : AppCompatActivity() {

    lateinit var adapter: padapter
    lateinit var binding: ActivityProfileBinding
    lateinit var profileUserId: String
    lateinit var currentUserId: String
    lateinit var profileUserModel: usermodal
    //photo uplode karne ke liye firestore me
    lateinit var photoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //w
        Log.d("hi1", "userdata: ")
        profileUserId = intent.getStringExtra("id")!!

        Log.d("hiend", "userdata: ${profileUserId}")

        currentUserId = FirebaseAuth.getInstance().currentUser?.uid!!


        //Rule 1 agar ham ko gallry se kuch le hai video yaha iamge to aise luncher ka use hota hai
        //menifest me permission deni hoti hai read meadi video
        photoLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->

            if (result.resultCode== RESULT_OK){
                uplodefirestore(result.data?.data)
                Log.d("hi2", "userdata: ")
            }

        }



        Log.d("hi3", "userdata: ")
        if (profileUserId == currentUserId) {
            // Current user profile
            binding.profileLogoutButton.text = "Logout"
            binding.profileLogoutButton.setOnClickListener(){
                logout()
            }

            //2
            binding.pp.setOnClickListener(){
                Log.d("hi2by", "userdata: ")
                chekpermissionandpickphoto()
            }


        }
        else{
          binding.profileLogoutButton.text="Follow"
            binding.profileLogoutButton.setOnClickListener(){
                followUnfollowUser()
            }
        }


            // Other user profile
            Log.d("hi4", "userdata: ")
            getProfileDataFromFirebase()
        Log.d("hi5", "userdata: ")
        ssetuprvi()

    }

    private fun followUnfollowUser() {
        Log.d("hi6", "userdata: ")
        Firebase.firestore.collection("users")
            .document(currentUserId) // DocumentReference
            .get() // Task<DocumentSnapshot>
            .addOnSuccessListener {
                val currentUserModel = it.toObject(usermodal::class.java)!!
                if (profileUserModel.followerList.contains(currentUserId)) {
                    // Unfollow user
                    profileUserModel.followerList.remove(currentUserId)
                    currentUserModel.followingList.remove(profileUserId)
                    binding.profileLogoutButton.text = "Follow"
                } else {
                    // Follow user
                    profileUserModel.followerList.add(currentUserId)
                    currentUserModel.followingList.add(profileUserId)
                    binding.profileLogoutButton.text = "Unfollow"
                }
                updateUserData(profileUserModel)
                updateUserData(currentUserModel)
            }
    }

    //n
    private fun updateUserData(modal: usermodal) {
        Log.d("h7", "userdata: ")
        Firebase.firestore.collection("users")
            .document(modal.id!!)
            .set(modal) // Task<Void>
            .addOnSuccessListener {
                getProfileDataFromFirebase()
            }
    }


    //4
    private fun chekpermissionandpickphoto() {

        Log.d("hi5by", "userdata: ")
        var readExternalPhoto:String=""
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            readExternalPhoto=android.Manifest.permission.READ_MEDIA_VIDEO

        }
        else{
            readExternalPhoto=android.Manifest.permission.READ_EXTERNAL_STORAGE

        }
        if (ContextCompat.checkSelfPermission(this,readExternalPhoto)== PackageManager.PERMISSION_GRANTED){
            photopicker()
        }
        else{
            ActivityCompat.requestPermissions(
                this, arrayOf(readExternalPhoto),100
            )
        }
    }

    //5
    private fun photopicker() {
        Log.d("hi6by", "userdata: ")
        // Toast.makeText(this, "video p", Toast.LENGTH_SHORT).show()
        var intent=Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type="image/*"
        photoLauncher.launch(intent)

    }


    //3

    private fun uplodefirestore(photouri: Uri?) {

        val photoRef=FirebaseStorage.getInstance()
            .reference
            .child("profilepic/"+ currentUserId)
        if (photouri != null) {
            photoRef.putFile(photouri)
                .addOnSuccessListener {
                    photoRef.downloadUrl.addOnSuccessListener { downloduri->
                        posttofirestore(downloduri.toString())
                    }
                }
        }
    }

    private fun posttofirestore(url:String){

        Firebase.firestore.collection("users")
            .document(currentUserId)
            .update("profilepic",url)
            .addOnSuccessListener {
               getProfileDataFromFirebase()
                Toast.makeText(this, "pic update", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {

                Toast.makeText(this, "pic update faild", Toast.LENGTH_SHORT).show()
            }

    }



//c1
    fun ssetuprvi(){
    Log.d("hi1.23", "userdata: ")
        val option=FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos")
                    .whereEqualTo("uploade",profileUserId)
                    .orderBy("createdTime",Query.Direction.DESCENDING),
                VideoModel::class.java
            ).build()
    Log.d("hi1.23", "userdata: ${profileUserId}")
    Log.d("hi1.23", "userdata: ")
    adapter= padapter(option)
    Log.d("hi1.23", "userdata: ${option}")
    binding.rvi.layoutManager=GridLayoutManager(this,3)
    binding.rvi.adapter=adapter

    }







//
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        var intent= Intent(this,login::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    //
    private fun getProfileDataFromFirebase() {
        Log.d("hi4.1", "userdata: ${profileUserId}")
        Firebase.firestore.collection("users")
            .document(profileUserId)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    Log.d("hi4.1.1", "userdata: $it")
                    profileUserModel = it.toObject(usermodal::class.java)!!
                    if (profileUserModel != null) {
                        Log.d("hi4.1.2", "userdata: ")
                        setUI()
                    } else {
                        Log.d("hi4.1.2", "userdata: Profile data is null")
                    }
                } else {
                    Log.d("hi4.1.1", "userdata: Document does not exist")
                }
            }
            .addOnFailureListener {
                Log.d("hi4.1.3", "userdata: ")
            }
    }


    //y
    private fun setUI() {
          Log.d("hi4.2", "userdata: ")
        profileUserModel.apply {

            Glide.with(binding.pp.context)
                .load(profilepic)
                .apply(RequestOptions().placeholder(R.drawable.profile))
                .circleCrop()
                .into(binding.pp)

            binding.profileUsername.text = "@${name}"
            //
            if (profileUserModel.followerList.contains(currentUserId))
                binding.profileLogoutButton.text="Unfollow"
            binding.profileProgressBar.visibility = View.INVISIBLE
            binding.followingCount.text = followingList.size.toString()
            binding.followerCount.text = followerList.size.toString()
            Firebase.firestore.collection("videos")
                .whereEqualTo("uploade",profileUserId)
                .get()
                .addOnSuccessListener {
                    binding.postsCount.text=it.size().toString()
                }        }
    }
    //
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }


    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}


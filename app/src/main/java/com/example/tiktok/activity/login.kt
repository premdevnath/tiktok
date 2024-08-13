package com.example.tiktok.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tiktok.MainActivity
import com.example.tiktok.R
import com.example.tiktok.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth




        binding.singupi.setOnClickListener()
        {
            var intent = Intent(this,singup::class.java)
            startActivity(intent)
        }
        binding.logii.setOnClickListener()
        {
            var emailp = binding.email.text.toString()
            var pass = binding.passwordd.text.toString()
            if (emailp.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "please fill both are frist", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(emailp, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "invalid try again", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

    }


    override fun onStart() {
        super.onStart()
        var currentUser = auth.currentUser
        if (currentUser != null) {

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

}

package com.example.tiktok.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.tiktok.MainActivity
import com.example.tiktok.databinding.ActivitySingupBinding
import com.example.tiktok.model.usermodal
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class singup : AppCompatActivity() {

    lateinit var binding: ActivitySingupBinding
    lateinit var auth: FirebaseAuth
    lateinit var name: String
    lateinit var email: String
    lateinit var pass: String
    lateinit var rpass: String
    lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.singin.setOnClickListener()
        {
            Log.d("hiby", "userdata: ")
            var intent= Intent(this,login::class.java)
            startActivity(intent)
        }

        binding.register.setOnClickListener {
            signup()
        }
    }


    fun signup() {
        val email = binding.emaill.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.repassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emaill.setError("Email not valid")
            return;
        }


        if (password.length < 6) {
            binding.password.setError("Minimum 6 character")
            return;
        }

        if (password != confirmPassword) {
            binding.repassword.setError("Password not matched")
            return;
        }
        signupWithFirebase(email, password)
    }

    fun signupWithFirebase(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            it.user?.let { user ->
                val userModel = usermodal(user.uid, email, email.substringBefore("@"))
                Firebase.firestore.collection("users")
                    .document(user.uid)
                    .set(userModel).addOnSuccessListener {
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT)
                            .show()

                        startActivity(Intent(this, login::class.java))
                        finish()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "no valid", Toast.LENGTH_SHORT).show()
        }
    }
}






















        /*
        Log.d("hi1", "userdata: ")

        //
        binding.singin.setOnClickListener()
        {
            Log.d("hiby", "userdata: ")
            var intent= Intent(this,login::class.java)
            startActivity(intent)
        }

        // authenticatin ke liye
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        //data save ke liye
        //databaseReference= Firebase.database.reference

        binding.register.setOnClickListener()
        {
            Log.d("hi1", "userdata: ")
            email=binding.emaill.text.toString().trim()
            name=binding.name.text.toString().trim()
            pass=binding.password.text.toString().trim()
            rpass=binding.repassword.text.toString().trim()
            if(email.isEmpty()||name.isEmpty()||pass.isEmpty()||rpass.isEmpty())
            {
                Log.d("hi1", "userdata: ")
                Toast.makeText(this, "please fill all blanks", Toast.LENGTH_SHORT).show()
            }
            else if(pass!=rpass)
            {
                Toast.makeText(this, "please fill both are same", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Log.d("hi0", "userdata: ")
                creatuser(email,pass)
                var intent= Intent(this,login::class.java)
                startActivity(intent)
                finish()

            }

        }

    }


    private fun creatuser(email: String, pass: String) {
        Log.d("hi!", "userdata: ")
        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this){ task->
                Log.d("hi1", "Account created successfully")
                if(task.isSuccessful)
                {
                    Log.d("hi1", "Account created successfully")
                    Log.d("hi1", "userdata: ")
                    //r1.6 agar accaount craeting task successful ho gya to toast show hoaga or saveuserdata fun call kiya
                    userdata()
                    Log.d("hi2", "userdata: ")
                    Toast.makeText(this, "creat accaount", Toast.LENGTH_SHORT).show()

                }
                else
                {
                    Log.d("hi1", "Account not created")
                    Log.d("hi3", "userdata: ")
                    Toast.makeText(this, "faild", Toast.LENGTH_SHORT).show()
                }

            }

    }
    //r yaha hamne user ke ragister data ko save akraya firebase me

    private fun userdata() {

        Log.d("hi4", "userdata: ")
        email = binding.emaill.text.toString().trim()
        name = binding.name.text.toString().trim()
        pass = binding.password.text.toString().trim()
     //   rpass = binding.repassword.text.toString().trim()
        //
        val userId = auth.currentUser?.uid
       //add user data
        val user = hashMapOf(
            "email" to email,
            "name" to name,
            "pass" to pass,
            "uid"  to userId,
        )


// Add a new document with a generated ID
        Log.d("hi5", "userdata: ")
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "User data saved", Toast.LENGTH_SHORT).show()
            }

            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
            }
       /*

        val user = usermodal(email, name, pass, rpass,userId)


        if (userId != null) {
          firestore.collection("users")
              .document(userId)
              .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "User data saved", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                }
        }

        */
    }
}

         */




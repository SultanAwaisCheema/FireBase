package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase.databinding.ActivityLoginWithGoogleBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore

class LoginWithGoogleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginWithGoogleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginWithGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val database = Firebase.database
        val usersRef = database.getReference("users")
        val email = binding.gmailTV.text.toString()
        val password = binding.passwordTV.text.toString()
        binding.signInBtn.setOnClickListener {
            if (email == "admin" && password == "123") {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                val userId = usersRef.push().key // Generate a unique key
                val user = LoginUser(binding.gmailTV.text.toString(), binding.passwordTV.text.toString(),"Pending")
                if (userId != null && binding.gmailTV.text.toString()
                        .isNotEmpty() && binding.passwordTV.text.toString().isNotEmpty()
                ) {
                    usersRef.child(userId).setValue(user)
                        .addOnSuccessListener {
                            // Data successfully written
                            Toast.makeText(
                                applicationContext,
                                "Request sent to admin successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            // Data write failed
                            Toast.makeText(
                                applicationContext,
                                " Not successful",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(applicationContext, "Failed to send request", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.withGoogleBtn.setOnClickListener {

        }


    }
}
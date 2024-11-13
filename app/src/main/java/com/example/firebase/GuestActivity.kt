package com.example.firebase

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase.databinding.ActivityGuestBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database

class GuestActivity : AppCompatActivity() {
    private lateinit var binding:ActivityGuestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val database = Firebase.database
        val usersRef = database.getReference("guest")
       binding.insertBTnG.setOnClickListener(View.OnClickListener {
        val name = binding.nameTVG.text.toString()
        val about = binding.aboutTvG.text.toString()
        // Create a new user
        val userId = usersRef.push().key // Generate a unique key
        val user = User(name, about)

        if (userId != null && binding.nameTVG.text.toString().isNotEmpty()&& binding.aboutTvG.text.toString().isNotEmpty()) {
            usersRef.child(userId).setValue(user)
                .addOnSuccessListener {
                    // Data successfully written
                    Toast.makeText(applicationContext, "Data written successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Data write failed
                    Toast.makeText(applicationContext, "Data Not successfully", Toast.LENGTH_SHORT).show()
                }
        }
           else{
            Toast.makeText(applicationContext, "Wrong Input", Toast.LENGTH_SHORT).show()
        }
    })
    }


    }

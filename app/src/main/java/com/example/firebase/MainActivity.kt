package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.guestActivity.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, GuestActivity::class.java)
            startActivity(intent)
        })
        binding.teacherActivity.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, TeacherActivity::class.java)
            startActivity(intent)
        })
        binding.studentActivity.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
        })
        binding.loginWithGoogleBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginWithGoogleActivity::class.java)
            startActivity(intent)
        })
    }
}
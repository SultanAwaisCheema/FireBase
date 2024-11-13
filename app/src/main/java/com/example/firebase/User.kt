package com.example.firebase

data class User(val username: String? = null, val about: String? = null)
data class LoginUser(val email: String, val password: String,val status: String)
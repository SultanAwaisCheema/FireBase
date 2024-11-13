package com.example.firebase

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.ActivityTeacherBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("UNCHECKED_CAST")
class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("teacher")
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TeacherAdapter
    private var teacherList: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.recyclerView
//        binding.recyclerView.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TeacherAdapter(teacherList,object : TeacherAdapter.UpdateListener {
                override fun updateListener(position: Int, name: String, about: String) {
                    binding.nameTV.setText(name)
                    binding.aboutTv.setText(about)
                    deleteTeacher(name)
                    updateTeacher(User(name,about))
//                        val myRef = FirebaseDatabase.getInstance().getReference("teacher")
//                        myRef.orderByChild("name").equalTo(name)
//                        myRef.orderByChild("about").equalTo(about)
////                        myRef.setValue("newValue")
//                        Toast.makeText(applicationContext, "update value=$name \n and update about=$about", Toast.LENGTH_SHORT).show()

                }
                override fun deleteListener(position: Int, name: String, about: String) {
                    deleteTeacher(name)
                }
            }
        )
        binding.insertBTn.setOnClickListener {
            val name = binding.nameTV.text.toString().trim()
            val about = binding.aboutTv.text.toString().trim()
            insertTeacher(name,about)
        }
        // Retrieve data and update RecyclerView
        recyclerView.adapter = adapter
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teacherList.clear()
                binding.recyclerView.visibility = View.VISIBLE
                for (data in snapshot.children) {
                    val teacher = data.getValue(User::class.java)
                    teacher?.let {
                        teacherList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TeacherActivity", "Failed to read value.", error.toException())
            }
        })
    }
    private fun insertTeacher(name:String,about:String){
        if (name.isNotEmpty() && about.isNotEmpty()) {
            // Query Firebase to check if user with the same name exists
            val query = usersRef.orderByChild("name").equalTo(name)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // User with the same name already exists
                        Toast.makeText(
                            applicationContext,
                            "User with this name already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // User does not exist, proceed with insertion
                        val userId = usersRef.push().key // Generate a unique key
                        val user = User(name, about)

                        if (userId != null) {
                            usersRef.child(userId).setValue(user)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Data written successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Clear input fields after successful insertion
                                    binding.nameTV.text.clear()
                                    binding.aboutTv.text.clear()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Failed to write data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Failed to generate unique ID",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    Toast.makeText(
                        applicationContext,
                        "Database error: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(applicationContext, "Wrong Input", Toast.LENGTH_SHORT).show()
        }
}
    private fun updateTeacher(teacher: User) {
        // Ensure username is not null
        val username = teacher.username ?: return
        // Query to find the node with matching username
        usersRef.orderByChild("name").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        // Update the data for each matching node
                        val userId = teacher.username
                        userId.let {
                            val updatedData = hashMapOf(
                                "username" to teacher.username,
                                "about" to teacher.about
                            )
                            usersRef.child(userId).updateChildren(updatedData as Map<String, Any>)
                                .addOnSuccessListener {
                                    Log.d("update", "Data updated successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("update", "Data update failed", e)
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Log.e("update", "Data update cancelled", error.toException())
                }
            })
    }
    private fun deleteTeacher(username: String) {
        val usersRef = FirebaseDatabase.getInstance().getReference("teacher")
        usersRef.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        data.ref.removeValue()
                    }
                    Log.d("delete", "Data deleted successfully")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("delete", "Data deletion failed", error.toException())
                }
            })
    }


}


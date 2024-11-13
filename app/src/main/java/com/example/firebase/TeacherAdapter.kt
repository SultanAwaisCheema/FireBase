package com.example.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class TeacherAdapter(private val teacherList: List<User>,private var listener: UpdateListener
) :
    RecyclerView.Adapter<TeacherAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val aboutTextView: TextView = itemView.findViewById(R.id.aboutTextView)
        val deleteImageView: ImageView = itemView.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_teacher, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teacher = teacherList[position]
        holder.nameTextView.text = "Name: ${teacher.username}"
        holder.aboutTextView.text = "About: ${teacher.about}"

        holder.nameTextView.setOnClickListener {
            listener.updateListener(position,teacherList[position].username.toString(),teacherList[position].about.toString())
        }

        holder.deleteImageView.setOnClickListener {
            listener.deleteListener(position,teacherList[position].username.toString(),teacherList[position].about.toString())
        }
    }

    override fun getItemCount(): Int {
        return teacherList.size
    }

    interface UpdateListener {
        fun updateListener(position: Int, name: String, about: String)
        fun deleteListener(position: Int, name: String, about: String)
    }
}

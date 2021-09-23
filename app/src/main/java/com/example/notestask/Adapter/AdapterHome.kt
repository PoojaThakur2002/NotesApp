package com.example.notestask.Adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notestask.R
import com.example.notestask.RoomDb.AddNotes
import com.example.notestask.UI.ActivityNotesDetail

class AdapterHome(val notes:List<AddNotes>) :RecyclerView.Adapter<AdapterHome.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHome.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_home, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AdapterHome.ViewHolder, position: Int) {
        holder.bindItems(notes[position])
    }

    override fun getItemCount(): Int {
        return  notes.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(addNotes: AddNotes) {
            val title = itemView.findViewById(R.id.tv_title) as TextView
            val image = itemView.findViewById(R.id.img_note) as ImageView
            title.text=addNotes.title

            if (addNotes.Image.isNotBlank()) {
                val c=itemView.context
                Glide.with(c).load(addNotes.Image).into(image)
                Log.e("adapterImage",addNotes.Image)
            }

            image.setOnClickListener {
                val intent= Intent(itemView.context,ActivityNotesDetail::class.java)
                    .putExtra("img",addNotes.Image)
                    .putExtra("title",addNotes.title)
                    .putExtra("des",addNotes.description)
                itemView.context.startActivity(intent)
            }
        }
    }
}
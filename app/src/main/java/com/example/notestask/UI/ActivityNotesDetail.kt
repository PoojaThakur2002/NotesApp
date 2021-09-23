package com.example.notestask.UI

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.notestask.BaseActivity
import com.example.notestask.R
import com.example.notestask.databinding.ActivityNotesDetailBinding

class ActivityNotesDetail : BaseActivity() {
    lateinit var binding: ActivityNotesDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val image = intent.getStringExtra("img")
        val title=intent.getStringExtra("title")
        val des=intent.getStringExtra("des")

        Glide.with(applicationContext).load(image).into(binding.imageSlider)
        binding.tvTile.text=title
        binding.tvDes.text=des
    }
}
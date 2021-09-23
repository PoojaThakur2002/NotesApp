package com.example.notestask.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notestask.Adapter.AdapterHome
import com.example.notestask.BaseActivity
import com.example.notestask.R
import com.example.notestask.RoomDb.DataBase
import com.example.notestask.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch

class ActivityHome : BaseActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvHome.layoutManager=LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
           launch {
                applicationContext?.let {
                    val data = DataBase(it).getlogin()
                        .getAllNotes()
                    binding.rvHome.adapter=AdapterHome(data)

                }
            }
        binding.btnAddNote.setOnClickListener {
            val intent = Intent(this,ActivityAddNotes::class.java)
            startActivity(intent)
        }
    }
}
package com.example.notestask.UI

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.loader.content.AsyncTaskLoader
import com.example.notestask.BaseActivity
import com.example.notestask.Helpers.toast
import com.example.notestask.R
import com.example.notestask.RoomDb.DataBase
import com.example.notestask.RoomDb.Login
import com.example.notestask.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch

class ActivitySignUp : BaseActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val name= binding.etName.text.toString()
            val mob= binding.etMob.text.toString()
            val email= binding.etEmail.text.toString()
            val password= binding.etPassword.text.toString()

            launch {
                var data=Login(name,mob,email,password)
                applicationContext?.let {
                    DataBase(it).getlogin().signUp(data)
                    Log.e("insert-------------->", data.toString())
                    val intent = Intent(it,ActivityLogin::class.java)
                    startActivity(intent)
                    it.toast("SignUp SuccessFully")
                }
            }
        }
    }
}
package com.example.notestask.UI

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.example.notestask.BaseActivity
import com.example.notestask.Helpers.toast
import com.example.notestask.RoomDb.DataBase
import com.example.notestask.RoomDb.Login
import com.example.notestask.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class ActivityLogin : BaseActivity() {
    val regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$"
    lateinit var binding: ActivityLoginBinding
    private lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        //check user login
        if (pref.getString("password","")!!.isNotBlank()){
            val intent = Intent(applicationContext, ActivityHome::class.java)
            startActivity(intent)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
        }else if(pref.getString("password","")!!.isEmpty())
        binding.btnLogin.setOnClickListener {
            launch {
                applicationContext?.let {
                    when {
                        binding.etMobLogin.text.toString().isEmpty() -> {
                            it.toast("Please Enter Mobile Number")
                        }
                        binding.etEmail.text.toString().isEmpty() -> {
                            it.toast("Please Enter EmailId")
                        }
                        binding.etPassword.text.toString().isEmpty() -> {
                            it.toast("Please Enter Password")
                        }
//                        binding.etEmail.text.toString().isEmailValid()->{
//                            it.toast("Email is incorrect")
//                        }
                        else -> {
                            val isValid = DataBase(it).getlogin()
                                .getLoginData(binding.etPassword.text.toString())
                            when {
                                isValid.password.isBlank()||isValid.password.isEmpty()||isValid.password.equals("") -> {
                                    it.toast("Password is no correct")
                                }
                                binding.etPassword.text.toString().isNotEmpty() && isValid.password.toString().isNotEmpty() &&    binding.etPassword.text.toString()== isValid.password -> {
                                    it.toast("Successfully Logged In")
                                    pref.edit().putString("password",isValid.password).apply()
                                    val intent = Intent(it, ActivityHome::class.java)
                                    startActivity(intent)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    finish()
                                }
                                else -> {
                                    it.toast("Login was not successful")
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this,ActivitySignUp::class.java)
            startActivity(intent)
        }
    }
     fun String.isEmailValid() =
            Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                            "\\@" +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                            "(" +
                            "\\." +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                            ")+"
            ).matcher(this).matches()
}
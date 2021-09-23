package com.example.notestask.RoomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Login(
    val name:String,
    val mobile:String,
    val email:String,
    val password:String
){
    @PrimaryKey(autoGenerate = true)
    var id :Int=0
}

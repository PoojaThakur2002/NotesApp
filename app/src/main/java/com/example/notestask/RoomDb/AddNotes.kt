package com.example.notestask.RoomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddNotes(
    val Image:String,
    val title:String,
    val description:String
){
    @PrimaryKey(autoGenerate = true)
    var id :Int=0
}
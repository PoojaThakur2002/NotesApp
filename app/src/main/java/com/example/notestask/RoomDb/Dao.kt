package com.example.notestask.RoomDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    suspend fun  signUp(login: Login)

    @Query("SELECT * FROM login WHERE login.password LIKE :password")
    suspend fun getLoginData(password:String): Login


    @Insert
    fun addNotes(addNotes: AddNotes)

    @Query("SELECT * FROM addnotes ORDER BY id DESC" )
    fun getAllNotes():List<AddNotes>
}
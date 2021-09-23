package com.example.notestask.RoomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Login::class,AddNotes::class],
    version = 3
)
abstract class DataBase :RoomDatabase(){

    abstract fun getlogin():Dao

    companion object {
        @Volatile var instance:DataBase? =null
        private val LOCK=Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            DataBase::class.java,
            "my_db"
        ) .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}
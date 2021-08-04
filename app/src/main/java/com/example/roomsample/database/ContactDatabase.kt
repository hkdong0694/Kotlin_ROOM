package com.example.roomsample.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * RoomSample
 * Class: ContactDatabase
 * Created by 한경동 (Joel) on 2021/08/04.
 * Description:
 */
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactsDao(): ContactDAO

    companion object {
        private var instance: ContactDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ContactDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java, "database-contacts"
                ).allowMainThreadQueries().build()
            }
            return instance
        }
    }
}
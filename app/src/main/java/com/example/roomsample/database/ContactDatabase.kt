package com.example.roomsample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * RoomSample
 * Class: ContactDatabase
 * Created by 한경동 (Joel) on 2021/08/04.
 * Description:
 */
@Database(entities = [Contacts::class], version = 1, exportSchema = false)
// exportSchema false -> 스키마 경고 안뜨게 설정
// version -> 처음 데이터베이스를 생성할 때 1 로 설정
// entities -> 한 개의 entities 를 가지면 위 처럼 설정
// 만약 여러개를 가진다면 entities = arrayOf(User::class, Student::class) 설정
abstract class ContactDatabase : RoomDatabase() {

    // DataBase와 연결되는 DAO ( Data Access Object )
    // 실질적으로 데이터베이스에 접근하는 객체
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
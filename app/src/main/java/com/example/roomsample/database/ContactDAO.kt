package com.example.roomsample.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * RoomSample
 * Class: ContactDAO
 * Created by 한경동 (Joel) on 2021/08/04.
 * Description:
 */
@Dao
interface ContactDAO {
    @Query("SELECT * FROM tb_contacts")
    fun getAll(): List<Contacts>

    @Insert
    fun insertAll(vararg contacts: Contacts)

    @Delete
    fun delete(contacts: Contacts)
}
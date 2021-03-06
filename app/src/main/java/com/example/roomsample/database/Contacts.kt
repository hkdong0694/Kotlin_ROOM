package com.example.roomsample.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * RoomSample
 * Class: Contacts
 * Created by 한경동 (Joel) on 2021/08/04.
 * Description:
 */
@Entity(tableName = "tb_contacts")
data class Contacts(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var name: String,
    var age: Int,
    var telNo: String,
    // 이미지 Uri 상태로 저장 x ( Uri -> String ) 으로 변환 후 저장
    var imageUri: String
)

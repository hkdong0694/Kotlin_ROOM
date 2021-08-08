package com.example.roomsample

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.roomsample.database.ContactDatabase
import com.example.roomsample.database.Contacts
import kotlinx.android.synthetic.main.activity_second.*
import java.net.URI

class SecondActivity : AppCompatActivity() {
    private var db: ContactDatabase?= null
    private var imageData : Uri? = null

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            imageData = result.data?.data
            Glide.with(this).load(imageData).into(iv_main)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        db = ContactDatabase.getInstance(this)
        iv_main.setOnClickListener {
            goToGalley()
        }

        btn_save.setOnClickListener {
            if(checkText() == 0) {
                var saveData = Contacts(0, et_name.text.toString(),
                    Integer.parseInt(et_age.text.toString()),
                    et_tel.text.toString(),
                    )
                db?.contactsDao()?.insertAll(saveData)
                // DB 데이터 저장
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "데이터를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkText() : Int {
        return if(et_name == null || et_name.text.toString() == "") {
            1
        } else if(et_age == null || et_age.text.toString() == "") {
            2
        } else if(et_tel == null || et_tel.text.toString() == "") {
            3
        } else if(imageData == null) {
            4
        } else 0
    }

    private fun goToGalley() {
        val intent = Intent()
        // 기기 기본 갤러리 접근
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startForResult.launch(intent)
    }

}
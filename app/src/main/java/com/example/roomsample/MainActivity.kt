package com.example.roomsample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roomsample.database.Contacts
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_main.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = DataAdapter()
        }

        btn_add.setOnClickListener {
            startForResult.launch(Intent(this, SecondActivity::class.java))
        }

    }

    class DataAdapter() : RecyclerView.Adapter<ViewHolder>() {

        private var itemData: List<Contacts> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.database_holder, parent, false)
            return ViewHolder(view)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = itemData[position]
            Glide.with(holder.itemView).load(data.image).into(holder.image)
            holder.index.text = "${position + 1}"
            holder.name.text = data.name
            holder.age.text = data.age.toString()
            holder.telNo.text = data.telNo
        }

        override fun getItemCount(): Int {
            return itemData.size
        }

    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.iv_item)
        var index: TextView = itemView.findViewById(R.id.tv_index)
        var name: TextView = itemView.findViewById(R.id.tv_name)
        var age: TextView = itemView.findViewById(R.id.tv_age)
        var telNo: TextView = itemView.findViewById(R.id.tv_tel)
    }
}
package com.example.roomsample

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.example.roomsample.database.ContactDatabase
import com.example.roomsample.database.Contacts
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var db: ContactDatabase?= null
    private var dbData : List<Contacts> = emptyList()
    private lateinit var dataAdapter: DataAdapter

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            dataAdapter.setData(db?.contactsDao()?.getAll()!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = ContactDatabase.getInstance(this)
        val data = db?.contactsDao()?.getAll()!!
        if(data.isNotEmpty()) {
            dbData = data
        }

        rv_main.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            dataAdapter = DataAdapter(dbData)
            adapter = dataAdapter
        }

        btn_add.setOnClickListener {
            startForResult.launch(Intent(this, SecondActivity::class.java))
        }

    }

    class DataAdapter(data : List<Contacts>) : RecyclerView.Adapter<ViewHolder>() {

        private var itemData: List<Contacts> = data

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.database_holder, parent, false)
            return ViewHolder(view)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = itemData[position]
            Glide.with(holder.itemView).load(Uri.parse(data.imageUri)).into(holder.image)
            holder.index.text = "${position + 1}"
            holder.name.text = data.name
            holder.age.text = data.age.toString()
            holder.telNo.text = data.telNo
        }

        override fun getItemCount(): Int {
            return itemData.size
        }

        fun setData(items : List<Contacts>) {
            itemData = items
            notifyDataSetChanged()
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
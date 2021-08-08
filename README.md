# Kotlin_ROOM
모바일 로컬 데이터베이스를 이용하여 데이터 저장 및 꺼내오는 Sample 예제

## ContactDatabase.kt

~~~kotlin

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

~~~

## Contacts.kt

~~~kotlin

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

~~~

## ContactDAO.kt

~~~kotlin

@Dao
interface ContactDAO {
    @Query("SELECT * FROM tb_contacts")
    fun getAll(): List<Contacts>

    @Insert
    fun insertAll(vararg contacts: Contacts)

    @Delete
    fun delete(contacts: Contacts)
}

~~~

## MainActivity.kt

~~~kotlin

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

~~~
package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.myapplication.DAO.UserDao
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.model.Users
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private lateinit var db: AppDatabase
lateinit var log: EditText
lateinit var pas: EditText
lateinit var but:TextView
private lateinit var userDatabase: AppDatabase

class Enter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)
        log = findViewById(R.id.Ent_log)
        pas = findViewById(R.id.Ent_pass)
        but = findViewById(R.id.register_click)
        but.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        userDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "Users"
        ).build()
    }


    fun GO_TO_MAIN(view: View) {



            if (log.text.isNotEmpty() && pas.text.isNotEmpty()) {
                val userLogin = log.text.toString()
                val userPassword = pas.text.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    val userDao = userDatabase.userDao()
                    val user = userDao.getUserByUsername(userLogin)
                    Log.d("User","${user}")
                    Log.d("userDao.getUserByUsername(userLogin)","${userDao.getUserByUsername(userLogin)}")
                    withContext(Dispatchers.Main) {
                        if (user==null||user.password != userPassword) {
                            showSnackbar("Invalid password or login")
                        } else {
                            showSnackbar("The entrance is completed")
                            val dbName = "${userLogin}_database"
                            val intent = Intent(this@Enter, Main::class.java)
                            intent.putExtra("DB_NAME", dbName)
                            startActivity(intent)

                        }
                    }
                }
            } else {
                showSnackbar("Обнаружены пустые поля")
            }

        }

        private fun showSnackbar(message: String) {
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
        }

}

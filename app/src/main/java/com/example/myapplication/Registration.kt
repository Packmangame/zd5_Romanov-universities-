package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.myapplication.DAO.UserDao
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.model.Users
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private lateinit var db: AppDatabase
lateinit var rEmail: EditText
lateinit var rName: EditText
lateinit var rLogin: EditText
lateinit var rPassword: EditText

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        db = AppDatabase.getDatabase(this)
        rEmail=findViewById(R.id.Reg_email)
        rName=findViewById(R.id.Reg_name)
        rLogin=findViewById(R.id.Reg_login)
        rPassword=findViewById(R.id.Reg_password)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "user_database"
        ).build()
    }

    fun GO_TO_Enter(view: View) {
        if(rEmail!!.text.isNotEmpty()|| rName!!.text.isNotEmpty()
            || rLogin!!.text.isNotEmpty()|| rPassword!!.text.isNotEmpty())
        {
            val userLogin = rLogin.text.toString()
            if (validateEmail(rEmail!!.text.toString()))
             {
                lifecycleScope.launch {

                        val user = Users(
                            email = rEmail!!.text.toString(),
                            name = rName!!.text.toString(),
                            login = rLogin!!.text.toString(),
                            password = rPassword!!.text.toString()
                        )
                        db.userDao().insert(user)

                        val alert = AlertDialog.Builder(this@Registration)
                            .setTitle("Accept")
                            .setMessage("User added")
                            .show()
                    Log.d("User","${user}")
                        val intent= Intent( this@Registration,Enter::class.java)
                        startActivity(intent)

                }
            } else {
                val alert = AlertDialog.Builder(this@Registration)
                    .setTitle("Error02")
                    .setMessage("Incorrect data in places Email or Password")
                    .show()
            }
        }else
        {
            val alert = AlertDialog.Builder(this@Registration)
                .setTitle("Error01")
                .setMessage("Places is null")
                .show()
        }
    }

    fun validateEmail(email:String):Boolean {
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return regex.matches(email)
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}
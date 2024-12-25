package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.Data.DataAdapter
import com.example.myapplication.API.ApiManager
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.model.University
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

private lateinit var recyclerView: RecyclerView
private lateinit var btnFetchFact: Button
private lateinit var editTextInput: EditText
private lateinit var userDatabase: AppDatabase
class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnFetchFact = findViewById(R.id.btnFetchFact)
        editTextInput = findViewById(R.id.editTextInput)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbName = intent.getStringExtra("DB_NAME") ?: "default_database"
        userDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            dbName
        ).build()

        btnFetchFact.setOnClickListener {
            val input = editTextInput.text.toString()
            fetchAndSaveData(this, input)
        }

        displaySavedData(this, recyclerView)
    }
    private fun fetchAndSaveData(context: Context, input: String) {
        val apiManager = ApiManager(context)

        apiManager.fetchDataFromApi(input,
            { data ->
                val jsonObject = JSONObject(data)
                val UnName = jsonObject.getString("name")
                val UnCountry = jsonObject.getJSONObject("country")
                val UnWeb=jsonObject.getJSONObject("web_pages")
                /* val imageUrl = sprites.getString("front_default")*/

                val abilitiesArray = jsonObject.getJSONArray("abilities")
                val abilitiesList = (0 until abilitiesArray.length()).map { index ->
                    val abilityObject = abilitiesArray.getJSONObject(index).getJSONObject("ability")
                    abilityObject.getString("name")
                }.toTypedArray()

                CoroutineScope(Dispatchers.IO).launch {

                    val existingCharacter = userDatabase.universDao().getCharacterByName(UnName)
                    if (existingCharacter == null) {
                        /*val imageBytes = fetchImageBytes(imageUrl)*/
                        val UNiversity = University(
                            UniverName = UnName,
                            UniverCountry = UnCountry.toString(),
                            UniverWeb = UnWeb.toString()
                            /* imageBytes = imageBytes*/
                        )
                        userDatabase.universDao().insertData(UNiversity)
                        withContext(Dispatchers.Main) {
                            displaySavedData(context, recyclerView)
                            showSnackbar("Книга успешно добавлена!")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            showSnackbar("Книга уже существует!")
                        }
                    }
                }
            },
            { error ->
                showSnackbar("Не удалось найти запрос. Попробуйте снова.")
            }
        )
    }


    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    /*    private fun fetchImageBytes(imageUrl: String): ByteArray? {
            return try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.inputStream.use { inputStream ->
                    return inputStream.readBytes()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }*/

    private fun displaySavedData(context: Context, recyclerView: RecyclerView) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataList = userDatabase.universDao().getAllData()

            withContext(Dispatchers.Main) {
                val adapter = DataAdapter(dataList) { selectedData ->
                    showOptionsDialog(selectedData)
                }
                recyclerView.adapter = adapter
            }
        }
    }

    private fun showOptionsDialog(data: University) {
        val options = arrayOf("Редактировать", "Удалить")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите действие")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> editData(data)
                1 -> deleteData(data)
            }
        }
        builder.show()
    }

    private fun deleteData(data: University) {
        CoroutineScope(Dispatchers.IO).launch {
            userDatabase.universDao().deleteData(data)
            withContext(Dispatchers.Main) {
                displaySavedData(this@Main, recyclerView)
            }
        }
    }

    private fun editData(data: University) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Редактировать имя")

        val input = EditText(this)
        input.setText(data.UniverName)
        builder.setView(input)

        builder.setPositiveButton("Сохранить") { dialog, _ ->
            val newName = input.text.toString()
            if (newName.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val updatedData = data.copy(UniverName = newName)
                    userDatabase.universDao().updateData(updatedData)

                    withContext(Dispatchers.Main) {
                        displaySavedData(this@Main, recyclerView)
                        showSnackbar("Имя обновлено!")
                    }
                }
            } else {
                showSnackbar("Имя не может быть пустым!")
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
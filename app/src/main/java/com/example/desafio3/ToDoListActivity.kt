package com.example.desafio3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONObject

class ToDoListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ToDoAdapter
    private val toDoList = mutableListOf<ToDo>()
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)


        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnAdd = findViewById<Button>(R.id.btnAddTodo)

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddToDoActivity::class.java))
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // — Agregar tarea
        findViewById<Button>(R.id.btnAddTodo).setOnClickListener {
            startActivity(Intent(this, AddToDoActivity::class.java))
        }

        recyclerView = findViewById(R.id.recyclerToDo)
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadToDos()

    }

    override fun onResume() {
        super.onResume()
        loadToDos()
    }

    private fun loadToDos() {
        ToDoService.getAllToDos { success, response ->
            runOnUiThread {
                if (success && response != null) {
                    val jsonArray = JSONArray(response)
                    toDoList.clear()

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        if (obj.getString("createdBy") == currentUserEmail) {
                            val todo = ToDo(
                                id = obj.getString("id"),
                                title = obj.getString("title"),
                                description = obj.getString("description"),
                                done = obj.getBoolean("done"),
                                createdAt = obj.getString("createdAt"),
                                createdBy = obj.getString("createdBy")
                            )
                            toDoList.add(todo)
                        }
                    }

                    adapter = ToDoAdapter(toDoList)
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this, "Error al cargar tareas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
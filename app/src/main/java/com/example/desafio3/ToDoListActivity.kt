package com.example.desafio3


import org.json.JSONObject
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray

class ToDoListActivity : AppCompatActivity(), ToDoAdapter.OnItemActionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ToDoAdapter
    private val toDoList = mutableListOf<ToDo>()
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)

        // 1️⃣ Logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 2️⃣ Agregar nueva tarea
        findViewById<Button>(R.id.btnAddTodo).setOnClickListener {
            startActivity(Intent(this, AddToDoActivity::class.java))
        }

        // 3️⃣ Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerToDo)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ToDoAdapter(toDoList, this)  // 'this' implementa OnItemActionListener
        recyclerView.adapter = adapter

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
                    toDoList.clear()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        if (obj.getString("createdBy") == currentUserEmail) {
                            toDoList.add(
                                ToDo(
                                    id = obj.getString("id"),
                                    title = obj.getString("title"),
                                    description = obj.getString("description"),
                                    done = obj.getBoolean("done"),
                                    createdAt = obj.getString("createdAt"),
                                    createdBy = obj.getString("createdBy")
                                )
                            )
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Error al cargar tareas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onEdit(todo: ToDo) {
        val intent = Intent(this, EditToDoActivity::class.java)
        intent.putExtra("TODO_ID", todo.id)
        startActivity(intent)
    }

    override fun onDelete(todo: ToDo) {
        ToDoService.deleteToDo(todo.id) { success, _ ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    loadToDos()
                } else {
                    Toast.makeText(this, "Error al eliminar tarea", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
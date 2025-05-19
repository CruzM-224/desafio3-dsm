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

class ToDoListActivity : AppCompatActivity(), ToDoAdapter.OnItemActionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter     : ToDoAdapter
    private val toDoList            = mutableListOf<ToDo>()
    private val currentUserEmail    = FirebaseAuth.getInstance().currentUser?.email ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        findViewById<Button>(R.id.btnAddTodo).setOnClickListener {
            startActivity(Intent(this, AddToDoActivity::class.java))
        }

        recyclerView = findViewById(R.id.recyclerToDo)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ToDoAdapter(toDoList, this)
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
                    val arr = JSONArray(response)
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        if (obj.getString("createdBy") == currentUserEmail) {
                            toDoList.add(
                                ToDo(
                                    id          = obj.getString("id"),
                                    title       = obj.getString("title"),
                                    description = obj.getString("description"),
                                    done        = obj.getBoolean("done"),
                                    createdAt   = obj.getString("createdAt"),
                                    createdBy   = obj.getString("createdBy")
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
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onToggleDone(todo: ToDo, isDone: Boolean) {

        val json = JSONObject().apply {
            put("title", todo.title)
            put("description", todo.description)
            put("done", isDone)
            put("createdAt", todo.createdAt)
            put("createdBy", todo.createdBy)
        }
        ToDoService.updateToDo(todo.id, json.toString()) { success, _ ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(
                        this,
                        if (isDone) "Marcada como completada" else "Marcada como pendiente",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadToDos()
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

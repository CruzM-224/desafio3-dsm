package com.example.desafio3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class EditToDoActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnUpdate: Button

    private lateinit var todoId: String
    private var originalCreatedAt: String = ""
    private var originalCreatedBy: String = ""
    private var originalDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        etTitle = findViewById(R.id.etEditTitle)
        etDescription = findViewById(R.id.etEditDescription)
        btnUpdate = findViewById(R.id.btnUpdateTodo)

        // 1️⃣ Leer ID que viene en el Intent
        todoId = intent.getStringExtra("TODO_ID") ?: run {
            finish() // no ID, salimos
            return
        }

        // 2️⃣ Obtener datos actuales para prellenar
        ToDoService.getToDoById(todoId) { success, response ->
            runOnUiThread {
                if (success && response != null) {
                    val obj = JSONObject(response)
                    etTitle.setText(obj.getString("title"))
                    etDescription.setText(obj.getString("description"))
                    // Guardar los campos originales
                    originalCreatedAt  = obj.getString("createdAt")
                    originalCreatedBy  = obj.getString("createdBy")
                    originalDone       = obj.getBoolean("done")
                } else {
                    Toast.makeText(this, "Error al cargar tarea", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        // 3️⃣ Listener para actualizar
        btnUpdate.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc  = etDescription.text.toString().trim()

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Construir JSON con los valores (se envía sin el "id")
            val json = JSONObject().apply {
                put("title", title)
                put("description", desc)
                put("done", originalDone)
                put("createdAt", originalCreatedAt)
                put("createdBy", originalCreatedBy)
            }

            // Llamada PUT
            ToDoService.updateToDo(todoId, json.toString()) { success, response ->
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                        finish()  // vuelve a la lista y recarga en onResume()
                    } else {
                        Toast.makeText(this, "Error al actualizar: $response", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
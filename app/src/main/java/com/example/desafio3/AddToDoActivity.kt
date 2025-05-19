package com.example.desafio3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AddToDoActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSaveTodo)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc  = etDescription.text.toString().trim()

            // Validación simple
            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Construir JSON
            val json = JSONObject().apply {
                put("title", title)
                put("description", desc)
                put("done", false)
                // Fecha en ISO
                val iso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    .format(Date())
                put("createdAt", iso)
                put("createdBy", currentUserEmail)
            }

            // Llamada POST
            ToDoService.createToDo(json.toString()) { success, response ->
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, "Tarea creada", Toast.LENGTH_SHORT).show()
                        // Volver a la lista
                        finish()
                    } else {
                        Toast.makeText(this, "Error: $response", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
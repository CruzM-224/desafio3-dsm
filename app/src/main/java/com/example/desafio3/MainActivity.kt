package com.example.desafio3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Ya tiene sesión → al listado de ToDos
            startActivity(Intent(this, ToDoListActivity::class.java))
        } else {
            // No hay sesión → a login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()  // Cerramos este “router”
    }
}
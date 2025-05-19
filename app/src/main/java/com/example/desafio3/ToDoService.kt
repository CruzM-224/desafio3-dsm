package com.example.desafio3
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object ToDoService {

    private val client = OkHttpClient()
    private const val BASE_URL = "https://68163b7232debfe95dbdd500.mockapi.io/academic/v1/to-do"

    fun getAllToDos(onResult: (Boolean, String?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful, response.body?.string())
            }
        })
    }

    fun getToDoById(id: String, onResult: (Boolean, String?) -> Unit) {
        val request = Request.Builder()
            .url("$BASE_URL/$id")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful, response.body?.string())
            }
        })
    }

    fun createToDo(jsonBody: String, onResult: (Boolean, String?) -> Unit) {
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(BASE_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful, response.body?.string())
            }
        })
    }

    fun updateToDo(id: String, jsonBody: String, onResult: (Boolean, String?) -> Unit) {
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/$id")
            .put(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful, response.body?.string())
            }
        })
    }

    fun deleteToDo(id: String, onResult: (Boolean, String?) -> Unit) {
        val request = Request.Builder()
            .url("$BASE_URL/$id")
            .delete()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful, response.body?.string())
            }
        })
    }
}
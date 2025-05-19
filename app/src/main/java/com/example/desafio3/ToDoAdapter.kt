package com.example.desafio3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(
    private val toDoList: List<ToDo>,
    private val listener: OnItemActionListener
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    interface OnItemActionListener {
        fun onEdit(todo: ToDo)
        fun onDelete(todo: ToDo)
    }

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title       : TextView      = itemView.findViewById(R.id.tvTitle)
        val description : TextView      = itemView.findViewById(R.id.tvDescription)
        val createdAt   : TextView      = itemView.findViewById(R.id.tvCreatedAt)
        val btnEdit     : ImageButton   = itemView.findViewById(R.id.btnEdit)
        val btnDelete   : ImageButton   = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = toDoList[position]
        holder.title.text       = todo.title
        holder.description.text = todo.description
        holder.createdAt.text   = "Creado el: ${todo.createdAt}"


        holder.btnEdit.setOnClickListener {
            listener.onEdit(todo)
        }

        holder.btnDelete.setOnClickListener {
            listener.onDelete(todo)
        }
    }

    override fun getItemCount(): Int = toDoList.size
}
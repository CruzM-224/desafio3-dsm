package com.example.desafio3

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
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
        fun onToggleDone(todo: ToDo, isDone: Boolean)
    }

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbDone      : CheckBox     = itemView.findViewById(R.id.cbDone)
        val title       : TextView     = itemView.findViewById(R.id.tvTitle)
        val description : TextView     = itemView.findViewById(R.id.tvDescription)
        val createdAt   : TextView     = itemView.findViewById(R.id.tvCreatedAt)
        val btnEdit     : ImageButton  = itemView.findViewById(R.id.btnEdit)
        val btnDelete   : ImageButton  = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = toDoList[position]

        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = todo.done

        if (todo.done) {
            holder.title.setTextColor(Color.GREEN)
            holder.description.setTextColor(Color.GREEN)
            holder.createdAt.setTextColor(Color.GREEN)
        } else {
            val defaultColor = holder.title.context
                .obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))
                .getColor(0, Color.BLACK)
            holder.title.setTextColor(defaultColor)
            holder.description.setTextColor(defaultColor)
            holder.createdAt.setTextColor(defaultColor)
        }

        holder.title.text       = todo.title
        holder.description.text = todo.description
        holder.createdAt.text   = "Creado el: ${todo.createdAt}"

        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            listener.onToggleDone(todo, isChecked)
        }

        holder.btnEdit.setOnClickListener {
            listener.onEdit(todo)
        }
        holder.btnDelete.setOnClickListener {
            listener.onDelete(todo)
        }
    }

    override fun getItemCount(): Int = toDoList.size
}
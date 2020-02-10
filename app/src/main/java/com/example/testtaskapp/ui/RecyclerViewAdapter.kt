package com.example.testtaskapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testtaskapp.R
import kotlinx.android.synthetic.main.item.view.*

class RecyclerViewAdapter(val onItemClicked: OnItemClicked) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0)
            holder.itemView.item.text = "Фото"
        if (position == 1)
            holder.itemView.item.text = "Аудио"
        holder.itemView.setOnClickListener { onItemClicked.onItemClicked(position) }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClicked {
        fun onItemClicked(index: Int)
    }
}
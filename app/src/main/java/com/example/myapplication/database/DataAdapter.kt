package com.example.myapplication.Data

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.University

class DataAdapter(
    private val dataList: List<University>,
    private val onItemLongClick: (University) -> Unit
) : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val inputTextView: TextView = view.findViewById(R.id.inputTextView)
        val dataTextView: TextView = view.findViewById(R.id.dataTextView)
        val imageView: ImageView = view.findViewById(R.id.sprite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_item, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        holder.inputTextView.text = data.UniverName
        holder.dataTextView.text = data.UniverCountry

        /*data.imageBytes?.let { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            Glide.with(holder.itemView.context)
                .load(bitmap)
                .into(holder.imageView)
        }*/

        holder.itemView.setOnLongClickListener {
            onItemLongClick(data)
            true
        }
    }

    override fun getItemCount(): Int = dataList.size
}

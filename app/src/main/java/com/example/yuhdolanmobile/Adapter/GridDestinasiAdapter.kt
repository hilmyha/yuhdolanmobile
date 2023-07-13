package com.example.yuhdolanmobile.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.yuhdolanmobile.R
import com.example.yuhdolanmobile.Response.Destinasi

class GridDestinasiAdapter(private val context: Context, private val dataList: ArrayList<Destinasi>): RecyclerView.Adapter<GridDestinasiAdapter.CategoryDestinasiViewHolder>() {
    class CategoryDestinasiViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val cvCategoryDestinasi = itemView.findViewById<CardView>(R.id.cv_category_destinasi_card)
        val tvDestinasiNama = itemView.findViewById<TextView>(R.id.tv_destinasi_card_title)
        val tvDestinasiLokasi = itemView.findViewById<TextView>(R.id.tv_destinasi_card_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryDestinasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_destinasi_card, parent, false)
        return CategoryDestinasiViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Destinasi>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CategoryDestinasiViewHolder, position: Int) {
        holder.tvDestinasiNama.text = dataList[position].nama
        holder.tvDestinasiLokasi.text = dataList[position].lokasi
        holder.cvCategoryDestinasi.setOnClickListener {
            Toast.makeText(context, dataList[position].nama, Toast.LENGTH_SHORT).show()
        }
    }

}
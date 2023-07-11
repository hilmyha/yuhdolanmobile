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

class DestinasiAdapter(private val context: Context, private val dataList: ArrayList<Destinasi>): RecyclerView.Adapter<DestinasiAdapter.DestinasiViewHolder>() {
    class DestinasiViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val cvDestinasi = itemView.findViewById<CardView>(R.id.cv_destinasi_card)
        val tvDestinasiNama = itemView.findViewById<TextView>(R.id.tv_destinasi_card_title)
        val tvDestinasiLokasi = itemView.findViewById<TextView>(R.id.tv_destinasi_card_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_destinasi_card, parent, false)
        return DestinasiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 3) {
            3
        } else {
            dataList.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Destinasi>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DestinasiViewHolder, position: Int) {
        holder.tvDestinasiNama.text = dataList[position].nama
        holder.tvDestinasiLokasi.text = dataList[position].lokasi
        holder.cvDestinasi.setOnClickListener {
            Toast.makeText(context, dataList[position].nama, Toast.LENGTH_SHORT).show()
        }
    }
}
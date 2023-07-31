package com.example.yuhdolanmobile.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yuhdolanmobile.R
import com.example.yuhdolanmobile.Response.Ulasan
import com.example.yuhdolanmobile.Response.User
import org.json.JSONArray
import org.json.JSONObject

class UlasanAdapter(private val context: Context, private val dataList: ArrayList<Ulasan>): RecyclerView.Adapter<UlasanAdapter.UlasanViewHolder>() {
    class UlasanViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvUlasanNama = itemView.findViewById<TextView>(R.id.tv_title_ulasan_card)
        val tvUlasanIsi = itemView.findViewById<TextView>(R.id.tv_isi_ulasan_card)
        val tvUlasanTanggal = itemView.findViewById<TextView>(R.id.tv_tanggal_ulasan_card)
        val tvPengulas = itemView.findViewById<TextView>(R.id.tv_nama_ulasan_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UlasanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ulasan_card, parent, false)
        return UlasanViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Ulasan>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UlasanViewHolder, position: Int) {
        val id = dataList[position].id
        holder.tvUlasanNama.text = dataList[position].judul
        val isi = dataList[position].isi
        // hilangkan tag html
        val isiTanpaTag = isi?.replace("<[^>]*>".toRegex(), "")
        holder.tvUlasanIsi.text = isiTanpaTag
        holder.tvUlasanTanggal.text = dataList[position].tanggal_berkunjung
        holder.tvPengulas.text = dataList[position].user.name
    }

    private fun User(id: Int, name: String, username: String): User {
        return User(id, name, username)
    }
}
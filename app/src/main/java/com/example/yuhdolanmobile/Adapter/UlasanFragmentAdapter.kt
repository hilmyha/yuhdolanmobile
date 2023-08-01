package com.example.yuhdolanmobile.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.R
import com.example.yuhdolanmobile.Response.Ulasan
import com.example.yuhdolanmobile.Response.UlasanDeleteResponse
import retrofit2.Callback

class UlasanFragmentAdapter(private val context: Context, private val dataList: ArrayList<Ulasan>): RecyclerView.Adapter<UlasanFragmentAdapter.UlasanViewHolder>() {
    class UlasanViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvUlasanNama = itemView.findViewById<TextView>(R.id.tv_title_ulasan_card)
        val tvUlasanIsi = itemView.findViewById<TextView>(R.id.tv_isi_ulasan_card)
        val tvUlasanTanggal = itemView.findViewById<TextView>(R.id.tv_tanggal_ulasan_card)
        val tvPengulas = itemView.findViewById<TextView>(R.id.tv_nama_ulasan_card)
        val btnDelete = itemView.findViewById<ImageView>(R.id.iv_delete_ulasan_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UlasanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ulasan_section_card, parent, false)
        return UlasanFragmentAdapter.UlasanViewHolder(view)
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
        holder.tvUlasanNama.text = dataList[position].judul
        val isi = dataList[position].isi
        // hilangkan tag html
        val isiTanpaTag = isi?.replace("<[^>]*>".toRegex(), "")
        holder.tvUlasanIsi.text = isiTanpaTag
        holder.tvUlasanTanggal.text = dataList[position].tanggal_berkunjung
        holder.tvPengulas.text = dataList[position].user.name

        holder.btnDelete.setOnClickListener {
            val id = dataList[position].id
            this.deleteUlasanById(id)
            Toast.makeText(context, "id: $id", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteUlasanById(id: Int) {

        val token = context.getSharedPreferences("Login", AppCompatActivity.MODE_PRIVATE).getString("Token", null)

        val apiClient = ApiClient
        val apiCall = apiClient.create("Bearer $token").deleteUlasan(id)

        apiCall.enqueue(object : Callback<UlasanDeleteResponse> {
            override fun onResponse(
                call: retrofit2.Call<UlasanDeleteResponse>,
                response: retrofit2.Response<UlasanDeleteResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Ulasan berhasil dihapus", Toast.LENGTH_SHORT).show()
                    // refresh fragment with swipe refresh layout

                } else {
                    Toast.makeText(context, "Ulasan gagal dihapus", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<UlasanDeleteResponse>, t: Throwable) {
                Toast.makeText(context, "Ulasan gagal dihapus", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
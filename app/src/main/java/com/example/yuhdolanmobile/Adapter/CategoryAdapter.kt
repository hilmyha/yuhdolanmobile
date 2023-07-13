package com.example.yuhdolanmobile.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.yuhdolanmobile.DestinasiByCategoryActivity
import com.example.yuhdolanmobile.R
import com.example.yuhdolanmobile.Response.Category
import com.example.yuhdolanmobile.Response.Destinasi
import org.w3c.dom.Text

class CategoryAdapter(private val context: Context, private val dataList: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val cvCategory = itemView.findViewById<CardView>(R.id.cv_category_card)
        val tvCategoryTitle = itemView.findViewById<TextView>(R.id.tv_title_category_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_card, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 6) {
            6
        } else {
            dataList.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Category>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.tvCategoryTitle.text = dataList[position].nama
        holder.cvCategory.setOnClickListener {
//            Toast.makeText(context, dataList[position].nama, Toast.LENGTH_SHORT).show()

            val id = dataList[position].id.toString()
            Intent(context, DestinasiByCategoryActivity::class.java).also {
                it.putExtra("category", id)
                it.putExtra("title", dataList[position].nama)
                context.startActivity(it)
            }
        }
    }
}

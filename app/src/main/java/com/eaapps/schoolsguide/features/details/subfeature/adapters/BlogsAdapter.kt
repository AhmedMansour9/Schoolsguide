package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.BlogItemBinding

class BlogAdapter(private val dataList: List<SchoolResponse.SchoolData.DataSchool.ShareData>,private val onSelectedItem:(SchoolResponse.SchoolData.DataSchool.ShareData)->Unit) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {


    inner class BlogViewHolder(private val binding: BlogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(position: Int) {
            val data = dataList[position]
            binding.blog = data
            binding.executePendingBindings()
            itemView.setOnClickListener {
                onSelectedItem(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder =
        BlogViewHolder(BlogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
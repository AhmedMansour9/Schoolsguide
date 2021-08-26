package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.PhotoItemBinding

class PhotosAdapter(private val dataList: List<SchoolResponse.SchoolData.DataSchool.Gallary>,private val onPhotoSelect:(SchoolResponse.SchoolData.DataSchool.Gallary)->Unit) :
    RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(private val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = dataList[position]
            binding.gallary = data
            binding.executePendingBindings()
            itemView.setOnClickListener {
                onPhotoSelect(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(
            PhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
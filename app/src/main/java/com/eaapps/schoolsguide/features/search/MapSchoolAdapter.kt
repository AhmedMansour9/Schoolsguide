package com.eaapps.schoolsguide.features.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.SchoolMapItemBinding

class MapSchoolAdapter(private val dataList: List<SchoolResponse.SchoolData.DataSchool>) :
    RecyclerView.Adapter<MapSchoolAdapter.MapViewHolder>() {


    inner class MapViewHolder(private val binding: SchoolMapItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = dataList[position]
            binding.dataSchool = data
            binding.rate.rating = data.total_review.toFloat()
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder =
        MapViewHolder(
            SchoolMapItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
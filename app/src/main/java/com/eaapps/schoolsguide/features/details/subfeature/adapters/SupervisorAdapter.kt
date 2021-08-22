package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.SupervisorItemBinding

class SupervisorAdapter(private val dataList: List<SchoolResponse.SchoolData.DataSchool.CommunicateData>) :
    RecyclerView.Adapter<SupervisorAdapter.SupervisorViewHolder>() {


    inner class SupervisorViewHolder(private val binding: SupervisorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(position: Int) {
            val data = dataList[position]
            binding.communicateData = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupervisorViewHolder =
        SupervisorViewHolder(
            SupervisorItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SupervisorViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.AwardsItemBinding

class AwardsAdapter(private val dataList: List<SchoolResponse.SchoolData.DataSchool.ShareData>) :
    RecyclerView.Adapter<AwardsAdapter.AwardsViewHolder>() {

    inner class AwardsViewHolder(private val binding: AwardsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(position: Int) {
            val data = dataList[position]
            binding.awardsData = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardsViewHolder =
        AwardsViewHolder(
            AwardsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AwardsViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
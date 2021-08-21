package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.databinding.SubItemAdapterBinding
import com.eaapps.schoolsguide.domain.model.SubModel

class SubAdapter(private val dataList: List<SubModel>) :
    RecyclerView.Adapter<SubAdapter.SubViewHolder>() {


    inner class SubViewHolder(private val binding: SubItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = dataList[position]
            binding.subModel = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder =
        SubViewHolder(
            SubItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
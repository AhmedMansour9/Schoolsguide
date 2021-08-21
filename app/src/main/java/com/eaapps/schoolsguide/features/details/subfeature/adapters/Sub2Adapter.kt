package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.databinding.SubItem2AdapterBinding
import com.eaapps.schoolsguide.domain.model.SubModel

class Sub2Adapter(private val dataList: List<SubModel>) :
    RecyclerView.Adapter<Sub2Adapter.SubViewHolder>() {


    inner class SubViewHolder(private val binding: SubItem2AdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = dataList[position]
            binding.subModel = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder =
        SubViewHolder(
            SubItem2AdapterBinding.inflate(
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
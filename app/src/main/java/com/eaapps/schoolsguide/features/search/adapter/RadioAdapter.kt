package com.eaapps.schoolsguide.features.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.databinding.RadioAdapterItemBinding

class RadioAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<RadioAdapter.RadioViewHolder>() {


    inner class RadioViewHolder(private val binding: RadioAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.rd.text = list[position]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioViewHolder =
        RadioViewHolder(
            RadioAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RadioViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int = list.size


}
package com.eaapps.schoolsguide.features.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.databinding.DetailsItemNavigationsBinding
import com.eaapps.schoolsguide.domain.model.NavigationPropertiesModel

class PropertiesAdapter(
    private val dataList: List<NavigationPropertiesModel> = arrayListOf(),
    private val onItemSelected: (NavigationPropertiesModel) -> Unit
) : RecyclerView.Adapter<PropertiesAdapter.PropertiesViewHolder>() {


    inner class PropertiesViewHolder(private val binding: DetailsItemNavigationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val propertiesModel = dataList[position]
            binding.propertiesModel = propertiesModel
            binding.executePendingBindings()
            itemView.setOnClickListener {
                onItemSelected(propertiesModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesViewHolder =
        PropertiesViewHolder(
            DetailsItemNavigationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PropertiesViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int = dataList.size
}
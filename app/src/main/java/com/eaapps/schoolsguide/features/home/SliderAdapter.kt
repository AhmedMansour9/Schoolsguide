package com.eaapps.schoolsguide.features.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SliderResponse
import com.eaapps.schoolsguide.databinding.SliderItemBinding

class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private val sliderListData = ArrayList<SliderResponse.SliderData>()

    fun setData(data: List<SliderResponse.SliderData>) {
        sliderListData.clear()
        sliderListData.addAll(data)
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(private val sliderItemBinding: SliderItemBinding) :
        RecyclerView.ViewHolder(sliderItemBinding.root) {


        fun bind(position: Int) {
            val data = sliderListData[position]
            sliderItemBinding.sliderData = data
            sliderItemBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder =
        SliderViewHolder(
            SliderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = sliderListData.size
}
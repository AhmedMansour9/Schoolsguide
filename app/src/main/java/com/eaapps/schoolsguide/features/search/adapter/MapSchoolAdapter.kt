package com.eaapps.schoolsguide.features.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.SchoolMapItemBinding

class MapSchoolAdapter(
    private var dataList: ArrayList<SchoolResponse.SchoolData.DataSchool> = ArrayList(),
    private val onSelectItem:(SchoolResponse.SchoolData.DataSchool)->Unit,
    private val onToggleFavorite: (Int, Int) -> Unit
) :
    RecyclerView.Adapter<MapSchoolAdapter.MapViewHolder>() {

    inner class MapViewHolder(private val binding: SchoolMapItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = dataList[position]
            binding.dataSchool = data
            binding.rate.rating = data.total_review.toFloat()
            binding.executePendingBindings()
            binding.favBox.setOnCheckedChangeListener { buttonView, _ ->
                if (buttonView.isPressed) {
                    onToggleFavorite(data.id, position)
                }
            }
            itemView.setOnClickListener {
                onSelectItem(data)
            }
        }
    }

    fun setData(listData: List<SchoolResponse.SchoolData.DataSchool>) {
        dataList = ArrayList(listData)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int) {
        val data = dataList[position]
        data.isFavoired = !data.isFavoired
        dataList[position] = data
        notifyItemChanged(position)
    }

    fun indexSchool(dataSchool: SchoolResponse.SchoolData.DataSchool): Int {
        if (dataList.isNotEmpty())
            return dataList.indexOf(dataSchool)
        return -1
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
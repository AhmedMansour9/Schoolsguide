package com.eaapps.schoolsguide.features.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.SchoolItemVerticalBinding

class FavoriteAdapter(private val toggleFavorite: (Int, Int, List<SchoolResponse.SchoolData.DataSchool>) -> Unit) :
    PagingDataAdapter<SchoolResponse.SchoolData.DataSchool, FavoriteAdapter.FavoriteViewHolder>(
        object :
            DiffUtil.ItemCallback<SchoolResponse.SchoolData.DataSchool>() {
            override fun areItemsTheSame(
                oldItem: SchoolResponse.SchoolData.DataSchool,
                newItem: SchoolResponse.SchoolData.DataSchool
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SchoolResponse.SchoolData.DataSchool,
                newItem: SchoolResponse.SchoolData.DataSchool
            ): Boolean = oldItem == newItem

        }) {

    fun removeItem(position: Int) {
        val schoolData = getItem(position)
        toggleFavorite(schoolData?.id!!, position, snapshot().items)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder =
        FavoriteViewHolder(
            SchoolItemVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class FavoriteViewHolder(private val schoolItemVerticalBinding: SchoolItemVerticalBinding) :
        RecyclerView.ViewHolder(schoolItemVerticalBinding.root) {


        fun bind(position: Int) {
            val schoolData = getItem(position)
            schoolItemVerticalBinding.dataSchool = schoolData
            schoolItemVerticalBinding.executePendingBindings()
            itemView.setOnClickListener {
                notifyDataSetChanged()

            }
            schoolItemVerticalBinding.favBox.setOnCheckedChangeListener { buttonView, _ ->
                if (buttonView.isPressed) {
                    removeItem(position)
                }
            }
        }
    }
}

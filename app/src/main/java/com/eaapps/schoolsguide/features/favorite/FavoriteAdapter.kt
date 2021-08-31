package com.eaapps.schoolsguide.features.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.SchoolItemVerticalBinding

class FavoriteAdapter( private val onShareSchool: (Int) -> Unit,private val onToggleFavorite: (Int) -> Unit) :
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

            schoolItemVerticalBinding.favBox.setOnCheckedChangeListener { buttonView, _ ->
                if (buttonView.isPressed) {
                    onToggleFavorite(schoolData?.id!!)
                }
            }

            schoolItemVerticalBinding.share.setOnClickListener {
                onShareSchool(schoolData?.id!!)
            }
        }
    }
}

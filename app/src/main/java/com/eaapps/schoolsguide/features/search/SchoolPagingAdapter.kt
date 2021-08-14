package com.eaapps.schoolsguide.features.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.SchoolItemGridBinding
import com.eaapps.schoolsguide.databinding.SchoolItemHomeBinding
import com.eaapps.schoolsguide.databinding.SchoolItemVerticalBinding

class SchoolPagingAdapter(
    private var listMode: Boolean = true,
    private val toggleFavorite: (Int) -> Unit
) :
    PagingDataAdapter<SchoolResponse.SchoolData.DataSchool, RecyclerView.ViewHolder>(
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

    override fun getItemViewType(position: Int): Int {
        return if (listMode) 1 else 0
    }

    fun changeMode(listMode: Boolean){
        this.listMode = listMode
    }

    inner class SchoolVerticalViewHolder(private val schoolItemVerticalBinding: SchoolItemVerticalBinding) :
        RecyclerView.ViewHolder(schoolItemVerticalBinding.root) {
        fun bind(position: Int) {
            val schoolData = getItem(position)
            schoolItemVerticalBinding.dataSchool = schoolData
            schoolItemVerticalBinding.executePendingBindings()
            schoolItemVerticalBinding.favBox.setOnCheckedChangeListener { buttonView, isChecked ->
//                if (buttonView.isPressed) {
//                }
            }
        }
    }

    inner class SchoolGridViewHolder(private val schoolItemGridBinding: SchoolItemGridBinding) :
        RecyclerView.ViewHolder(schoolItemGridBinding.root) {

        fun bind(position: Int) {
            val schoolData = getItem(position)
            schoolItemGridBinding.dataSchool = schoolData
            schoolItemGridBinding.executePendingBindings()
            schoolItemGridBinding.favBox.setOnCheckedChangeListener { buttonView, isChecked ->
//                if (buttonView.isPressed) {
//                }
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listMode)
            (holder as SchoolVerticalViewHolder).bind(position)
        else
            (holder as SchoolGridViewHolder).bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1)
            return SchoolVerticalViewHolder(
                SchoolItemVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        else
            return SchoolGridViewHolder(
                SchoolItemGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }
}
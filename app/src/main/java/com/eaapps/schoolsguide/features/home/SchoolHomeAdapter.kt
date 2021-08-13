package com.eaapps.schoolsguide.features.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.SchoolItemHomeBinding

class SchoolHomeAdapter(val toggleFavorite: (Int) -> Unit) :
    RecyclerView.Adapter<SchoolHomeAdapter.SchoolViewHolder>() {

    private val dataSchoolList = ArrayList<SchoolResponse.SchoolData.DataSchool>()

    fun setData(data: List<SchoolResponse.SchoolData.DataSchool>) {
        dataSchoolList.clear()
        dataSchoolList.addAll(data)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, checked: Boolean) {
        val schoolData: SchoolResponse.SchoolData.DataSchool = dataSchoolList[position]
        schoolData.isFavoired = checked
        dataSchoolList[position] = schoolData
        toggleFavorite(schoolData.id)
        notifyItemChanged(position)
    }

    inner class SchoolViewHolder(private val schoolItemHomeBinding: SchoolItemHomeBinding) :
        RecyclerView.ViewHolder(schoolItemHomeBinding.root) {

        fun bind(position: Int) {
            val schoolData: SchoolResponse.SchoolData.DataSchool = dataSchoolList[position]
            schoolItemHomeBinding.dataSchool = schoolData
            schoolItemHomeBinding.executePendingBindings()

            schoolItemHomeBinding.favBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    updateItem(position, isChecked)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder =
        SchoolViewHolder(
            SchoolItemHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = dataSchoolList.size
}
package com.eaapps.schoolsguide.features.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.TypeResponse
import com.eaapps.schoolsguide.databinding.ItemSchoolTypeBinding

class SchoolTypeAdapter(private val type:(TypeResponse.TypeData)->Unit) : RecyclerView.Adapter<SchoolTypeAdapter.SchoolTypeViewHolder>() {

    private val schoolTypeListData = ArrayList<TypeResponse.TypeData>()

    fun setData(data: List<TypeResponse.TypeData>) {
        schoolTypeListData.clear()
        schoolTypeListData.addAll(data)
        notifyDataSetChanged()
    }

    inner class SchoolTypeViewHolder(private val schoolTypeBinding: ItemSchoolTypeBinding) :
        RecyclerView.ViewHolder(schoolTypeBinding.root) {

        fun bind(position: Int) {
            val typeData = schoolTypeListData[position]
            schoolTypeBinding.typeData = typeData
            schoolTypeBinding.executePendingBindings()
            itemView.setOnClickListener {
                type(typeData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolTypeViewHolder =
        SchoolTypeViewHolder(
            ItemSchoolTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SchoolTypeViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int = schoolTypeListData.size
}
package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.JobItemBinding

class JobsAdapter(
    private val dataList: List<SchoolResponse.SchoolData.DataSchool.JobData>,
    private val onShareJob: (SchoolResponse.SchoolData.DataSchool.JobData) -> Unit,
    private val onApplyJob: (SchoolResponse.SchoolData.DataSchool.JobData) -> Unit
) :
    RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {


    inner class JobsViewHolder(private val binding: JobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(position: Int) {
            val data = dataList[position]
            binding.jobData = data
            binding.executePendingBindings()
            itemView.setOnClickListener {
                onShareJob(data)
            }
            binding.applyBtn.setOnClickListener {
                onApplyJob(data)
            }

            binding.shareBtn.setOnClickListener {
                onShareJob(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder =
        JobsViewHolder(JobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
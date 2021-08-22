package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.ReviewItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat

class ReviewParentsAdapter(private val dataList: List<SchoolResponse.SchoolData.DataSchool.SchoolReview>) :
    RecyclerView.Adapter<ReviewParentsAdapter.ReviewViewHolder>() {


    inner class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(position: Int) {
            val data = dataList[position]
            binding.schoolReview = data
            binding.dataComment.apply {
                val simpleDate = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
                try {
                    val date = simpleDate.parse(data.created_at)
                    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val milliseconds =
                        date.time
                    val dateComment = DateUtils.getRelativeTimeSpanString(
                        milliseconds,
                        System.currentTimeMillis(),
                        0L,
                        DateUtils.FORMAT_ABBREV_RELATIVE
                    )
                    text = dateComment
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder =
        ReviewViewHolder(
            ReviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
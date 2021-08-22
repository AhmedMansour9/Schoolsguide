package com.eaapps.schoolsguide.features.details.subfeature.adapters

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.NotificationItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat

class NotificationsAdapter(private val dataList: List<SchoolResponse.SchoolData.DataSchool.ShareData>) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {


    inner class NotificationsViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(position: Int) {
            val data = dataList[position]
            binding.notificationData = data
            data.created_at?.apply {
                binding.timeNotify.apply {
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
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder =
        NotificationsViewHolder(
            NotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}
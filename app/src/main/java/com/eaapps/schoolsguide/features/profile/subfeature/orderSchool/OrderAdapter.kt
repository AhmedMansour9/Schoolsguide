package com.eaapps.schoolsguide.features.profile.subfeature.orderSchool

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.data.entity.SchoolBookingRequestsResponse
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.OrderItemBinding

class OrderAdapter(
    private val dataList: ArrayList<SchoolBookingRequestsResponse.RequestData> = ArrayList(),
    private val navigateItem: (SchoolResponse.SchoolData.DataSchool) -> Unit
) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {


    fun setData(data: List<SchoolBookingRequestsResponse.RequestData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(position: Int) {
            val data = dataList[position]
            binding.orderSchool = data
            binding.executePendingBindings()

            binding.viewDetails.setOnClickListener {
                navigateItem(data.data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder =
        OrderViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int =
        dataList.size

}

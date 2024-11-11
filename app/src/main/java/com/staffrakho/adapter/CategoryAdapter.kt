package com.staffrakho.adapter

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.staffrakho.R
import com.staffrakho.dataModel.PopularCategoryList
import com.staffrakho.databinding.ItemCategoriesBinding
import com.staffrakho.utility.AppConstant


class CategoryAdapter(
    private var notificationList: ArrayList<PopularCategoryList>,
    var requireContext: Activity,
) : RecyclerView.Adapter<CategoryAdapter.Holder>() {

    class Holder(val binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: PopularCategoryList, requireContext: Activity) {
            binding.tvCategoryName.text = dataItem.name
            binding.tvTotJob.text = dataItem.jobs_count


            Glide.with(requireContext)
                .load(AppConstant.MEDIA_BASE_URL + dataItem.image)
                .placeholder(R.drawable.job_icon)
                .into(binding.tvImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataItem = notificationList[position]
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstant.CATEGORY_ID, dataItem.id.toString())
            bundle.putString(AppConstant.CITY_ID, "")
            bundle.putString(AppConstant.STATE_ID, "")
            bundle.putString(AppConstant.JOB_TYPE, "")
            bundle.putString(AppConstant.SUB_CATEGORY_ID, "")
            Navigation.findNavController(holder.itemView).navigate(R.id.openSearchJobScreen, bundle)
        }
        holder.bind(dataItem, requireContext)
    }
}
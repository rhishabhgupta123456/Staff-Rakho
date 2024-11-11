package com.staffrakho.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.staffrakho.dataModel.OnBoardingModel
import com.staffrakho.databinding.ItemOnbaordingBinding

class OnBoardingAdapter(private var dataList: ArrayList<OnBoardingModel>) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemOnbaordingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(textList: OnBoardingModel) {
            binding.tvItemDescription.text = textList.itemDescription
            binding.tvTitle.text = textList.itemTitle
            binding.ivItem.setImageResource(textList.itemImage)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
      val binding =  ItemOnbaordingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() : Int {
        return 3
    }
}



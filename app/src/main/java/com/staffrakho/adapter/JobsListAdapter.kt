package com.staffrakho.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.dataModel.FamilyDataModel
import com.staffrakho.dataModel.JobList
import com.staffrakho.dataModel.MyJobs
import com.staffrakho.databinding.ItemAppliedJobBinding
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.SessionManager
import kotlin.random.Random

class JobsListAdapter(
    private var dataList: ArrayList<JobList>,
    var requireContext: Context,
) : RecyclerView.Adapter<JobsListAdapter.Holder>() {

    interface OnRequestAction {
        fun viewJobs(item: JobList)
        fun savedJob(item: JobList)
        fun unSavedJob(item: JobList)
    }

    private var listener: OnRequestAction? = null

    fun setOnRequestAction(listener: OnRequestAction) {
        this.listener = listener
    }


    class Holder(val binding: ItemAppliedJobBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(dataItem: JobList, requireContext: Context) {

            binding.tvJobTitle.text = dataItem.roles
            binding.tvCompanyName.text = dataItem.company_name
            binding.tvLoaction.text = dataItem.address
            binding.tvSalary.text = dataItem.salary_label
            binding.jobType.text = dataItem.type_label


            if (dataItem.is_favorite == 1) {
                binding.btBookmarkJob.setImageResource(R.drawable.bookmark_select_icon)
            } else {
                binding.btBookmarkJob.setImageResource(R.drawable.save_job_icon)
            }


            if (dataItem.type_label != null) {

                if (dataItem.type_label.lowercase() == "full-time") {
                    binding.jobType.setBackgroundResource(R.drawable.btdark_blue_bg)
                    binding.jobType.setTextColor(requireContext.resources.getColor(R.color.white))
                }

                if (dataItem.type_label.lowercase() == "part-time") {
                    binding.jobType.setBackgroundResource(R.drawable.btdark_yellow_bg)
                    binding.jobType.setTextColor(requireContext.resources.getColor(R.color.blackTextColor))
                }
                if (dataItem.type_label.lowercase() == "work from home") {
                    binding.jobType.setBackgroundResource(R.drawable.btdark_orange_bg)
                    binding.jobType.setTextColor(requireContext.resources.getColor(R.color.white))
                }
            }

            if (dataItem.company_logo == null || dataItem.company_logo == "") {
                binding.tvLogo.visibility = View.GONE
                binding.tvDefalutImage.visibility = View.VISIBLE

                if (dataItem.company_name != null) {
                    if (dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                            .joinToString("") { it[0].uppercase() }.length >= 2) {
                        binding.tvDefalutImage.text =
                            dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                                .joinToString("") { it[0].uppercase() }.substring(0, 2)
                    } else {
                        binding.tvDefalutImage.text =
                            dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                                .joinToString("") { it[0].uppercase() }
                    }
                } else {
                    binding.tvDefalutImage.text = ""
                }

                when (Random.nextInt(1, 11)) {
                    1 -> {
                        binding.tvDefalutImage.setBackgroundColor(
                            requireContext.resources.getColor(
                                R.color.bgColor1
                            )
                        )
                        binding.tvDefalutImage.setTextColor(requireContext.resources.getColor(R.color.textColor1))
                    }

                    2 -> {
                        binding.tvDefalutImage.setBackgroundColor(
                            requireContext.resources.getColor(
                                R.color.bgColor2
                            )
                        )
                        binding.tvDefalutImage.setTextColor(requireContext.resources.getColor(R.color.textColor2))
                    }

                    3 -> {
                        binding.tvDefalutImage.setBackgroundColor(
                            requireContext.resources.getColor(
                                R.color.bgColor3
                            )
                        )
                        binding.tvDefalutImage.setTextColor(requireContext.resources.getColor(R.color.textColor3))
                    }

                    4 -> {
                        binding.tvDefalutImage.setBackgroundColor(
                            requireContext.resources.getColor(
                                R.color.bgColor4
                            )
                        )
                        binding.tvDefalutImage.setTextColor(requireContext.resources.getColor(R.color.textColor4))
                    }

                    5 -> {
                        binding.tvDefalutImage.setBackgroundColor(
                            requireContext.resources.getColor(
                                R.color.bgColor5
                            )
                        )
                        binding.tvDefalutImage.setTextColor(requireContext.resources.getColor(R.color.textColor5))
                    }

                    6 -> {
                        binding.tvDefalutImage.setBackgroundColor(
                            requireContext.resources.getColor(
                                R.color.bgColor6
                            )
                        )
                        binding.tvDefalutImage.setTextColor(requireContext.resources.getColor(R.color.textColor6))
                    }

                    7 -> {
                        binding.tvDefalutImage.setBackgroundColor(
                            requireContext.resources.getColor(
                                R.color.bgColor7
                            )
                        )
                        binding.tvDefalutImage.setTextColor(requireContext.resources.getColor(R.color.textColor7))
                    }

                    else -> {
                        binding.tvDefalutImage.setBackgroundColor(
                            requireContext.resources.getColor(
                                R.color.bgColor8
                            )
                        )
                        binding.tvDefalutImage.setTextColor(requireContext.resources.getColor(R.color.textColor8))
                    }
                }

            } else {
                binding.tvLogo.visibility = View.VISIBLE
                binding.tvDefalutImage.visibility = View.GONE

                Glide.with(requireContext).load(AppConstant.MEDIA_BASE_URL + dataItem.company_logo)
                    .into(binding.tvLogo)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemAppliedJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataItem = dataList[position]
        holder.bind(dataItem, requireContext)

        holder.binding.btOpenProfile.setOnClickListener {
            listener?.viewJobs(dataItem)
        }

        holder.binding.btBookmarkJob.setOnClickListener() {
            if (dataList[position].is_favorite == 1) {
                dataList[position].is_favorite = 0
                listener?.unSavedJob(dataList[position])
                holder.binding.btBookmarkJob.setImageResource(R.drawable.save_job_icon)
            } else {
                dataList[position].is_favorite = 1
                listener?.savedJob(dataList[position])
                holder.binding.btBookmarkJob.setImageResource(R.drawable.bookmark_select_icon)
            }
        }

    }

    fun addItems(newItems: ArrayList<JobList>) {
        val startPosition = dataList.size
        dataList.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun updateAllList(newItems: ArrayList<JobList>) {
        dataList = newItems
        notifyDataSetChanged()
    }

}
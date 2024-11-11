package com.staffrakho.adapter

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.dataModel.UserData
import com.staffrakho.databinding.ItemApplicantsBinding
import com.staffrakho.databinding.ItemUserProfileBinding
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.SessionManager
import kotlin.random.Random


class ApplicantsAdapter(
    private var dataList: ArrayList<UserData>,
    var requireContext: Activity,
    private var sourceFragment: String,
) : RecyclerView.Adapter<ApplicantsAdapter.Holder>() {

    val sessionManager = SessionManager(requireContext)

    class Holder(val binding: ItemApplicantsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: UserData, requireContext: Activity) {
            binding.tvUserName.text = dataItem.name
            binding.tvAppliedJob.text = dataItem.job_title
            if (dataItem.avatar == null || dataItem.avatar == "") {
                binding.tvLogo.visibility = View.GONE
                binding.tvDefalutImage.visibility = View.VISIBLE

                if (dataItem.name != null) {
                    if (dataItem.name.split(" ").filter { it.isNotEmpty() }
                            .joinToString("") { it[0].uppercase() }.length >= 2) {
                        binding.tvDefalutImage.text =
                            dataItem.name.split(" ").filter { it.isNotEmpty() }
                                .joinToString("") { it[0].uppercase() }.substring(0, 2)
                    } else {
                        binding.tvDefalutImage.text =
                            dataItem.name.split(" ").filter { it.isNotEmpty() }
                                .joinToString("") { it[0].uppercase() }
                    }
                } else {
                    binding.tvDefalutImage.text = null
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

                Glide.with(requireContext).load(AppConstant.MEDIA_BASE_URL + dataItem.avatar)
                    .into(binding.tvLogo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemApplicantsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataItem = dataList[position]
        holder.itemView.setOnClickListener {
            sessionManager.setSourceFragment(sourceFragment)
            val bundle = Bundle()
            bundle.putString(AppConstant.USER_DETAIL, Gson().toJson(dataItem))
            Navigation.findNavController(holder.itemView).navigate(R.id.openUserProfileViewScreen, bundle)
        }
        holder.bind(dataItem, requireContext)
    }

    // This Function is used for Add New Items
    fun addItems(newItems: ArrayList<UserData>) {
        val startPosition = dataList.size
        dataList.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)

    }
}
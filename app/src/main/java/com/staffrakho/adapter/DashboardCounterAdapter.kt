package com.staffrakho.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.staffrakho.R
import com.staffrakho.dataModel.Counters
import com.staffrakho.databinding.ItemDashboardCounterBinding


class DashboardCounterAdapter(
    private var notificationList: ArrayList<Counters>,
    var requireContext: Activity,
) : RecyclerView.Adapter<DashboardCounterAdapter.Holder>() {

    class Holder(val binding: ItemDashboardCounterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: Counters) {
            binding.tvCount.text = dataItem.count
            binding.tvLable.text = dataItem.label
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemDashboardCounterBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return Holder(binding)

    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataItem = notificationList[position]
        holder.bind(dataItem)

        holder.itemView.setOnClickListener() {
            if (notificationList[position].key == "applied_jobs") {
                Navigation.findNavController(holder.itemView).navigate(R.id.openAppliedJobScreen)
            }

            if (notificationList[position].key == "job_posted") {
                Navigation.findNavController(holder.itemView).navigate(R.id.openPostJobScreen)
            }

            if (notificationList[position].key == "application_received") {
                Navigation.findNavController(holder.itemView).navigate(R.id.openPostJobScreen)
            }

            if (notificationList[position].key == "visited_profiles") {
                Navigation.findNavController(holder.itemView).navigate(R.id.openVisitedProfileFragment)
            }

            if (notificationList[position].key == "new_profiles") {
                Navigation.findNavController(holder.itemView).navigate(R.id.openNewProfileFragment)
            }

            if (notificationList[position].key == "shortlisted_profiles") {
                Navigation.findNavController(holder.itemView).navigate(R.id.openShortListedProfileFragment)
            }


        }

    }
}
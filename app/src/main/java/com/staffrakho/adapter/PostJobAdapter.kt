package com.staffrakho.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.staffrakho.R
import com.staffrakho.dataModel.MyJobs
import com.staffrakho.databinding.ItemPostJobBinding


class PostJobAdapter(
    private var dataList: ArrayList<MyJobs>,
    var requireContext: Activity,
) : RecyclerView.Adapter<PostJobAdapter.Holder>() {

    interface OnRequestAction {
        fun editItem(item: MyJobs)
        fun deleteItem(item: MyJobs)
        fun viewItem(item: MyJobs)
        fun changeJobState(item: MyJobs)
        fun viewApplicantsItem(item: MyJobs)
    }

    private var listener: OnRequestAction? = null

    fun setOnRequestAction(listener: OnRequestAction) {
        this.listener = listener
    }

    class Holder(val binding: ItemPostJobBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(dataItem: MyJobs, requireContext: Activity) {
            binding.jobTitle.text = dataItem.title
            binding.jobPosted.text = dataItem.created_at
            binding.tvTotApplicants.text = dataItem.applicant_count +" "+ requireContext.getString(R.string.Applicants)


/*            if (dataItem.status == "0") {
                binding.jobStatus.text = requireContext.getString(R.string.inactive)
                binding.jobStatus.setTextColor(requireContext.resources.getColor(R.color.redTextColor))
            } else {
                binding.jobStatus.text = requireContext.getString(R.string.active)
                binding.jobStatus.setTextColor(requireContext.resources.getColor(R.color.greenTextColor))
            }*/
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemPostJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataItem = dataList[position]

        holder.binding.btMenu.setOnClickListener {
            openMenu(holder.binding.btMenu, dataItem)
        }

        holder.binding.switchActive.isChecked = dataItem.status != "0"

        holder.binding.switchActive.setOnCheckedChangeListener { _, isChecked ->
            listener?.changeJobState(dataItem)
        }
        holder.bind(dataItem, requireContext)
    }

    // This Function is used for open Popup Menu
    private fun openMenu(item: ImageView, dataItem: MyJobs) {
        val popupMenu = PopupMenu(requireContext, item)
        popupMenu.menuInflater.inflate(R.menu.job_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
            when (menuItem.itemId) {

                R.id.btView -> {
                    listener?.viewItem(dataItem)
                }

              /*  R.id.btViewApplicants -> {
                    listener?.viewApplicantsItem(dataItem)
                }
*/
                R.id.btEdit -> {
                    listener?.editItem(dataItem)
                }

                R.id.btDelete -> {
                    listener?.deleteItem(dataItem)
                }

            }
            true
        }


        // Showing the popup menu
        popupMenu.show()
    }

}
package com.staffrakho.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.staffrakho.R
import com.staffrakho.dataModel.FamilyDataModel
import com.staffrakho.databinding.ItemFamillyBackgroundBinding

class FamilyRelationAdapter(
    private var dataList: ArrayList<FamilyDataModel>,
    var requireContext: Context,
    private var display : Int,
) : RecyclerView.Adapter<FamilyRelationAdapter.Holder>() {

    interface OnRequestAction {
        fun editItem(item: FamilyDataModel)
        fun deleteItem(item: FamilyDataModel)
    }

    private var listener: OnRequestAction? = null

    fun setOnRequestAction(listener: OnRequestAction) {
        this.listener = listener
    }

    class Holder(val binding: ItemFamillyBackgroundBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(dataItem: FamilyDataModel) {
            binding.tvName.text = dataItem.name
            binding.tvRelation.text = dataItem.relation_name
            binding.tvLivingWithYou.text = dataItem.living_with_you
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemFamillyBackgroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataItem = dataList[position]

        if (display == 0){
            holder.binding.btMenu.visibility = View.GONE
        }else{
            holder.binding.btMenu.visibility = View.VISIBLE
        }

        holder.binding.btMenu.setOnClickListener{
            openMenu(holder.binding.btMenu , dataItem)
        }
        holder.bind(dataItem)
    }

    // This Function is used for open Popup Menu Screen
    private fun openMenu(item: ImageView, dataItem: FamilyDataModel) {
        val popupMenu = PopupMenu(requireContext, item)
        popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
            when(menuItem.itemId){
                R.id.btEdit ->{
                    listener?.editItem(dataItem)
                }

                R.id.btDelete ->{
                    listener?.deleteItem(dataItem)
                }
            }

            true
        }

        // Showing the popup menu
        popupMenu.show()
    }


}
package com.staffrakho.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.staffrakho.R
import com.staffrakho.activity.ChatActivity
import com.staffrakho.dataModel.MyJobs
import com.staffrakho.dataModel.MessageInboxList
import com.staffrakho.databinding.ItemMessageInboxBinding
import com.staffrakho.utility.AppConstant
import kotlin.random.Random


class MessageInboxAdapter(
    private var notificationList: ArrayList<MessageInboxList>,
    var requireContext: Activity,
) : RecyclerView.Adapter<MessageInboxAdapter.Holder>() {

    interface OnRequestAction {
        fun clearChat(item: MessageInboxList)
        fun deleteChat(item: MessageInboxList)
    }

    private var listener: OnRequestAction? = null

    fun setOnRequestAction(listener: OnRequestAction) {
        this.listener = listener
    }

    class Holder(val binding: ItemMessageInboxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: MessageInboxList, requireContext: Activity) {
            binding.tvName.text = dataItem.to_name
            binding.tvRole.text = ""

            if (dataItem.profile_picture == null || dataItem.profile_picture == "") {
                binding.tvLogo.visibility = View.GONE
                binding.tvDefalutImage.visibility = View.VISIBLE

                if (dataItem.to_name != null) {
                    if (dataItem.to_name.split(" ").filter { it.isNotEmpty() }
                            .joinToString("") { it[0].uppercase() }.length >= 2) {
                        binding.tvDefalutImage.text =
                            dataItem.to_name.split(" ").filter { it.isNotEmpty() }
                                .joinToString("") { it[0].uppercase() }.substring(0, 2)
                    } else {
                        binding.tvDefalutImage.text =
                            dataItem.to_name.split(" ").filter { it.isNotEmpty() }
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

                Glide.with(requireContext).load(AppConstant.MEDIA_BASE_URL + dataItem.profile_picture)
                    .into(binding.tvLogo)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemMessageInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataItem = notificationList[position]
        holder.bind(dataItem, requireContext)


        holder.itemView.setOnClickListener(){
            val intent = Intent(requireContext,ChatActivity::class.java)
            intent.putExtra(AppConstant.CHANNEL_ID , dataItem.id)
            intent.putExtra(AppConstant.RECEIVER_NAME ,  dataItem.to_name)
            intent.putExtra(AppConstant.RECEIVER_PROFILE_PICTURE ,  dataItem.profile_picture)
            requireContext.startActivity(intent)
        }

        holder.itemView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // When the button is pressed, change the background color
                    holder.itemView.setBackgroundColor(requireContext.resources.getColor(R.color.pressColor))
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // When the press is released, reset the background color
                    holder.itemView.setBackgroundColor(requireContext.resources.getColor(R.color.white))
                }
            }
            false
        }

        holder.itemView.setOnLongClickListener {
            openMenu(holder.itemView , dataItem)
            true
        }



    }


    private fun openMenu(item: View, dataItem: MessageInboxList) {
        val popupMenu = PopupMenu(requireContext, item)
        popupMenu.menuInflater.inflate(R.menu.chat_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
            when (menuItem.itemId) {

                R.id.btClear -> {
                    listener?.clearChat(dataItem)
                }


                R.id.btDelete -> {
                    listener?.deleteChat(dataItem)
                }

            }
            true
        }


        // Showing the popup menu
        popupMenu.show()
    }




}
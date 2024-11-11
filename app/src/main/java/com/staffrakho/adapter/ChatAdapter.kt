package com.staffrakho.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.staffrakho.R
import com.staffrakho.dataModel.ChatList
import com.staffrakho.dataModel.MessageInboxList
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.FileDownloader
import java.net.URL


class ChatAdapter(
    private val chatMessages: ArrayList<ChatList>,
    private var senderName: String,
    private var requireContect: Activity,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnRequestAction {
        fun deleteMessage(item: ChatList)
    }

    private var listener: OnRequestAction? = null

    fun setOnRequestAction(listener: OnRequestAction) {
        this.listener = listener
    }

    // Different View Types
    companion object {
        const val VIEW_TYPE_TEXT_SENT = 1
        const val VIEW_TYPE_TEXT_RECEIVED = 2
        const val VIEW_TYPE_PDF_SENT = 3
        const val VIEW_TYPE_PDF_RECEIVED = 4
        const val VIEW_TYPE_IMAGE_SENT = 5
        const val VIEW_TYPE_IMAGE_RECEIVED = 6
    }


    override fun getItemViewType(position: Int): Int {
        val message = chatMessages[position]
        return if (message.from_name == senderName) {
            if (message.attachment != null) {
                val ext = getFileExtensionFromUrl(AppConstant.MEDIA_BASE_URL + message.attachment)
                Log.e("Extenssion", ext.toString())
                if (ext == "jpg" || ext == "jpeg" || ext == "png") {
                    VIEW_TYPE_IMAGE_RECEIVED
                } else {
                    VIEW_TYPE_PDF_RECEIVED
                }
            } else {
                VIEW_TYPE_TEXT_RECEIVED
            }
        } else {
            if (message.attachment != null) {
                val ext = getFileExtensionFromUrl(AppConstant.MEDIA_BASE_URL + message.attachment)
                Log.e("Extenssion", ext.toString())
                if (ext == "jpg" || ext == "jpeg" || ext == "png") {
                    VIEW_TYPE_IMAGE_SENT
                } else {
                    VIEW_TYPE_PDF_SENT
                }
            } else {
                VIEW_TYPE_TEXT_SENT
            }

        }

    }

    private fun getFileExtensionFromUrl(url: String): String? {
        return try {
            val fileName = URL(url).path.substringAfterLast('/')
            fileName.substringAfterLast('.', "")
        } catch (e: Exception) {
            null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TEXT_SENT -> TextViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_text_sent, parent, false)
            )

            VIEW_TYPE_TEXT_RECEIVED -> TextViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_text_received, parent, false)
            )


            VIEW_TYPE_PDF_SENT -> PdfViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_pdf_sent, parent, false)
            )

            VIEW_TYPE_IMAGE_SENT -> ImageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_image_sent, parent, false)
            )

            VIEW_TYPE_IMAGE_RECEIVED -> ImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image_received, parent, false)
            )

            else -> PdfViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_pdf_received, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatMessages[position]
        when (holder) {
            is TextViewHolder -> holder.bind(message)
            is ImageViewHolder -> holder.bind(message)
            is PdfViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = chatMessages.size

    // ViewHolder for Text Messages
    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text_message)
        fun bind(message: ChatList) {

            itemView.setOnLongClickListener {
                val popupMenu = PopupMenu(itemView.context, itemView)
                popupMenu.menuInflater.inflate(R.menu.message_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
                    when (menuItem.itemId) {

                        R.id.btDeleteMessage -> {
                            listener?.deleteMessage(message)
                        }

                    }
                    true
                }


                // Showing the popup menu
                popupMenu.show()

                true
            }

            textView.text = message.message
        }
    }

    // ViewHolder for PDF Messages
    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pdfBox: ConstraintLayout = itemView.findViewById(R.id.pdfBox)
        private val pdfView: TextView = itemView.findViewById(R.id.pdf_message)
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)

        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun bind(message: ChatList) {

            pdfBox.setOnClickListener() {
                FileDownloader.downloadFile(
                    AppConstant.MEDIA_BASE_URL + message.attachment, itemView.context
                )
            }


            itemView.setOnLongClickListener {
                val popupMenu = PopupMenu(itemView.context, itemView)
                popupMenu.menuInflater.inflate(R.menu.message_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
                    when (menuItem.itemId) {

                        R.id.btDeleteMessage -> {
                            listener?.deleteMessage(message)
                        }

                    }
                    true
                }


                // Showing the popup menu
                popupMenu.show()

                true
            }

            pdfView.text = "${message.attachment}"
            tvMessage.text = "${message.message}"
        }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageBox: ConstraintLayout = itemView.findViewById(R.id.imageBox)
        private val tvImageShow: ImageView = itemView.findViewById(R.id.tvImageShow)
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)

        @SuppressLint("SetTextI18n")
        fun bind(message: ChatList) {
            imageBox.setOnClickListener() {
                FileDownloader.downloadFile(
                    AppConstant.MEDIA_BASE_URL + message.attachment, itemView.context
                )
            }

            itemView.setOnLongClickListener {
                val popupMenu = PopupMenu(itemView.context, itemView)
                popupMenu.menuInflater.inflate(R.menu.message_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
                    when (menuItem.itemId) {

                        R.id.btDeleteMessage -> {
                            listener?.deleteMessage(message)
                        }

                    }
                    true
                }


                // Showing the popup menu
                popupMenu.show()

                true
            }



            Glide.with(requireContect)
                .load(AppConstant.MEDIA_BASE_URL + message.attachment)
                .into(tvImageShow)

            tvMessage.text = "${message.message}"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(newMessage: ChatList) {
        chatMessages.add(newMessage)

        notifyDataSetChanged()
    }
}

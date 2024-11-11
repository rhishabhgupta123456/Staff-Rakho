package com.staffrakho.dataModel

data class MessageInboxDataModel(
    var message: String,
    var status: Boolean,
    var data: ArrayList<MessageInboxList>,
)

data class MessageInboxList(
    val id: String,
    val from_name: String,
    val to_name: String,
    val profile_picture: String? = null,
)
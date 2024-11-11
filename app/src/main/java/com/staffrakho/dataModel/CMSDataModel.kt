package com.staffrakho.dataModel

data class CMSDataModel(
    val success: Boolean,
    val message: String,
    val data: CMSData,
)

data class CMSData(
    val privacy_policy: Content,
    val about_us: Content,
    val contact_us: Content,
    val terms_conditions: Content,
)

data class Content(
    val page_title: String,
    val page_content: String,
    val image: String?,
    val id: String,
)

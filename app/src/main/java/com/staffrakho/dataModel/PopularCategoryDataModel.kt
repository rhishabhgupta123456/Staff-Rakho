package com.staffrakho.dataModel

data class PopularCategoryDataModel(
    val success: Boolean,
    val message: String,
    val data: ArrayList<PopularCategoryList>,
)

data class PopularCategoryList(
    val id: Int,
    val name: String,
    val slug: String,
    val status: String,
    val image: String? = null,
    val jobs_count: String,
)

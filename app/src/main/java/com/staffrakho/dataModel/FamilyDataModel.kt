package com.staffrakho.dataModel

data class FamilyList(
    val family: ArrayList<FamilyDataModel>,
    val workExperience: ArrayList<WorkExperienceDataModel>,
)

data class FamilyDataModel(
    val id: Int,
    val user_id: Int,
    val name: String,
    val relation: String,
    val relation_name: String,
    val living_with_you: String,
)

data class WorkExperienceDataModel(
    val id: Int,
    val category_id: Int,
    val category: String,
    val sub_category_id: Int,
    val sub_category: String,
    val from_date: String,
    val to_date: String,
    val shop_name: String,
    val reason_for_change: String,
    val role: String,
)

package com.staffrakho.dataModel

data class AppliedJobDataModel(
    val success: Boolean,
    val message: String,
    val pagination: PageSetup?,
    val data: ArrayList<JobList>,
)

data class PageSetup(
    val current_page: Int,
    val page_size: Int,
    val total: Int,
    val total_pages: Int,
    val offset: Int,
)

data class JobList(
    val id: Int,
    val userId: String,
    val company_id: String,
    val title: String,
    val slug: String,
    val description: String,
    val roles: String,
    val category_id: String,
    val position: String,
    val address: String,
    val featured: String,
    val type: String?,
    val status: String,
    val salary_label: String,
    val pin_code: String,
    val required_education_label: String,
    val experience_label: String,
    val type_label: String?,
    val phone: String,
    val email: String,
    val whatsapp: String,
    val direct_apply: Int,
    val call_apply: Int,
    var is_favorite: Int,
    val message_apply: Int,
    val whatsapp_apply: Int,
    val last_date: String,
    val deleted_at: String,
    val created_at: String,
    val updated_at: String,
    val number_of_vacancy: String,
    val experience: String,
    val gender: String,
    val salary: String,
    val sub_category_id: String,
    val state_id: String,
    val city_id: String,
    val profile_type: String,
    val company_logo: String?,
    val company_name: String?,
    val category_name: String,
    val sub_category_name: String,
    val profile_type_label: String,
    val company_look_label: String,
    val business_type_label: String,
    val state_name: String,
    val city_name: String,
    val already_applied: String,
)
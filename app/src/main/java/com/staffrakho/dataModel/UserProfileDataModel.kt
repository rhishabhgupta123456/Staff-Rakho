package com.staffrakho.dataModel

data class UserProfileDataModel(
    val data: ArrayList<UserData>,
    val message: String,
    val pagination: PageSetup?,
    val success: Boolean,
)

data class UserData(
    val address: String,
    val avatar: String?,
    val bio: String,
    val city_name: String,
    val cover_letter: String,
    val disability: String,
    val dob: String,
    val email: String,
    val family: ArrayList<FamilyDataModel>?,
    val gender: String,
    val gender_label: String,
    val id: Int,
    val is_shortlisted: Int,
    val mobile: String,
    val name: String?,
    val physically_fit: String,
    val profile_type: String,
    val resume: String,
    val role: String,
    val skills: String,
    val state_name: String,
    val transport_facility: String,
    val workExperience: ArrayList<WorkExperienceDataModel>?,
    val zip: String,
    val job_title: String?= null,

    val sub_district: String?= null,
    val village: String?= null,
    val age_range_label: String?= null,
    val disability_details: String?= null,
    val marrital_status_label: String?= null,
)



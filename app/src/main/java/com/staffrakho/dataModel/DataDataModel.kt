package com.staffrakho.dataModel

data class AllFilterList(
    val data: AllFilterDataModel,
)

data class AllFilterDataModel(
    val relations: ArrayList<Model1>,
    val salaryRange: ArrayList<Model1>,
    val profileType: ArrayList<Model1>,
    val gender: ArrayList<Model1>,
    val experiences: ArrayList<Model1>,
    val companyLook: ArrayList<Model1>,
    val transportFacility: ArrayList<Model1>,
    val physicalFit: ArrayList<Model1>,
    val workingDays: ArrayList<Model1>,
    val jobType: ArrayList<Model1>,
    val businessType: ArrayList<Model1>,
    val category: ArrayList<Model2>,
    val ready_for_short_terms_job: ArrayList<Model1>,
    val age_range: ArrayList<Model1>,
    val marrital_status: ArrayList<Model1>,
    val job_looking_status: ArrayList<Model1>,
)

data class Model1(
    val id: String,
    val name: String?,
)

data class Model2(
    val id: String,
    val country_id: String,
    val state_name: String,
    val created_at: String?,
    val updated_at: String?,
)


data class StateList(
    val data: ArrayList<StateDataModel>,
)

data class StateDataModel(
    val id: String,
    val country_id: String,
    val state_name: String?,
    val created_at: String?,
    val updated_at: String?,
)


data class CityList(
    val data: ArrayList<CityDataModel>,
)

data class CityDataModel(
    val id: String,
    val country_id: String,
    val state_id: String,
    val city_name: String?,
    val created_at: String?,
    val updated_at: String?,
)

data class CategoryList(
    val data: ArrayList<CategoryDataModel>,
)

data class CategoryDataModel(
    val id: String,
    val name: String,
    val slug: String,
    val status: String,
)

data class SubCategoryList(
    val data: ArrayList<CategoryDataModel>,
)

data class SubCategoryDataModel(
    val id: String,
    val name: String,
    val slug: String,
    val category_id: String,
)

data class EducationList(
    val data: ArrayList<EducationDataModel>,
)

data class EducationDataModel(
    val id: String,
    val name: String,
  )
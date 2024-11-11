package com.staffrakho.networkModel.employerNetworkPannel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.SingleLiveEvent
import okhttp3.MultipartBody
import okhttp3.RequestBody


class EmployerViewModel : ViewModel() {

    private val employerRepositoryImplement: EmployerRepositoryImplement =
        EmployerRepositoryImplement()

    fun getUserEmployerProfile(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getUserEmployerProfile(bearerToken)
    }

    fun getUserEmployerDashBoard(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getUserEmployerDashBoard(bearerToken)
    }

    fun getAllMyJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getAllMyJobs(bearerToken)
    }

    fun getAllFilterList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getAllFilterList(bearerToken)
    }

    fun getEducation(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getEducation(bearerToken)
    }

    fun getStateList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getStateList(bearerToken)
    }


    fun getCityList(
        bearerToken: String,
        stateId: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getCityList(bearerToken, stateId)
    }

    fun createChannelList(
        bearerToken: String,
        receiverID: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.createChannelList(bearerToken, receiverID)
    }


    fun getCategoryList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getCategoryList(bearerToken)
    }

    fun getRecentProfiles(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getRecentProfiles(bearerToken)
    }

    fun getApplicantsList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getApplicantsList(bearerToken)
    }

    fun getVisitedProfile(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getVisitedProfile(bearerToken)
    }

    fun getShortListedProfile(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getShortListedProfile(bearerToken)
    }

    fun getNewProfile(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getNewProfile(bearerToken)
    }

    fun findPerfectEmployee(
        bearerToken: String,
        categoryId: String,
        subCategoryId: String,
        jobType: String,
        stateId: String,
        cityId: String,
        currentPage: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.findPerfectEmployee(
            bearerToken,
            categoryId,
            subCategoryId,
            jobType,
            stateId,
            cityId,
            currentPage
        )
    }

    fun getSubCategoryList(
        bearerToken: String,
        categoryId: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.getSubCategoryList(bearerToken, categoryId)
    }

    fun addJobs(
        bearerToken: String,
        title: String,
        description: String,
        roles: String,
        categoryId: String,
        subCategoryId: String,
        position: String,
        address: String,
        type: String,
        experience: String,
        numberOfVacancy: String,
        gender: String,
        status: String,
        lastDate: String,
        stateId: String,
        cityId: String,
        profileType: String,
        salary: String,
        pinCode: String?,
        requiredEducation: String?,
        requiredSkills: String?,
        preferredCallTime: String?,
        phone: String?,
        email: String?,
        whatsapp: String?,
        directApply: Int?,
        callApply: Int?,
        messageApply: Int?,
        whatsappApply: Int,
        village: String,
        subDistrict: String,
        jobDuration: Int?,
        ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.addJobs(
            bearerToken,
            title,
            description,
            roles,
            categoryId,
            subCategoryId,
            position,
            address,
            type,
            experience,
            numberOfVacancy,
            gender,
            status,
            lastDate,
            stateId,
            cityId,
            profileType,
            salary,
            pinCode,
            requiredEducation,
            requiredSkills,
            preferredCallTime,
            phone,
            email,
            whatsapp,
            directApply,
            callApply,
            messageApply,
            whatsappApply,
            village,
            subDistrict,
            jobDuration


        )
    }


    fun updateMyJobs(
        bearerToken: String,
        id: Int,
        title: String,
        description: String,
        roles: String,
        categoryId: String,
        subCategoryId: String,
        position: String,
        address: String,
        type: String,
        experience: String,
        numberOfVacancy: String,
        gender: String,
        status: String,
        lastDate: String,
        stateId: String,
        cityId: String,
        profileType: String,
        salary: String,
        pinCode: String?,
        requiredEducation: String?,
        requiredSkills: String?,
        preferredCallTime: String?,
        phone: String?,
        email: String?,
        whatsapp: String?,
        directApply: Int?,
        callApply: Int?,
        messageApply: Int?,
        whatsappApply: Int,

        ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.updateMyJobs(
            bearerToken,
            id,
            title,
            description,
            roles,
            categoryId,
            subCategoryId,
            position,
            address,
            type,
            experience,
            numberOfVacancy,
            gender,
            status,
            lastDate,
            stateId,
            cityId,
            profileType,
            salary,
            pinCode,
            requiredEducation,
            requiredSkills,
            preferredCallTime,
            phone,
            email,
            whatsapp,
            directApply,
            callApply,
            messageApply,
            whatsappApply

        )
    }

    fun deleteMyJobs(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.deleteMyJobs(bearerToken, jobId)
    }

    fun deleteShortListUser(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.deleteShortListUser(bearerToken, jobId)
    }

    fun shortListUser(
        bearerToken: String,
        userID: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.shortListUser(bearerToken, userID)
    }

    fun visitedUser(
        bearerToken: String,
        userID: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.visitedUser(bearerToken, userID)
    }

    fun changeJobStateActivate(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.changeJobStateActivate(bearerToken, jobId)
    }

    fun updateEmployerLogo(
        bearerToken: String,
        logo: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.updateEmployerLogo(bearerToken, logo)
    }

    fun updateEmployerBanner(
        bearerToken: String,
        logo: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.updateEmployerBanner(bearerToken, logo)
    }

    fun updateEmployerInteriorPhoto(
        bearerToken: String,
        logo: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.updateEmployerInteriorPhoto(bearerToken, logo)
    }

    fun updateEmployerExteriorPhoto(
        bearerToken: String,
        logo: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.updateEmployerExteriorPhoto(bearerToken, logo)
    }


    fun editEmployerProfile(
        bearerToken: String,
        companyName: String,
        address: String,
        cityId: String?,
        zip: String,
        businessType: String?,
        stateId: String?,
        floor: Int?,
        fromTime: String,
        toTime: String,
        boysFromTime: String,
        boysToTime: String,
        girlsFromTime: String,
        girlsToTime: String,
        contactNo: String,
        companyLook: String?,
        openingDays: List<String>,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return employerRepositoryImplement.editEmployerProfile(
            bearerToken,
            companyName,
            address,
            cityId,
            zip,
            businessType,
            stateId,
            floor,
            fromTime,
            toTime,
            boysFromTime,
            boysToTime,
            girlsFromTime,
            girlsToTime,
            contactNo,
            companyLook,
            openingDays
        )
    }

}
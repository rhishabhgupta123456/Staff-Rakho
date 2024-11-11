package com.staffrakho.networkModel.jobSeekerNetworkPanel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.SingleLiveEvent
import okhttp3.MultipartBody
import okhttp3.RequestBody


class JobSeekerViewModel : ViewModel() {

    private val jobSeekerRepositoryImplement = JobSeekerRepositoryImplement()


    fun getUserProfile(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getUserProfile(bearerToken)
    }

    fun savedTheJob(
        bearerToken: String,
        jobID: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.savedTheJob(bearerToken, jobID)
    }


    fun unSavedJob(
        bearerToken: String,
        jobID: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.unSavedJob(bearerToken, jobID)
    }


    fun visitedJob(
        bearerToken: String,
        jobID: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.visitedJob(bearerToken, jobID)
    }

    fun getUserEmployerProfile(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getUserEmployerProfile(bearerToken)
    }


    fun getPopularCategory(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getPopularCategory(bearerToken)
    }


    fun editProfile(
        bearerToken: String,
        dob: String,
        gender: String?,
        stateId: String?,
        cityId: String?,
        address: String,
        zip: String,
        skills: String,
        transportFacility: String?,
        physicallyFit: String?,
        disability: String?,
        profileType: String?,
        bio: String,
        role: String,
        subDistrict: String,
        disabilityDetails: String,
        ageRange: String?,
        village: String,
        marritalstatus: String?,
        jobLookingStatus: String?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.editProfile(
            bearerToken,
            dob,
            gender,
            stateId,
            cityId,
            address,
            zip,
            skills,
            transportFacility,
            physicallyFit,
            disability,
            profileType,
            bio,
            role,
            subDistrict,
            disabilityDetails,
            ageRange,
            village,
            marritalstatus,
            jobLookingStatus
        )
    }


    fun addWorkExperience(
        bearerToken: String,
        categoryId: String,
        subCategoryId: String,
        fromDate: String,
        toDate: String,
        reasonForChange: String,
        shopName: String,
        role: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.addWorkExperience(
            bearerToken,
            categoryId,
            subCategoryId,
            fromDate,
            toDate,
            reasonForChange,
            shopName,
            role
        )
    }

    fun updateWorkExperience(
        bearerToken: String,
        id: Int,
        categoryId: String,
        subCategoryId: String,
        fromDate: String,
        toDate: String,
        reasonForChange: String,
        shopName: String,
        role: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.updateWorkExperience(
            bearerToken,
            id,
            categoryId,
            subCategoryId,
            fromDate,
            toDate,
            reasonForChange,
            shopName,
            role
        )
    }

    fun deleteWorkExperience(
        bearerToken: String,
        id: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.deleteWorkExperience(
            bearerToken,
            id,
        )
    }


    fun addFamilyMember(
        bearerToken: String,
        name: String,
        relation: String,
        livingWithYou: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.addFamilyMember(
            bearerToken,
            name,
            relation,
            livingWithYou,
        )
    }

    fun updateFamilyMember(
        bearerToken: String,
        id: Int,
        name: String,
        relation: String,
        livingWithYou: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.updateFamilyMember(
            bearerToken,
            id,
            name,
            relation,
            livingWithYou,
        )
    }

    fun deleteFamilyMember(
        bearerToken: String,
        id: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.deleteFamilyMember(
            bearerToken,
            id,
        )
    }

    fun getAllFilterList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getAllFilterList(bearerToken)
    }

    fun getStateList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getStateList(bearerToken)
    }


    fun getCityList(
        bearerToken: String,
        stateId: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getCityList(bearerToken, stateId)
    }


    fun getCategoryList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getCategoryList(bearerToken)
    }


    fun getRecentVisitedJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getRecentVisitedJobs(bearerToken)
    }

    fun getNewJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getNewJobs(bearerToken)
    }

    fun getFavouriteJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getFavouriteJobs(bearerToken)
    }

    fun getProfileMatchingJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getProfileMatchingJobs(bearerToken)
    }


    fun getSubCategoryList(
        bearerToken: String,
        categoryId: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getSubCategoryList(bearerToken, categoryId)
    }

    fun updateProfilePicture(
        bearerToken: String,
        profilePicture: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.updateProfilePicture(bearerToken, profilePicture)
    }

    fun updateResume(
        bearerToken: String, pdfPart: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.updateResume(bearerToken, pdfPart)
    }

    fun sendMessage(
        bearerToken: String,
        id: Int,
        message: RequestBody?,
        attachment: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.sendMessage(bearerToken, id, message, attachment)
    }

    fun deleteChannel(
        bearerToken: String,
        id: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.deleteChannel(bearerToken, id)
    }

    fun deleteParticularMessage(
        bearerToken: String,
        id: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.deleteParticularMessage(bearerToken, id)
    }

    fun clearAllChat(
        bearerToken: String,
        id: Int,
        toId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.clearAllChat(bearerToken, id, toId)
    }

    fun findJobList(
        bearerToken: String,
        categoryId: String,
        subCategoryId: String,
        jobType: String,
        stateId: String,
        cityId: String,
        gender: String,
        salary: String,
        zip: String,
        businessType: String,
        companyLook: String,
        currentPage: String,
        sortOrder: String,
        sortField: String,

        ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.findJobList(
            bearerToken,
            categoryId,
            subCategoryId,
            jobType,
            stateId,
            cityId,
            gender,
            salary,
            zip,
            businessType,
            companyLook,
            currentPage,
            sortOrder,
            sortField
        )
    }

    fun deleteAccount(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.deleteAccount(
            bearerToken,
        )
    }

    fun getCMS(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getCMS(
            bearerToken,
        )
    }

    fun getAppliedJob(
        bearerToken: String,
        sortOrder: String,
        sortField: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getAppliedJob(bearerToken, sortOrder, sortField)
    }

    fun getChannelList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getChannelList(bearerToken)
    }

    fun getChatHistory(bearerToken: String, id: Int): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getChatHistory(bearerToken, id)
    }

    fun getDashboardCounter(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getDashboardCounter(bearerToken)
    }

    fun applyTheJob(bearerToken: String, jobId: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.applyTheJob(bearerToken, jobId)
    }

    fun getCompanyDetails(
        bearerToken: String,
        companyId: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.getCompanyDetails(bearerToken, companyId)
    }


    fun emailUpdateRequest(
        bearerToken: String,
        email: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.emailUpdateRequest(bearerToken, email, userType)
    }

    fun changePassword(
        bearerToken: String,
        currentPassword: String,
        password: String,
        passwordConfirmation: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.changePassword(
            bearerToken,
            currentPassword,
            password,
            passwordConfirmation
        )
    }

    fun emailUpdate(
        bearerToken: String,
        email: String,
        otp: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.emailUpdate(bearerToken, email, otp, userType)
    }

    fun mobileUpdateRequest(
        bearerToken: String,
        mobile: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.mobileUpdateRequest(bearerToken, mobile, userType)
    }

    fun mobileUpdate(
        bearerToken: String,
        mobile: String,
        otp: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return jobSeekerRepositoryImplement.mobileUpdate(bearerToken, mobile, otp, userType)
    }


}
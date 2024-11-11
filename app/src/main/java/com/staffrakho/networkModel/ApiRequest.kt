package com.staffrakho.networkModel

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.staffrakho.utility.AppConstant
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


class ApiInterface {

    private var apiRequest: ApiRequest

    init {

        val headerInterceptor = Interceptor { chain ->
            val originalRequest: Request = chain.request()
            val requestWithHeaders: Request = originalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(requestWithHeaders)
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }


        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(AppConstant.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiRequest = retrofit.create(ApiRequest::class.java)


    }

    fun signIn(emailOrPhone: String, password: String, userType: String): Call<JsonObject?> {
        return apiRequest.signIn(emailOrPhone, password, userType)
    }

    fun register(
        name: String, email: String, mobile: String, password: String, gender: String, dob: String,
    ): Call<JsonObject?> {
        return apiRequest.register(name, email, mobile, password, gender, dob)
    }

    fun employerRegister(
        name: String, email: String, mobile: String, password: String, cname: String,
    ): Call<JsonObject?> {
        return apiRequest.employerRegister(name, email, mobile, password, cname)
    }

    fun savedTheJob(
        bearerToken: String,
        jobID: Int,
    ): Call<JsonObject?> {
        return apiRequest.savedTheJob(bearerToken, jobID)
    }

    fun unSavedJob(
        bearerToken: String,
        id: Int,
    ): Call<JsonObject?> {
        return apiRequest.unSavedJob(
            bearerToken,
            id,
        )
    }

    fun socialLogIn(
        name: String,
        email: String,
        socialType: String,
        socialId: String,
        userType: String,
    ): Call<JsonObject?> {
        return apiRequest.socialLogIn(name, email, socialType, socialId, userType)
    }


    fun forgetPassword(username: String, userType: String): Call<JsonObject?> {
        return apiRequest.forgetPassword(username, userType)
    }

    fun resetPassword(
        username: String,
        userType: String,
        password: String,
        passwordConfirmation: String,
        otp: String,
    ): Call<JsonObject?> {
        return apiRequest.resetPassword(username, userType, password, passwordConfirmation, otp)
    }

    fun getUserProfile(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getUserProfile(bearerToken)
    }


    fun visitedJob(
        bearerToken: String,
        jobID: Int,
    ): Call<JsonObject?> {
        return apiRequest.visitedJob(
            bearerToken,
            jobID,
        )
    }

    fun getUserEmployerProfile(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getUserEmployerProfile(bearerToken)
    }

    fun getRecentProfiles(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getRecentProfiles(bearerToken)
    }

    fun getUserEmployeeDashBoard(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getUserEmployeeDashBoard(bearerToken)
    }

    fun getPopularCategory(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getPopularCategory(bearerToken)
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
    ): Call<JsonObject?> {
        return apiRequest.editProfile(
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

    fun emailUpdateRequest(
        bearerToken: String, email: String, userType: String,

        ): Call<JsonObject?> {
        return apiRequest.emailUpdateRequest(bearerToken, email, userType)
    }

    fun changePassword(
        bearerToken: String,
        currentPassword: String,
        password: String,
        passwordConfirmation: String,

        ): Call<JsonObject?> {
        return apiRequest.changePassword(
            bearerToken,
            currentPassword,
            password,
            passwordConfirmation
        )
    }

    fun emailUpdate(
        bearerToken: String, email: String, otp: String, userType: String,
    ): Call<JsonObject?> {
        return apiRequest.emailUpdate(bearerToken, email, otp, userType)
    }

    fun mobileUpdateRequest(
        bearerToken: String, mobile: String, userType: String,
    ): Call<JsonObject?> {
        return apiRequest.mobileUpdateRequest(bearerToken, mobile, userType)
    }

    fun mobileUpdate(
        bearerToken: String, mobile: String, otp: String, userType: String,
    ): Call<JsonObject?> {
        return apiRequest.mobileUpdate(bearerToken, mobile, otp, userType)
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
    ): Call<JsonObject?> {

        return apiRequest.editEmployerProfile(
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


    fun addWorkExperience(
        bearerToken: String,
        categoryId: String,
        subCategoryId: String,
        fromDate: String,
        toDate: String,
        reasonForChange: String,
        shopName: String,
        role: String,
    ): Call<JsonObject?> {
        return apiRequest.addWorkExperience(
            bearerToken,
            categoryId,
            subCategoryId,
            fromDate,
            toDate,
            reasonForChange,
            shopName,
            role,
        )

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
    ): Call<JsonObject?> {
        return apiRequest.addJobs(
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

        ): Call<JsonObject?> {
        return apiRequest.updateMyJobs(
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
    ): Call<JsonObject?> {
        return apiRequest.updateWorkExperience(
            bearerToken,
            id,
            categoryId,
            subCategoryId,
            fromDate,
            toDate,
            reasonForChange,
            shopName,
            role,
        )

    }

    fun deleteWorkExperience(
        bearerToken: String,
        id: Int,
    ): Call<JsonObject?> {
        return apiRequest.deleteWorkExperience(
            bearerToken,
            id,
        )
    }


    fun getApplicants(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getApplicants(
            bearerToken,
        )
    }

    fun getVisitedProfile(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getVisitedProfile(
            bearerToken,
        )
    }

    fun getShortListedProfile(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getShortListedProfile(
            bearerToken,
        )
    }

    fun getNewProfile(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getNewProfile(
            bearerToken,
        )
    }

    fun deleteMyJobs(
        bearerToken: String,
        jobId: Int,
    ): Call<JsonObject?> {
        return apiRequest.deleteMyJobs(
            bearerToken,
            jobId,
        )
    }

    fun deleteShortListUser(
        bearerToken: String,
        jobId: Int,
    ): Call<JsonObject?> {
        return apiRequest.deleteShortListUser(
            bearerToken,
            jobId,
        )
    }

    fun shortListUser(
        bearerToken: String,
        userId: Int,
    ): Call<JsonObject?> {
        return apiRequest.shortListUser(
            bearerToken,
            userId,
        )
    }

    fun visitedUser(
        bearerToken: String,
        userId: Int,
    ): Call<JsonObject?> {
        return apiRequest.visitedUser(
            bearerToken,
            userId,
        )
    }

    fun changeJobStateActivate(
        bearerToken: String,
        jobId: Int,
    ): Call<JsonObject?> {
        return apiRequest.changeJobStateActivate(
            bearerToken,
            jobId,
        )
    }

    fun addFamilyMember(
        bearerToken: String,
        name: String,
        relation: String,
        livingWithYou: String,
    ): Call<JsonObject?> {
        return apiRequest.addFamilyMember(
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
    ): Call<JsonObject?> {
        return apiRequest.updateFamilyMember(
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
    ): Call<JsonObject?> {
        return apiRequest.deleteFamilyMember(
            bearerToken,
            id,
        )
    }


    fun getAllMyJobs(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getAllMyJobs(bearerToken)
    }


    fun getAllFilterList(bearerToken: String): Call<JsonObject?> {
        return apiRequest.getAllFilterList(bearerToken)
    }

    fun getEducation(bearerToken: String): Call<JsonObject?> {
        return apiRequest.getEducation(bearerToken)
    }

    fun getStateList(bearerToken: String): Call<JsonObject?> {
        return apiRequest.getStateList(bearerToken)
    }

    fun getCityList(bearerToken: String, stateId: String): Call<JsonObject?> {
        return apiRequest.getCityList(bearerToken, stateId)
    }

    fun createChannelList(
        bearerToken: String,
        receiverID: String,
    ): Call<JsonObject?> {
        return apiRequest.createChannelList(bearerToken, receiverID)
    }


    fun sendMessage(
        bearerToken: String,
        id: Int,
        message: RequestBody?,
        attachment: MultipartBody.Part?,
    ): Call<JsonObject?> {
        return apiRequest.sendMessage(bearerToken, id, message, attachment)
    }

    fun deleteChannel(
        bearerToken: String,
        id: Int,
    ): Call<JsonObject?> {
        return apiRequest.deleteChannel(bearerToken, id)
    }

    fun deleteParticularMessage(
        bearerToken: String,
        id: Int,
    ): Call<JsonObject?> {
        return apiRequest.deleteParticularMessage(bearerToken, id)
    }

    fun clearAllChat(
        bearerToken: String,
        id: Int,
        toId: Int,
    ): Call<JsonObject?> {
        return apiRequest.clearAllChat(bearerToken, id, toId)
    }

    fun getCategoryList(bearerToken: String): Call<JsonObject?> {
        return apiRequest.getCategoryList(bearerToken)
    }


    fun getAppliedJob(
        bearerToken: String,
        sortOrder: String,
        sortField: String,
    ): Call<JsonObject?> {
        return apiRequest.getAppliedJob(bearerToken, sortOrder, sortField)
    }

    fun getChannelList(bearerToken: String): Call<JsonObject?> {
        return apiRequest.getChannelList(bearerToken)
    }

    fun getChatHistory(bearerToken: String, id: Int): Call<JsonObject?> {
        return apiRequest.getChatHistory(bearerToken, id)
    }

    fun getDashboardCounter(bearerToken: String): Call<JsonObject?> {
        return apiRequest.getDashboardCounter(bearerToken)
    }

    fun getCMS(bearerToken: String): Call<JsonObject?> {
        return apiRequest.getCMS(bearerToken)
    }

    fun getSubCategoryList(bearerToken: String, categoryId: String): Call<JsonObject?> {
        return apiRequest.getSubCategoryList(bearerToken, categoryId)
    }


    fun updateProfilePicture(
        bearerToken: String, profilePicture: MultipartBody.Part?,
    ): Call<JsonObject?> {
        return apiRequest.updateProfilePicture(bearerToken, profilePicture)
    }


    fun updateEmployerLogo(
        bearerToken: String, logo: MultipartBody.Part?,
    ): Call<JsonObject?> {
        return apiRequest.updateEmployerLogo(bearerToken, logo)
    }


    fun updateEmployerBanner(
        bearerToken: String, banner: MultipartBody.Part?,
    ): Call<JsonObject?> {
        return apiRequest.updateEmployerBanner(bearerToken, banner)
    }

    fun updateEmployerInteriorPhoto(
        bearerToken: String, banner: MultipartBody.Part?,
    ): Call<JsonObject?> {
        return apiRequest.updateEmployerInteriorPhoto(bearerToken, banner)
    }

    fun updateEmployerExteriorPhoto(
        bearerToken: String, banner: MultipartBody.Part?,
    ): Call<JsonObject?> {
        return apiRequest.updateEmployerExteriorPhoto(bearerToken, banner)
    }

    fun updateResume(bearerToken: String, updateResume: MultipartBody.Part?): Call<JsonObject?> {
        return apiRequest.updateResume(bearerToken, updateResume)
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
    ): Call<JsonObject?> {
        return apiRequest.findJobList(
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

    fun findPerfectEmployee(
        bearerToken: String,
        categoryId: String,
        subCategoryId: String,
        jobType: String,
        stateId: String,
        cityId: String,
        currentPage: String,
    ): Call<JsonObject?> {
        return apiRequest.findPerfectEmployee(
            bearerToken, categoryId, subCategoryId, jobType, stateId, cityId, currentPage
        )
    }


    fun getRecentVisitedJobs(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getRecentVisitedJobs(
            bearerToken,
        )
    }

    fun getNewJobs(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getNewJobs(
            bearerToken,
        )
    }

    fun getFavouriteJobs(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getFavouriteJobs(
            bearerToken,
        )
    }

    fun getProfileMatchingJobs(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.getProfileMatchingJobs(
            bearerToken,
        )
    }


    fun deleteAccount(
        bearerToken: String,
    ): Call<JsonObject?> {
        return apiRequest.deleteAccount(
            bearerToken,
        )
    }

    fun applyTheJob(
        bearerToken: String, jobId: String,
    ): Call<JsonObject?> {
        return apiRequest.applyTheJob(
            bearerToken, jobId
        )
    }

    fun getCompanyDetails(
        bearerToken: String, companyId: String,
    ): Call<JsonObject?> {
        return apiRequest.getCompanyDetails(
            bearerToken, companyId
        )
    }


}


interface ApiRequest {

    @Headers("Accept: application/json")
    @POST("login")
    @FormUrlEncoded
    fun signIn(
        @Field("username") emailOrPhone: String,
        @Field("password") password: String,
        @Field("user_type") userType: String,
    ): Call<JsonObject?>


    @Headers("Accept: application/json")
    @POST("register")
    @FormUrlEncoded
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("dob") dob: String,
    ): Call<JsonObject?>


    @Headers("Accept: application/json")
    @POST("employer-register")
    @FormUrlEncoded
    fun employerRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("cname") gender: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @POST("social-login")
    @FormUrlEncoded
    fun socialLogIn(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("social_type") socialType: String,
        @Field("social_id") socialId: String,
        @Field("user_type") userType: String,
    ): Call<JsonObject?>

    @POST("jobseeker/favorite-job")
    @FormUrlEncoded
    fun savedTheJob(
        @Header("Authorization") bearerToken: String,
        @Field("job_id") jobId: Int,
    ): Call<JsonObject?>

    @DELETE("jobseeker/favorite-job/{id}")
    fun unSavedJob(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>


    @Headers("Accept: application/json")
    @POST("forgot-password")
    @FormUrlEncoded
    fun forgetPassword(
        @Field("username") userName: String,
        @Field("user_type") userType: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @POST("reset-password")
    @FormUrlEncoded
    fun resetPassword(
        @Field("username") userName: String,
        @Field("user_type") userType: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
        @Field("otp") otp: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @PATCH("jobseeker/personal-details")
    @FormUrlEncoded
    fun editProfile(
        @Header("Authorization") bearerToken: String,
        @Field("dob") userName: String,
        @Field("gender") gender: String?,
        @Field("state_id") stateId: String?,
        @Field("city_id") cityId: String?,
        @Field("address") address: String,
        @Field("zip") zip: String,
        @Field("skills") skills: String,
        @Field("transport_facility") transportFacility: String?,
        @Field("physically_fit") physicallyFit: String?,
        @Field("disability") disability: String?,
        @Field("profile_type") profileType: String?,
        @Field("bio") bio: String,
        @Field("role") role: String,
        @Field("sub_district") subDistrict: String,
        @Field("disability_details") disabilityDetails: String,
        @Field("age_range") ageRange: String?,
        @Field("village") village: String,
        @Field("marrital_status") marritalstatus: String?,
        @Field("job_looking_status") jobLookingStatus: String?,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @PATCH("employer/profile")
    @FormUrlEncoded
    fun editEmployerProfile(
        @Header("Authorization") bearerToken: String,
        @Field("cname") companyName: String,
        @Field("address") address: String,
        @Field("city_id") cityId: String?,
        @Field("zip") zip: String,
        @Field("business_type") businessType: String?,
        @Field("state_id") stateId: String?,
        @Field("floor") floor: Int?,
        @Field("from_time") fromTime: String,
        @Field("to_time") toTime: String,
        @Field("boys_from_time") boysFromTime: String,
        @Field("boys_to_time") boysToTime: String,
        @Field("girls_from_time") girlsFromTime: String,
        @Field("girls_to_time") girlsToTime: String,
        @Field("contact_no") contactNo: String,
        @Field("company_look") companyLook: String?,
        @Field("opening_days[]") openingDays: List<String>,
    ): Call<JsonObject?>


    @POST("account/email-update-request")
    @FormUrlEncoded
    fun emailUpdateRequest(
        @Header("Authorization") bearerToken: String,
        @Field("email") email: String,
        @Field("user_type") userType: String,
    ): Call<JsonObject?>

    @PATCH("account/change-password")
    @FormUrlEncoded
    fun changePassword(
        @Header("Authorization") bearerToken: String,
        @Field("current_password") currentPassword: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
    ): Call<JsonObject?>


    @PATCH("account/email-update")
    @FormUrlEncoded
    fun emailUpdate(
        @Header("Authorization") bearerToken: String,
        @Field("email") email: String,
        @Field("otp") otp: String,
        @Field("user_type") userType: String,
    ): Call<JsonObject?>

    @POST("account/mobile-update-request")
    @FormUrlEncoded
    fun mobileUpdateRequest(
        @Header("Authorization") bearerToken: String,
        @Field("mobile") mobile: String,
        @Field("user_type") userType: String,
    ): Call<JsonObject?>

    @PATCH("account/mobile-update")
    @FormUrlEncoded
    fun mobileUpdate(
        @Header("Authorization") bearerToken: String,
        @Field("mobile") mobile: String,
        @Field("otp") otp: String,
        @Field("user_type") userType: String,
    ): Call<JsonObject?>


    @Headers("Accept: application/json")
    @POST("jobseeker/work-experience")
    @FormUrlEncoded
    fun addWorkExperience(
        @Header("Authorization") bearerToken: String,
        @Field("category_id") categoryId: String,
        @Field("sub_category_id") subCategoryId: String,
        @Field("from_date") fromDate: String,
        @Field("to_date") toDate: String,
        @Field("reason_for_change") reasonForChange: String,
        @Field("shop_name") shopName: String,
        @Field("role") role: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @POST("employer/job")
    @FormUrlEncoded
    fun addJobs(
        @Header("Authorization") bearerToken: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("roles") roles: String,
        @Field("category_id") categoryId: String?,
        @Field("sub_category_id") subCategoryId: String?,
        @Field("position") position: String?,
        @Field("address") address: String,
        @Field("type") type: String,
        @Field("experience") experience: String,
        @Field("number_of_vacancy") numberOfVacancy: String,
        @Field("gender") gender: String,
        @Field("status") status: String?,
        @Field("last_date") lastDate: String,
        @Field("state_id") stateId: String?,
        @Field("city_id") cityId: String?,
        @Field("profile_type") profileType: String?,
        @Field("salary") salary: String?,
        @Field("pin_code") pinCode: String?,
        @Field("required_education") requiredEducation: String?,
        @Field("required_skills") requiredSkills: String?,
        @Field("preferred_call_time") preferredCallTime: String?,
        @Field("phone") phone: String?,
        @Field("email") email: String?,
        @Field("whatsapp") whatsapp: String?,
        @Field("direct_apply") directApply: Int?,
        @Field("call_apply") callApply: Int?,
        @Field("message_apply") messageApply: Int?,
        @Field("whatsapp_apply") whatsappApply: Int,
        @Field("village") village: String,
        @Field("sub_district") subDistrict: String,
        @Field("job_duration") jobDuration: Int?,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @PATCH("jobseeker/work-experience/{id}")
    @FormUrlEncoded
    fun updateWorkExperience(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
        @Field("category_id") categoryId: String,
        @Field("sub_category_id") subCategoryId: String,
        @Field("from_date") fromDate: String,
        @Field("to_date") toDate: String,
        @Field("reason_for_change") reasonForChange: String,
        @Field("shop_name") shopName: String,
        @Field("role") role: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @PATCH("employer/job/{id}")
    @FormUrlEncoded
    fun updateMyJobs(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("roles") roles: String,
        @Field("category_id") categoryId: String?,
        @Field("sub_category_id") subCategoryId: String?,
        @Field("position") position: String?,
        @Field("address") address: String,
        @Field("type") type: String,
        @Field("experience") experience: String,
        @Field("number_of_vacancy") numberOfVacancy: String,
        @Field("gender") gender: String,
        @Field("status") status: String?,
        @Field("last_date") lastDate: String,
        @Field("state_id") stateId: String?,
        @Field("city_id") cityId: String?,
        @Field("profile_type") profileType: String?,
        @Field("salary") salary: String?,
        @Field("pin_code") pinCode: String?,
        @Field("required_education") requiredEducation: String?,
        @Field("required_skills") requiredSkills: String?,
        @Field("preferred_call_time") preferredCallTime: String?,
        @Field("phone") phone: String?,
        @Field("email") email: String?,
        @Field("whatsapp") whatsapp: String?,
        @Field("direct_apply") directApply: Int?,
        @Field("call_apply") callApply: Int?,
        @Field("message_apply") messageApply: Int?,
        @Field("whatsapp_apply") whatsappApply: Int,

        ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @DELETE("jobseeker/work-experience/{id}")
    fun deleteWorkExperience(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @GET("employer/applicants")
    fun getApplicants(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @GET("profile/visited-profiles")
    fun getVisitedProfile(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @GET("profile/shortlisted-profiles")
    fun getShortListedProfile(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @GET("profile/new-profiles")
    fun getNewProfile(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @DELETE("employer/job/{id}")
    fun deleteMyJobs(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @DELETE("profile/shortlisted-profiles/{id}")
    fun deleteShortListUser(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @POST("profile/shortlisted-profiles")
    @FormUrlEncoded
    fun shortListUser(
        @Header("Authorization") bearerToken: String,
        @Field("jobseeker_id") id: Int,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @POST("profile/visited-profiles")
    @FormUrlEncoded
    fun visitedUser(
        @Header("Authorization") bearerToken: String,
        @Field("jobseeker_id") id: Int,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @POST("jobseeker/visited-job")
    @FormUrlEncoded
    fun visitedJob(
        @Header("Authorization") bearerToken: String,
        @Field("job_id") id: Int,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @PATCH("employer/job-status-update/{id}")
    fun changeJobStateActivate(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>


    @Headers("Accept: application/json")
    @POST("jobseeker/family")
    @FormUrlEncoded
    fun addFamilyMember(
        @Header("Authorization") bearerToken: String,
        @Field("name") name: String,
        @Field("relation") relation: String,
        @Field("living_with_you") livingWithYou: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @PATCH("jobseeker/family/{id}")
    @FormUrlEncoded
    fun updateFamilyMember(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("relation") relation: String,
        @Field("living_with_you") livingWithYou: String,
    ): Call<JsonObject?>


    @Headers("Accept: application/json")
    @DELETE("jobseeker/family/{id}")
    fun deleteFamilyMember(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>

    @GET("jobseeker/profile")
    fun getUserProfile(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("employer/profile")
    fun getUserEmployerProfile(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>


    @GET("profile/recent-profiles")
    fun getRecentProfiles(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("employer/dashboard")
    fun getUserEmployeeDashBoard(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("popular-categories")
    fun getPopularCategory(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("employer/job")
    fun getAllMyJobs(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("data/all")
    fun getAllFilterList(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("data/education")
    fun getEducation(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("data/state")
    fun getStateList(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>


    @GET("data/category")
    fun getCategoryList(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("jobseeker/applied-jobs")
    fun getAppliedJob(
        @Header("Authorization") bearerToken: String,
        @Query("sort_order") sortOrder: String,
        @Query("sort_field") sortField: String,
    ): Call<JsonObject?>

    @GET("message/channel")
    fun getChannelList(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("message/message/{id}")
    fun getChatHistory(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>


    @GET("jobseeker/dashboard")
    fun getDashboardCounter(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>


    @GET("data/cms")
    fun getCMS(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>


    @GET("data/sub-category")
    fun getSubCategoryList(
        @Header("Authorization") bearerToken: String,
        @Query("category_id") categoryId: String,
    ): Call<JsonObject?>


    @GET("data/city")
    fun getCityList(
        @Header("Authorization") bearerToken: String,
        @Query("state_id") stateId: String,
    ): Call<JsonObject?>

    @POST("message/channel")
    @FormUrlEncoded
    fun createChannelList(
        @Header("Authorization") bearerToken: String,
        @Field("to_id") stateId: String,
    ): Call<JsonObject?>

    @Multipart
    @POST("message/message/{id}")
    fun sendMessage(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
        @Part("message") message: RequestBody?,
        @Part attachment: MultipartBody.Part?,
    ): Call<JsonObject?>

    @DELETE("message/channel/{id}")
    fun deleteChannel(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>

    @DELETE("message/message/{id}")
    fun deleteParticularMessage(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
    ): Call<JsonObject?>

    @DELETE("message/clear_chat/{id}")
    fun clearAllChat(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
        @Query("to_id") toId: Int,
    ): Call<JsonObject?>


    @POST("jobseeker/photo")
    @Multipart
    fun updateProfilePicture(
        @Header("Authorization") bearerToken: String, @Part avatar: MultipartBody.Part?,
    ): Call<JsonObject?>

    @POST("employer/logo")
    @Multipart
    fun updateEmployerLogo(
        @Header("Authorization") bearerToken: String,
        @Part logo: MultipartBody.Part?,
    ): Call<JsonObject?>


    @POST("employer/banner")
    @Multipart
    fun updateEmployerBanner(
        @Header("Authorization") bearerToken: String, @Part banner: MultipartBody.Part?,
    ): Call<JsonObject?>


    @POST("employer/interior-photo")
    @Multipart
    fun updateEmployerInteriorPhoto(
        @Header("Authorization") bearerToken: String, @Part img: MultipartBody.Part?,
    ): Call<JsonObject?>

    @POST("employer/exterior-photo")
    @Multipart
    fun updateEmployerExteriorPhoto(
        @Header("Authorization") bearerToken: String, @Part img: MultipartBody.Part?,
    ): Call<JsonObject?>


    @POST("jobseeker/resume")
    @Multipart
    fun updateResume(
        @Header("Authorization") bearerToken: String,
        @Part resume: MultipartBody.Part?,
    ): Call<JsonObject?>

    @GET("jobs")
    fun findJobList(
        @Header("Authorization") bearerToken: String,
        @Query("category_id") categoryId: String,
        @Query("sub_category_id") subCategoryId: String,
        @Query("type") jobType: String,
        @Query("state_id") stateId: String,
        @Query("city_id") cityId: String,
        @Query("gender") gender: String,
        @Query("salary") salary: String,
        @Query("zip") zip: String,
        @Query("business_type") businessType: String,
        @Query("company_look") companyLook: String,
        @Query("current_page") currentPage: String,
        @Query("sort_order") sortOrder: String,
        @Query("sort_field") sortField: String,
    ): Call<JsonObject?>

    @GET("profile/search")
    fun findPerfectEmployee(
        @Header("Authorization") bearerToken: String,
        @Query("category_id") categoryId: String,
        @Query("sub_category_id") subCategoryId: String,
        @Query("type") jobType: String,
        @Query("state_id") stateId: String,
        @Query("city_id") cityId: String,
        @Query("current_page") currentPage: String,
    ): Call<JsonObject?>


    @GET("jobseeker/visited-jobs")
    fun getRecentVisitedJobs(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("recent-jobs")
    fun getNewJobs(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("jobseeker/favorite-jobs")
    fun getFavouriteJobs(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @GET("recommended-jobs")
    fun getProfileMatchingJobs(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>


    @DELETE("delete-account")
    fun deleteAccount(
        @Header("Authorization") bearerToken: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @POST("jobseeker/apply-job")
    @FormUrlEncoded
    fun applyTheJob(
        @Header("Authorization") bearerToken: String,
        @Field("job_id") jobId: String,
    ): Call<JsonObject?>

    @Headers("Accept: application/json")
    @GET("company/{company_id}")
    fun getCompanyDetails(
        @Header("Authorization") bearerToken: String,
        @Path("company_id") companyId: String,
    ): Call<JsonObject?>


}
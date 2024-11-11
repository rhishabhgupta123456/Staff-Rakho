package com.staffrakho.networkModel.authNetworkPannel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.SingleLiveEvent


class AuthViewModel : ViewModel() {

    private val authRepositoryImplement: AuthRepositoryImplement = AuthRepositoryImplement()



    fun login(
        emailOrPhone: String,
        getPassword: String,
        userType : String
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return authRepositoryImplement.login(emailOrPhone, getPassword ,userType)
    }

    fun register(
        name: String,
        email: String,
        mobile: String,
        password: String,
        gender: String,
        dob: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return authRepositoryImplement.register(name, email ,mobile , password,gender,dob)
    }
    fun employerRegister(
        name: String,
        email: String,
        mobile: String,
        password: String,
        cName: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return authRepositoryImplement.employerRegister(name, email ,mobile , password,cName)
    }


    fun forgetPassword(
        username: String,
        userType: String,
      ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return authRepositoryImplement.forgetPassword(username,userType)
    }


    fun resetPassword(
        username: String,
        userType: String,
        password: String,
        passwordConfirmation: String,
        otp: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return authRepositoryImplement.resetPassword(username,userType,password,passwordConfirmation,otp)
    }

    fun socialLogIn(
        name: String,
        email: String,
        socialType: String,
        socialId: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return authRepositoryImplement.socialLogIn(name, email, socialType, socialId, userType)
    }

}
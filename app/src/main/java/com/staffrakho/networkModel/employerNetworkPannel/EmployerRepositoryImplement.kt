package com.staffrakho.networkModel.employerNetworkPannel

import com.google.gson.JsonObject
import com.staffrakho.networkModel.ApiInterface
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.SingleLiveEvent
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EmployerRepositoryImplement {


    private val apiInterface: ApiInterface = ApiInterface()


    fun getUserEmployerProfile(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getUserEmployerProfile(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data


    }

    fun getUserEmployerDashBoard(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getUserEmployeeDashBoard(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data


    }

    fun getAllMyJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getAllMyJobs(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data


    }

    fun updateEmployerLogo(
        bearerToken: String,
        profilePicture: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateEmployerLogo(bearerToken, profilePicture)
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>,
                ) {
                    try {

                        if (response.body() != null) {
                            obj.setResponseAlt(response.body()!!)
                            obj.setIsErrorAlt(false)
                        } else {
                            obj.setMessageAlt("Server error")
                            obj.setIsErrorAlt(true)
                        }
                        data.value = obj
                    } catch (e: Exception) {
                        obj.setIsErrorAlt(true)
                        obj.setMessageAlt(e.message.toString())
                        data.value = obj
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject?>,
                    t: Throwable,
                ) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(t.message.toString())
                    data.value = obj
                }

            })

        return data

    }


    fun updateEmployerBanner(
        bearerToken: String,
        profilePicture: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateEmployerBanner(bearerToken, profilePicture)
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>,
                ) {
                    try {

                        if (response.body() != null) {
                            obj.setResponseAlt(response.body()!!)
                            obj.setIsErrorAlt(false)
                        } else {
                            obj.setMessageAlt("Server error")
                            obj.setIsErrorAlt(true)
                        }
                        data.value = obj
                    } catch (e: Exception) {
                        obj.setIsErrorAlt(true)
                        obj.setMessageAlt(e.message.toString())
                        data.value = obj
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject?>,
                    t: Throwable,
                ) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(t.message.toString())
                    data.value = obj
                }

            })

        return data

    }

    fun updateEmployerInteriorPhoto(
        bearerToken: String,
        profilePicture: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateEmployerInteriorPhoto(bearerToken, profilePicture)
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>,
                ) {
                    try {

                        if (response.body() != null) {
                            obj.setResponseAlt(response.body()!!)
                            obj.setIsErrorAlt(false)
                        } else {
                            obj.setMessageAlt("Server error")
                            obj.setIsErrorAlt(true)
                        }
                        data.value = obj
                    } catch (e: Exception) {
                        obj.setIsErrorAlt(true)
                        obj.setMessageAlt(e.message.toString())
                        data.value = obj
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject?>,
                    t: Throwable,
                ) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(t.message.toString())
                    data.value = obj
                }

            })

        return data

    }

    fun updateEmployerExteriorPhoto(
        bearerToken: String,
        profilePicture: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateEmployerExteriorPhoto(bearerToken, profilePicture)
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>,
                ) {
                    try {

                        if (response.body() != null) {
                            obj.setResponseAlt(response.body()!!)
                            obj.setIsErrorAlt(false)
                        } else {
                            obj.setMessageAlt("Server error")
                            obj.setIsErrorAlt(true)
                        }
                        data.value = obj
                    } catch (e: Exception) {
                        obj.setIsErrorAlt(true)
                        obj.setMessageAlt(e.message.toString())
                        data.value = obj
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject?>,
                    t: Throwable,
                ) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(t.message.toString())
                    data.value = obj
                }

            })

        return data

    }

    fun getAllFilterList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getAllFilterList(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun getEducation(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getEducation(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun getStateList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getStateList(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }


    fun getCategoryList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getCategoryList(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun getRecentProfiles(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getRecentProfiles(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun getSubCategoryList(
        bearerToken: String,
        categoryId: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getSubCategoryList(bearerToken, categoryId)
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>,
                ) {
                    try {

                        if (response.body() != null) {
                            obj.setResponseAlt(response.body()!!)
                            obj.setIsErrorAlt(false)
                        } else {
                            obj.setMessageAlt("Server error")
                            obj.setIsErrorAlt(true)
                        }
                        data.value = obj
                    } catch (e: Exception) {
                        obj.setIsErrorAlt(true)
                        obj.setMessageAlt(e.message.toString())
                        data.value = obj
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject?>,
                    t: Throwable,
                ) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(t.message.toString())
                    data.value = obj
                }

            })

        return data

    }

    fun getCityList(
        bearerToken: String,
        stateId: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getCityList(bearerToken, stateId).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun createChannelList(
        bearerToken: String,
        receiverID: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.createChannelList(bearerToken, receiverID).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

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
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.addJobs(
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

        ).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

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
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateMyJobs(
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

        ).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }


    fun deleteMyJobs(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.deleteMyJobs(bearerToken, jobId).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun deleteShortListUser(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.deleteShortListUser(bearerToken, jobId)
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>,
                ) {
                    try {

                        if (response.body() != null) {
                            obj.setResponseAlt(response.body()!!)
                            obj.setIsErrorAlt(false)
                        } else {
                            obj.setMessageAlt("Server error")
                            obj.setIsErrorAlt(true)
                        }
                        data.value = obj
                    } catch (e: Exception) {
                        obj.setIsErrorAlt(true)
                        obj.setMessageAlt(e.message.toString())
                        data.value = obj
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject?>,
                    t: Throwable,
                ) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(t.message.toString())
                    data.value = obj
                }

            })

        return data

    }

    fun shortListUser(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.shortListUser(bearerToken, jobId).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun visitedUser(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.visitedUser(bearerToken, jobId).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun changeJobStateActivate(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.changeJobStateActivate(bearerToken, jobId)
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>,
                ) {
                    try {

                        if (response.body() != null) {
                            obj.setResponseAlt(response.body()!!)
                            obj.setIsErrorAlt(false)
                        } else {
                            obj.setMessageAlt("Server error")
                            obj.setIsErrorAlt(true)
                        }
                        data.value = obj
                    } catch (e: Exception) {
                        obj.setIsErrorAlt(true)
                        obj.setMessageAlt(e.message.toString())
                        data.value = obj
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject?>,
                    t: Throwable,
                ) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(t.message.toString())
                    data.value = obj
                }

            })

        return data

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
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.findPerfectEmployee(
            bearerToken,
            categoryId,
            subCategoryId,
            jobType,
            stateId,
            cityId,
            currentPage
        )
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>,
                ) {
                    try {

                        if (response.body() != null) {
                            obj.setResponseAlt(response.body()!!)
                            obj.setIsErrorAlt(false)
                        } else {
                            obj.setMessageAlt("Server error")
                            obj.setIsErrorAlt(true)
                        }
                        data.value = obj
                    } catch (e: Exception) {
                        obj.setIsErrorAlt(true)
                        obj.setMessageAlt(e.message.toString())
                        data.value = obj
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject?>,
                    t: Throwable,
                ) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(t.message.toString())
                    data.value = obj
                }

            })

        return data

    }

    fun getApplicantsList(
        bearerToken: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getApplicants(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun getVisitedProfile(
        bearerToken: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getVisitedProfile(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun getShortListedProfile(
        bearerToken: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getShortListedProfile(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }

    fun getNewProfile(
        bearerToken: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getNewProfile(bearerToken).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

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
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.editEmployerProfile(
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
        ).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data

    }


}
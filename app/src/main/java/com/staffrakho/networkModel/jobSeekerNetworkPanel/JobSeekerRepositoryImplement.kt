package com.staffrakho.networkModel.jobSeekerNetworkPanel

import com.google.gson.JsonObject
import com.staffrakho.networkModel.ApiInterface
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.SingleLiveEvent
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JobSeekerRepositoryImplement {


    private val apiInterface: ApiInterface = ApiInterface()

    fun savedTheJob(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.savedTheJob(bearerToken, jobId)
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

    fun unSavedJob(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.unSavedJob(bearerToken, jobId)
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

    fun visitedJob(
        bearerToken: String,
        jobId: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.visitedJob(bearerToken, jobId).enqueue(object : Callback<JsonObject?> {
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

    fun getUserProfile(
        bearerToken: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getUserProfile(bearerToken).enqueue(object : Callback<JsonObject?> {
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


    fun getPopularCategory(
        bearerToken: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getPopularCategory(bearerToken).enqueue(object : Callback<JsonObject?> {
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

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.editProfile(
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

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.addWorkExperience(
            bearerToken,
            categoryId,
            subCategoryId,
            fromDate,
            toDate,
            reasonForChange,
            shopName,
            role,
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

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateWorkExperience(
            bearerToken,
            id,
            categoryId,
            subCategoryId,
            fromDate,
            toDate,
            reasonForChange,
            shopName,
            role,
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


    fun deleteWorkExperience(
        bearerToken: String,
        id: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.deleteWorkExperience(
            bearerToken,
            id,
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


    fun addFamilyMember(
        bearerToken: String,
        name: String,
        relation: String,
        livingWithYou: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.addFamilyMember(
            bearerToken,
            name,
            relation,
            livingWithYou,
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

    fun updateFamilyMember(
        bearerToken: String,
        id: Int,
        name: String,
        relation: String,
        livingWithYou: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateFamilyMember(
            bearerToken,
            id,
            name,
            relation,
            livingWithYou,
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


    fun deleteFamilyMember(
        bearerToken: String,
        id: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {

        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.deleteFamilyMember(
            bearerToken,
            id,
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


    fun getAppliedJob(
        bearerToken: String,
        sortOrder: String,
        sortField: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getAppliedJob(bearerToken, sortOrder, sortField)
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

    fun getChannelList(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getChannelList(bearerToken).enqueue(object : Callback<JsonObject?> {
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

    fun getChatHistory(bearerToken: String, id: Int): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getChatHistory(bearerToken, id).enqueue(object : Callback<JsonObject?> {
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

    fun getDashboardCounter(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getDashboardCounter(bearerToken).enqueue(object : Callback<JsonObject?> {
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


    fun getCMS(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getCMS(bearerToken).enqueue(object : Callback<JsonObject?> {
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


    fun getRecentVisitedJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getRecentVisitedJobs(bearerToken).enqueue(object : Callback<JsonObject?> {
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


    fun getNewJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getNewJobs(bearerToken).enqueue(object : Callback<JsonObject?> {
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


    fun getFavouriteJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getFavouriteJobs(bearerToken).enqueue(object : Callback<JsonObject?> {
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

    fun getProfileMatchingJobs(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getProfileMatchingJobs(bearerToken).enqueue(object : Callback<JsonObject?> {
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

    fun deleteAccount(bearerToken: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.deleteAccount(bearerToken).enqueue(object : Callback<JsonObject?> {
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

    fun applyTheJob(bearerToken: String, jobId: String): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.applyTheJob(bearerToken, jobId).enqueue(object : Callback<JsonObject?> {
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

    fun getCompanyDetails(
        bearerToken: String,
        companyId: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.getCompanyDetails(bearerToken, companyId)
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


    fun updateProfilePicture(
        bearerToken: String,
        profilePicture: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateProfilePicture(bearerToken, profilePicture)
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

    fun sendMessage(
        bearerToken: String,
        id: Int,
        message: RequestBody?,
        attachment: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.sendMessage(bearerToken, id, message, attachment)
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

    fun deleteChannel(
        bearerToken: String,
        id: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.deleteChannel(bearerToken, id).enqueue(object : Callback<JsonObject?> {
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

    fun clearAllChat(
        bearerToken: String,
        id: Int,
        toID: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.clearAllChat(bearerToken, id, toID).enqueue(object : Callback<JsonObject?> {
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

    fun deleteParticularMessage(
        bearerToken: String,
        id: Int,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.deleteParticularMessage(bearerToken, id)
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

    fun updateResume(
        bearerToken: String,
        updateResume: MultipartBody.Part?,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.updateResume(bearerToken, updateResume)
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
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.findJobList(
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


    fun emailUpdateRequest(
        bearerToken: String,
        email: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.emailUpdateRequest(bearerToken, email, userType)
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

    fun changePassword(
        bearerToken: String,
        currentPassword: String,
        password: String,
        passwordConfirmation: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.changePassword(
            bearerToken,
            currentPassword,
            password,
            passwordConfirmation
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


    fun emailUpdate(
        bearerToken: String,
        email: String,
        otp: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.emailUpdate(bearerToken, email, otp, userType)
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


    fun mobileUpdateRequest(
        bearerToken: String,
        mobile: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.mobileUpdateRequest(bearerToken, mobile, userType)
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

    fun mobileUpdate(
        bearerToken: String,
        mobile: String,
        otp: String,
        userType: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.mobileUpdate(bearerToken, mobile, otp, userType)
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

}
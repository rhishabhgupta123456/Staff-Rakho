package com.staffrakho.utility

import android.content.Context
import android.content.SharedPreferences

class SessionManager(var context: Context) {

    private var sharedPreferences: SharedPreferences =context.getSharedPreferences("staffAppTempStorage", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setUserType(userType: String) {
        editor.putString(AppConstant.USER_TYPE, userType)
        editor.apply()
    }

    fun getUserType():String{
        return sharedPreferences.getString(AppConstant.USER_TYPE,"").toString()
    }

    fun setBearerToken(token: String) {
        editor.putString("token", token)
        editor.apply()
    }

    fun getBearerToken(): String {
        return "Bearer " + sharedPreferences.getString("token","").toString()
    }

    fun setUserID(userId: String) {
        editor.putString("userId", userId)
        editor.apply()
    }

    fun getUserId(): String {
        return  sharedPreferences.getString("userId","").toString()
    }

    fun setSourceFragment(sourceFragment: String) {
        editor.putString(AppConstant.SOURCE_FRAGMENT, sourceFragment)
        editor.apply()
    }

    fun getSourceFragment(): String {
        return sharedPreferences.getString(AppConstant.SOURCE_FRAGMENT,"").toString()
    }

    fun setJobId( id : Int) {
        editor.putInt(AppConstant.JOB_ID, id)
        editor.apply()
    }

    fun getJobId(): Int {
        return sharedPreferences.getInt(AppConstant.JOB_ID,0)
    }

    fun setUserEmail(email: String?) {
        editor.putString("email", email)
        editor.apply()
    }

    fun getUserEmail(): String {
        return sharedPreferences.getString("email", "").toString()
    }

    fun setUserPhone(phone: String?) {
        editor.putString("phone", phone)
        editor.apply()
    }

    fun getUserPhone(): String {
        return sharedPreferences.getString("phone", "").toString()
    }



    private var languageSharedPreferences: SharedPreferences =context.getSharedPreferences("staffAppLanguageStorage", Context.MODE_PRIVATE)
    private var languageEditor: SharedPreferences.Editor = languageSharedPreferences.edit()

    fun setLanguage(language: String) {
        languageEditor.putString(AppConstant.LANGUAGE, language)
        languageEditor.apply()
    }

    fun getLanguage(): String {
        return languageSharedPreferences.getString(AppConstant.LANGUAGE,"English").toString()
    }


}
package com.staffrakho.utility

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.staffrakho.R
import com.staffrakho.activity.IdentityActivity
import com.staffrakho.networkModel.BaseResponse
import java.util.Locale

open class BaseActivity : AppCompatActivity() {


    fun  sessionEndDialog(msg:String?){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        logOutAccount()

        val sessionEndDialog= Dialog(this)
        sessionEndDialog.setCancelable(false)
        sessionEndDialog.setContentView(R.layout.alertbox_error)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(sessionEndDialog.window!!.attributes)

        sessionEndDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        sessionEndDialog.window!!.attributes = layoutParams
        val tvTitle: TextView =sessionEndDialog.findViewById(R.id.tv_title)
        val btnOk: TextView =sessionEndDialog.findViewById(R.id.btn_ok)
        tvTitle.text=msg

        btnOk.setOnClickListener {
            logOutAccount()
        }

        sessionEndDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    //    sessionEndDialog.show()
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun  alertErrorDialog(msg:String?){
        val alertErrorDialog= Dialog(this)
        alertErrorDialog.setCancelable(false)
        alertErrorDialog.setContentView(R.layout.alertbox_error)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertErrorDialog.window!!.attributes)

        Log.e("Error Message",msg.toString())
        alertErrorDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        alertErrorDialog.window!!.attributes = layoutParams

        val tvTitle: TextView =alertErrorDialog.findViewById(R.id.tv_title)
        val btnOk: TextView =alertErrorDialog.findViewById(R.id.btn_ok)
        tvTitle.text=msg

        btnOk.setOnClickListener {
            alertErrorDialog.dismiss()
        }

        alertErrorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertErrorDialog.show()
    }

    private fun logOutAccount(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("staffAppTempStorage", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.clear()
        editor.apply()
        val intent = Intent(this , IdentityActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


    fun checkResponse(jsonObject: BaseResponse<JsonObject>): JsonObject? {

        if (!jsonObject.isIsError) {
            if (jsonObject.response != null) {
                try {
                    val jsonObjectData: JsonObject = jsonObject.response!!
                    val status = jsonObjectData["success"].asBoolean

                    if (status) {
                        return jsonObjectData
                    } else {
                        alertErrorDialog(jsonObjectData["message"].asString)
                    }

                } catch (e: Exception) {
                    alertErrorDialog("Exception : $e")
                }

            }
        } else {
            sessionEndDialog(jsonObject.message)
        }
        return null
    }

    fun checkFieldSting(json: JsonElement?): String {
        return if (json != null && !json.isJsonNull) {
            json.asString
        } else {
            ""
        }
    }

    fun checkFieldObject(json: JsonElement?): JsonObject? {
        return if (json != null && !json.isJsonNull) {
            json.asJsonObject
        } else {
            null
        }
    }

    fun checkFieldArray(json: JsonElement?): JsonArray? {
        return if (json != null && !json.isJsonNull) {
            json.asJsonArray
        } else {
            null
        }
    }


}
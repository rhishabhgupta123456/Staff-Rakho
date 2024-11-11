package com.staffrakho.utility

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.res.Configuration
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.staffrakho.R
import com.staffrakho.activity.IdentityActivity
import com.staffrakho.networkModel.BaseResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


open class BaseFragment : Fragment() {


    fun sessionEndDialog(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        logOutAccount()

        val sessionEndDialog = Dialog(requireActivity())
        sessionEndDialog.setCancelable(false)
        sessionEndDialog.setContentView(R.layout.alertbox_error)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(sessionEndDialog.window!!.attributes)

        sessionEndDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        sessionEndDialog.window!!.attributes = layoutParams
        val tvTitle: TextView = sessionEndDialog.findViewById(R.id.tv_title)
        val btnOk: TextView = sessionEndDialog.findViewById(R.id.btn_ok)
        tvTitle.text = msg

        btnOk.setOnClickListener {
            logOutAccount()
        }

        sessionEndDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //     sessionEndDialog.show()
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    fun alertErrorDialog(msg: String?) {
        val alertErrorDialog = Dialog(requireActivity())
        alertErrorDialog.setCancelable(false)
        alertErrorDialog.setContentView(R.layout.alertbox_error)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertErrorDialog.window!!.attributes)

        Log.e("Error Message", msg.toString())
        alertErrorDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        alertErrorDialog.window!!.attributes = layoutParams

        val tvTitle: TextView = alertErrorDialog.findViewById(R.id.tv_title)
        val btnOk: TextView = alertErrorDialog.findViewById(R.id.btn_ok)
        tvTitle.text = msg

        btnOk.setOnClickListener {
            alertErrorDialog.dismiss()
        }

        alertErrorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertErrorDialog.show()
    }

    fun closeAppAlertDialog() {
        val alertErrorDialog = Dialog(requireActivity())
        alertErrorDialog.setCancelable(false)
        alertErrorDialog.setContentView(R.layout.alertbox_closeapp)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertErrorDialog.window!!.attributes)

        alertErrorDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        alertErrorDialog.window!!.attributes = layoutParams

        val tvTitle: TextView = alertErrorDialog.findViewById(R.id.tv_title)
        val btnYes: TextView = alertErrorDialog.findViewById(R.id.btnYes)
        val btnNo: TextView = alertErrorDialog.findViewById(R.id.btnNo)
        tvTitle.text = getString(R.string.are_you_want_to_close_the_app)

        btnYes.setOnClickListener {
            requireActivity().finish()
            alertErrorDialog.dismiss()
        }

        btnNo.setOnClickListener {
            alertErrorDialog.dismiss()
        }

        alertErrorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertErrorDialog.show()
    }

    fun logOutAccount() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("staffAppTempStorage", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.clear()
        editor.apply()
        val intent = Intent(requireActivity(), IdentityActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
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

    fun openDatePicker(callback: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val formattedDate = dateFormatter.format(selectedDate.time)
                callback(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
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

    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireActivity().resources.updateConfiguration(
            config,
            requireActivity().resources.displayMetrics
        )
    }

    @SuppressLint("ObsoleteSdkInt")
    fun htmlToPlainText(html: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html).toString()
        }
    }

}
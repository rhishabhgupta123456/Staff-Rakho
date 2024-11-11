package com.staffrakho.fragment.bottomSheetFilter

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.staffrakho.R
import com.staffrakho.activity.IdentityActivity
import com.staffrakho.dataModel.AllFilterList
import com.staffrakho.dataModel.CityList
import com.staffrakho.dataModel.StateList
import com.staffrakho.databinding.FragmentBottomSheetEmployerProfileEdittDialogBinding
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch
import java.util.Calendar


class BottomSheetEmployerProfileEditDialog(
    private var companyName: String,
    private var contactNumber: String,
    private var address: String,
    private var state: String,
    private var city: String,
    private var zip: String,
    private var businessType: String,
    private var floor: String,
    private var companyLook: String,
    private var fromTime: String,
    private var toTime: String,
    private var fromTimeBoys: String,
    private var toTimeBoys: String,
    private var fromTimeGirls: String,
    private var toTimeGirls: String,
    private var openingDays: String,
) : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var binding: FragmentBottomSheetEmployerProfileEdittDialogBinding
    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel
    var selectBusinessTypeId: String? = null
    var companyLookId: String? = null
    var selectedStateId: String? = null
    var selectCityId: String? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.background = resources.getDrawable(R.drawable.bottom_white_layout, null)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isHideable = false
            behavior.isDraggable = false
        }
        dialog.setCancelable(false)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomSheetEmployerProfileEdittDialogBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        sessionManager = SessionManager(requireContext())
        employerViewModel = ViewModelProvider(this)[EmployerViewModel::class.java]


        binding.etCompanyName.setText(companyName)
        binding.etContactNumber.text = contactNumber
        binding.etAddress.setText(address)
        binding.etZip.setText(zip)
        binding.etFloor.setText(floor)
        binding.etFromTime.text = fromTime
        binding.etToTime.text = toTime
        binding.etFromTimeBoys.text = fromTimeBoys
        binding.etToTimeBoys.text = toTimeBoys
        binding.etFromTimeGirls.text = fromTimeGirls
        binding.etToTimeGirls.text = toTimeGirls

        if (openingDays.isNotEmpty() && openingDays != "null") {
            setOpeningDays()
        }

        binding.btnApplyFilter.setOnClickListener(this)
        binding.btCloseFilter.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)
        binding.etFromTime.setOnClickListener(this)
        binding.etToTime.setOnClickListener(this)
        binding.etFromTimeBoys.setOnClickListener(this)
        binding.etToTimeBoys.setOnClickListener(this)
        binding.etFromTimeGirls.setOnClickListener(this)
        binding.etToTimeGirls.setOnClickListener(this)


        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            getAllFilterList()
            getStateList()
        }
    }

    private fun setOpeningDays() {
        val arr = openingDays.split(",")

        Log.e("Opening Days", arr.toString())

        for (day in arr) {

            when (day) {
                "SUN" -> {
                    binding.btDaySun.isChecked = true
                }

                "MON" -> {
                    binding.btDayMon.isChecked = true
                }

                "TUE" -> {
                    binding.btDayTue.isChecked = true
                }

                "WED" -> {
                    binding.btDayWed.isChecked = true
                }

                "THU" -> {
                    binding.btDayThu.isChecked = true
                }

                "FRI" -> {
                    binding.btDayFri.isChecked = true
                }

                "SAT" -> {
                    binding.btDaySat.isChecked = true
                }


            }

        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnApplyFilter -> {
                if (checkValidation()) {
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    }else{
                        updateProfile()
                    }

                }
            }

            R.id.btCloseFilter -> {
                dismiss()
            }

            R.id.btnClose -> {
                dismiss()
            }

            R.id.etFromTime -> {
                openTimePicker { selectedTime ->
                    binding.etFromTime.text = selectedTime
                }
            }

            R.id.etToTime -> {
                openTimePicker { selectedTime ->
                    binding.etToTime.text = selectedTime
                }
            }

            R.id.etFromTimeBoys -> {
                openTimePicker { selectedTime ->
                    binding.etFromTimeBoys.text = selectedTime
                }
            }

            R.id.etToTimeBoys -> {
                openTimePicker { selectedTime ->
                    binding.etToTimeBoys.text = selectedTime
                }
            }

            R.id.etFromTimeGirls -> {
                openTimePicker { selectedTime ->
                    binding.etFromTimeGirls.text = selectedTime
                }
            }

            R.id.etToTimeGirls -> {
                openTimePicker { selectedTime ->
                    binding.etToTimeGirls.text = selectedTime
                }
            }

        }
    }

    @SuppressLint("DefaultLocale")
    private fun openTimePicker(callback: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->
                val amPm = if (selectedHour < 12) "AM" else "PM"
                val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                val formattedTime =
                    String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm)
                callback(formattedTime)
            }, hour, minute, false // Set is24HourView to false
        )

        timePickerDialog.show()
    }


    // This Function is used for check all filed validation
    private fun checkValidation(): Boolean {
        if (binding.etCompanyName.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_company_name))
            return false
        } else if (binding.etAddress.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_address))
            return false
        } else if (selectedStateId == null) {
            alertErrorDialog(getString(R.string.please_select_the_state))
            return false
        } else if (selectCityId == null) {
            alertErrorDialog(getString(R.string.please_select_the_city))
            return false
        } else if (binding.etZip.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_zip_code))
            return false
        } else if (selectBusinessTypeId == null) {
            alertErrorDialog(getString(R.string.please_select_the_business_type))
            return false
        } else if (binding.etFloor.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_select_the_floor))
            return false
        } else if (companyLookId == null) {
            alertErrorDialog(getString(R.string.please_select_the_company_look))
            return false
        } else if (!binding.btDaySun.isChecked && !binding.btDayMon.isChecked && !binding.btDayTue.isChecked && !binding.btDayWed.isChecked && !binding.btDayThu.isChecked && !binding.btDayFri.isChecked && !binding.btDaySat.isChecked) {
            alertErrorDialog(getString(R.string.please_fill_the_office_opening_days))
            return false
        } else if (binding.etFromTime.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_office_opening_time))
            return false
        } else if (binding.etToTime.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_office_close_time))
            return false
        } else if (binding.etFromTimeBoys.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_office_opening_time_for_boys))
            return false
        } else if (binding.etToTimeBoys.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_office_close_time_for_boys))
            return false
        } else if (binding.etFromTimeGirls.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_office_opening_time_for_girls))
            return false
        } else if (binding.etToTimeGirls.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_office_close_time_girls))
            return false
        } else {
            return true
        }

    }

    // This Function is used for show error alert box
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

    // This Function is used for update user profile
    private fun updateProfile() {

        lifecycleScope.launch {
            employerViewModel.editEmployerProfile(
                sessionManager.getBearerToken(),
                binding.etCompanyName.text.toString(),
                binding.etAddress.text.toString(),
                selectCityId,
                binding.etZip.text.toString(),
                selectBusinessTypeId,
                selectedStateId,
                binding.etFloor.text.toString().toInt(),
                binding.etFromTime.text.toString(),
                binding.etToTime.text.toString(),
                binding.etFromTimeBoys.text.toString(),
                binding.etToTimeBoys.text.toString(),
                binding.etFromTimeGirls.text.toString(),
                binding.etToTimeGirls.text.toString(),
                binding.etContactNumber.text.toString(),
                companyLookId,
                getOpeningDays()
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            Toast.makeText(requireContext(), jsonObjectData["message"].asString, Toast.LENGTH_SHORT).show()
                            dismiss()
                            findNavController().navigate(R.id.openProfileScreen)
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    private fun getOpeningDays(): List<String> {
        val daysArray = mutableListOf<String>()

        if (binding.btDaySun.isChecked) {
            daysArray.add("SUN")
        }
        if (binding.btDayMon.isChecked) {
            daysArray.add("MON")
        }
        if (binding.btDayTue.isChecked) {
            daysArray.add("TUE")
        }
        if (binding.btDayWed.isChecked) {
            daysArray.add("WED")
        }
        if (binding.btDayThu.isChecked) {
            daysArray.add("THU")
        }
        if (binding.btDayFri.isChecked) {
            daysArray.add("FRI")
        }
        if (binding.btDaySat.isChecked) {
            daysArray.add("SAT")
        }

        return daysArray
    }


    // This Function is used for check api response
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
            Toast.makeText(requireContext(), jsonObject.message, Toast.LENGTH_SHORT).show()
            val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("staffAppTempStorage", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.clear()
            editor.apply()
            val intent = Intent(requireActivity() , IdentityActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }
        return null
    }

    // This Function is used for get All Filter List from database
    private fun getAllFilterList() {
        lifecycleScope.launch {

            employerViewModel.getAllFilterList(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            val allFilterList =
                                Gson().fromJson(jsonObjectData, AllFilterList::class.java)

                            // Set Business Type
                            val businessTypeList = mutableListOf("Select")
                            businessTypeList.addAll(allFilterList.data.businessType.map { it.name.toString() })
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                businessTypeList
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerBusinessType.adapter = adapter
                            binding.spinnerBusinessType.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        selectBusinessTypeId = if (position > 0) {
                                            allFilterList.data.businessType[position - 1].id
                                        } else {
                                            null
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }


                            val defaultIndex = businessTypeList.indexOf(businessType)
                            if (defaultIndex != -1) {
                                binding.spinnerBusinessType.setSelection(defaultIndex)
                                selectBusinessTypeId = if (defaultIndex > 0) {
                                    allFilterList.data.businessType[defaultIndex - 1].id
                                } else {
                                    null
                                }
                            }


                            // Set Company Look
                            val companyLookName = mutableListOf("Select")
                            companyLookName.addAll(allFilterList.data.companyLook.map { it.name.toString() })
                            val companyLookAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                companyLookName
                            )
                            companyLookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerCompanyLook.adapter = companyLookAdapter
                            binding.spinnerCompanyLook.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        companyLookId = if (position > 0) {
                                            allFilterList.data.companyLook[position - 1].id
                                        } else {
                                            null
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }


                            val defaultIndex2 = companyLookName.indexOf(companyLook)
                            if (defaultIndex2 != -1) {
                                binding.spinnerCompanyLook.setSelection(defaultIndex2)
                                companyLookId = if (defaultIndex2 > 0) {
                                    allFilterList.data.companyLook[defaultIndex2 - 1].id
                                } else {
                                    null
                                }
                            }

                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for get state List from database
    private fun getStateList() {
        var start = 0

        lifecycleScope.launch {

            employerViewModel.getStateList(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                            val stateList = Gson().fromJson(jsonObjectData, StateList::class.java)

                            val stateNames = mutableListOf("Select")
                            stateNames.addAll(stateList.data.map { it.state_name.toString() })

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                stateNames
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerState.adapter = adapter

                            binding.spinnerState.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        if (position > 0) {
                                            selectedStateId = stateList.data[position - 1].id
                                            if (start == 0) {
                                                start = 1
                                                getCityList(stateList.data[position - 1].id, 1)
                                            } else {
                                                getCityList(stateList.data[position - 1].id, 0)
                                            }
                                        } else {
                                            selectedStateId = null
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }


                            if (state != "") {
                                val defaultIndex = stateNames.indexOf(state)
                                if (defaultIndex != -1) {
                                    binding.spinnerState.setSelection(defaultIndex)
                                    selectedStateId = if (defaultIndex > 0) {
                                        stateList.data[defaultIndex - 1].id
                                    } else {
                                        null
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for get city List from database
    private fun getCityList(stateId: String, start: Int) {
        lifecycleScope.launch {

            Log.e("State Id", stateId)

            employerViewModel.getCityList(sessionManager.getBearerToken(), stateId)
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                            val cityList = Gson().fromJson(jsonObjectData, CityList::class.java)


                            val cityNames = mutableListOf("Select")
                            cityNames.addAll(cityList.data.map { it.city_name.toString() })
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                cityNames
                            )

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerCity.adapter = adapter

                            binding.spinnerCity.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        if (position > 0) {
                                            selectCityId = cityList.data[position - 1].id
                                        } else {
                                            selectCityId != null
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }

                            if (city != "" && start == 1) {
                                Log.e("city", city)
                                Log.e("Sub Category List", cityNames.toString())

                                val defaultIndex = cityNames.indexOf(city)
                                Log.e("Position", defaultIndex.toString())
                                if (defaultIndex != -1) {
                                    binding.spinnerCity.setSelection(defaultIndex)
                                    selectCityId = if (defaultIndex > 0) {
                                        cityList.data[defaultIndex - 1].id
                                    } else {
                                        null
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

}
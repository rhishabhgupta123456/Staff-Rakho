package com.staffrakho.fragment.bottomSheetFilter


import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import android.widget.CheckBox
import android.widget.CompoundButton
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
import com.staffrakho.dataModel.CategoryList
import com.staffrakho.dataModel.CityList
import com.staffrakho.dataModel.EducationList
import com.staffrakho.dataModel.MyJobs
import com.staffrakho.dataModel.StateList
import com.staffrakho.dataModel.SubCategoryList
import com.staffrakho.databinding.BottomSheetJobBinding
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.SessionManager
import com.staffrakho.utility.ValidationData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BottomSheetJobDialog(private var jobDataModel: MyJobs?) :
    BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var binding: BottomSheetJobBinding
    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel
    private var selectedCategoryId: String? = null
    private var profileTypeId: String? = null
    private var jobTypeId: String? = null
    private var selectedGenderId: String? = null
    private var selectedSubCategoryId: String? = null
    private var experienceId: String? = null
    private var statusId: String? = null
    private var selectedStateId: String? = null
    private var selectCityId: String? = null
    private var selectedSalaryRange: String? = null
    private var selectedEducation: String? = null

    private var isDuration: Int? = null


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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetJobBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        sessionManager = SessionManager(requireContext())
        employerViewModel = ViewModelProvider(this)[EmployerViewModel::class.java]

        binding.btnApplyFilter.setOnClickListener(this)
        binding.btCloseFilter.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)
        binding.tvLastDate.setOnClickListener(this)


        binding.isShotTermJobCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.durationHead.visibility = View.VISIBLE
                binding.durationSelectBox.visibility = View.VISIBLE
            } else {
                binding.durationHead.visibility = View.GONE
                binding.durationSelectBox.visibility = View.GONE
                isDuration = null
            }
        }



        if (jobDataModel != null) {
            binding.etTitle.setText(jobDataModel!!.title)
            binding.etDescription.setText(jobDataModel!!.description)
            binding.etRolesResponsibilities.setText(jobDataModel!!.roles)
            binding.etAddress.setText(jobDataModel!!.address)
            binding.etNumberOfVacancy.setText(jobDataModel!!.number_of_vacancy)
            binding.tvLastDate.text = jobDataModel!!.last_date
            binding.btnApplyFilter.text = getString(R.string.update)
            binding.etPinCode.setText(jobDataModel!!.pin_code)
            binding.etSkillRequired.setText(jobDataModel!!.required_skills)
            binding.etPreferTime.setText(jobDataModel!!.preferred_call_time)
            binding.etPhone.setText(jobDataModel!!.phone)
            binding.etEmail.setText(jobDataModel!!.email)
            binding.etWhatsAppNumber.setText(jobDataModel!!.whatsapp)

            binding.btCall.isChecked = jobDataModel!!.call_apply == 1
            binding.btWhatsApp.isChecked = jobDataModel!!.whatsapp_apply == 1
            binding.btDirectApply.isChecked = jobDataModel!!.direct_apply == 1
            binding.btMessage.isChecked = jobDataModel!!.message_apply == 1

        }

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            getCategoryList()
            getAllFilterList()
            getStateList()
            getEducationList()
        }


        selectStatusSpinner()
        setDurationSpinner()
    }

    private fun setDurationSpinner() {
        val status = ArrayList<String>()
        status.add("Select")
        status.add("1 Day")
        status.add("1 Week")
        status.add("1 Month")
        status.add("3 Months")
        status.add("6 Months")

        val statusAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            status
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDuration.adapter = statusAdapter
        binding.spinnerDuration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position > 0){
                        isDuration = position
                    }else{
                        isDuration = null
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (jobDataModel != null) {
            if (jobDataModel!!.status != "") {
                val defaultIndex = status.indexOf("")
                if (defaultIndex != -1) {
                    binding.spinnerDuration.setSelection(defaultIndex)
                    if (defaultIndex > 0){
                        isDuration = defaultIndex
                    }else{
                        isDuration = null
                    }
                }
            }
        }



    }

    private fun selectStatusSpinner() {
        val status = ArrayList<String>()
        status.add("Select")
        status.add("InActive")
        status.add("Active")

        val statusAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            status
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = statusAdapter
        binding.spinnerStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    statusId = if (status[position] == "Active") {
                        "1"
                    } else if (status[position] == "InActive") {
                        "0"
                    } else {
                        null
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (jobDataModel != null) {
            if (jobDataModel!!.status != "") {
                val value = if (jobDataModel!!.status == "0") {
                    "InActive"
                } else if (jobDataModel!!.status == "1") {
                    "Active"
                } else {
                    "Select"
                }

                val defaultIndex = status.indexOf(value)
                if (defaultIndex != -1) {
                    binding.spinnerStatus.setSelection(defaultIndex)
                    statusId = if (status[defaultIndex] == "Active") {
                        "1"
                    } else if (status[defaultIndex] == "InActive") {
                        "0"
                    } else {
                        null
                    }
                }
            }
        }


    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnApplyFilter -> {
                if (checkValidation()) {
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        if (jobDataModel != null) {
                            updateJobs()
                        } else {
                            addJobs()
                        }
                    }

                }

            }

            R.id.btCloseFilter -> {
                dismiss()
            }

            R.id.btnClose -> {
                dismiss()
            }

            R.id.tvLastDate -> {
                openDatePicker { selectedDate ->
                    binding.tvLastDate.text = selectedDate
                }
            }

        }

    }


    // This Function is used for  check all validation in all field
    private fun checkValidation(): Boolean {
        if (binding.etTitle.text.toString().isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_title))
            return false
        } else if (selectedCategoryId == null) {
            alertErrorDialog(getString(R.string.select_the_category))
            return false
        } else if (selectedSubCategoryId == null) {
            alertErrorDialog(getString(R.string.select_the_sub_category))
            return false
        } else if (jobTypeId == null) {
            alertErrorDialog(getString(R.string.select_the_job_type))
            return false
        } else if (experienceId == null) {
            alertErrorDialog(getString(R.string.select_the_minimum_required_experience))
            return false
        } else if (selectedEducation == null) {
            alertErrorDialog(getString(R.string.select_the_minimum_required_education))
            return false
        } else if (statusId == null) {
            alertErrorDialog(getString(R.string.select_the_job_status))
            return false
        } else if (binding.etSkillRequired.text.toString().isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_required_skill))
            return false
        } else if (binding.etAddress.text.toString().isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_address))
            return false
        } else if (selectedStateId == null) {
            alertErrorDialog(getString(R.string.select_the_state))
            return false
        } else if (selectCityId == null) {
            alertErrorDialog(getString(R.string.select_the_city))
            return false
        } else if (binding.etPinCode.text.toString().isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_pin_code))
            return false
        } else if (profileTypeId == null) {
            alertErrorDialog(getString(R.string.select_the_profile_type))
            return false
        } else if (selectedSalaryRange == null) {
            alertErrorDialog(getString(R.string.select_the_salary))
            return false;
        } else if (binding.etPhone.text.toString().isNotEmpty() && !ValidationData.isPhoneNumber(
                binding.etPhone.text.toString()
            )
        ) {
            alertErrorDialog(getString(R.string.fill_valid_phone))
            return false
        } else if (binding.etWhatsAppNumber.text.toString()
                .isNotEmpty() && !ValidationData.isPhoneNumber(binding.etWhatsAppNumber.text.toString())
        ) {
            alertErrorDialog(getString(R.string.fill_valid_whatsup_phone))
            return false
        } else if (binding.etEmail.text.toString()
                .isNotEmpty() && !ValidationData.isEmail(binding.etEmail.text.toString())
        ) {
            alertErrorDialog(getString(R.string.fill_valid_email))
            return false
        } else {
            return true
        }

    }

    // This Function is used for add job data
    private fun addJobs() {
        lifecycleScope.launch {

            employerViewModel.addJobs(
                sessionManager.getBearerToken(),
                binding.etTitle.text.toString(),
                binding.etDescription.text.toString(),
                binding.etRolesResponsibilities.text.toString(),
                selectedCategoryId.toString(),
                selectedSubCategoryId.toString(),
                binding.etRolesResponsibilities.text.toString(),
                binding.etAddress.text.toString(),
                jobTypeId.toString(),
                experienceId.toString(),
                binding.etNumberOfVacancy.text.toString(),
                selectedGenderId.toString(),
                statusId.toString(),
                binding.tvLastDate.text.toString(),
                selectedStateId.toString(),
                selectCityId.toString(),
                profileTypeId.toString(),
                selectedSalaryRange.toString(),
                binding.etPinCode.text.toString(),
                selectedEducation,
                binding.etSkillRequired.text.toString(),
                binding.etPreferTime.text.toString(),
                binding.etPhone.text.toString(),
                binding.etEmail.text.toString(),
                binding.etWhatsAppNumber.text.toString(),
                checkISChecked(binding.btDirectApply),
                checkISChecked(binding.btCall),
                checkISChecked(binding.btMessage),
                checkISChecked(binding.btWhatsApp),
                binding.etVillage.text.toString(),
                binding.etSubDistrict.text.toString(),
                isDuration
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            Toast.makeText(
                                requireContext(),
                                jsonObjectData["message"].asString,
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                            findNavController().navigate(R.id.openPostJobsScreen)
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    private fun checkISChecked(button: CheckBox): Int {
        return if (button.isChecked) {
            1
        } else {
            0
        }
    }

    // This Function is used for Update job data
    private fun updateJobs() {
        lifecycleScope.launch {

            employerViewModel.updateMyJobs(
                sessionManager.getBearerToken(),
                jobDataModel!!.id,
                binding.etTitle.text.toString(),
                binding.etDescription.text.toString(),
                binding.etRolesResponsibilities.text.toString(),
                selectedCategoryId.toString(),
                selectedSubCategoryId.toString(),
                binding.etRolesResponsibilities.text.toString(),
                binding.etAddress.text.toString(),
                jobTypeId.toString(),
                experienceId.toString(),
                binding.etNumberOfVacancy.text.toString(),
                selectedGenderId.toString(),
                statusId.toString(),
                binding.tvLastDate.text.toString(),
                selectedStateId.toString(),
                selectCityId.toString(),
                profileTypeId.toString(),
                selectedSalaryRange.toString(),
                binding.etPinCode.text.toString(),
                selectedEducation,
                binding.etSkillRequired.text.toString(),
                binding.etPreferTime.text.toString(),
                binding.etPhone.text.toString(),
                binding.etEmail.text.toString(),
                binding.etWhatsAppNumber.text.toString(),
                checkISChecked(binding.btDirectApply),
                checkISChecked(binding.btCall),
                checkISChecked(binding.btMessage),
                checkISChecked(binding.btWhatsApp),
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            Toast.makeText(
                                requireContext(),
                                jsonObjectData["message"].asString,
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                            findNavController().navigate(R.id.openPostJobsScreen)
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for get category list form database
    private fun getCategoryList() {
        var start = 0

        lifecycleScope.launch {

            employerViewModel.getCategoryList(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                            val categoryList =
                                Gson().fromJson(jsonObjectData, CategoryList::class.java)


                            val categoryName = mutableListOf("Select")
                            categoryName.addAll(categoryList.data.map { it.name })

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                categoryName
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerCategory.adapter = adapter

                            binding.spinnerCategory.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        if (position > 0) {
                                            selectedCategoryId = categoryList.data[position - 1].id
                                            if (start == 0) {
                                                start = 1
                                                getSubCategoryList(
                                                    categoryList.data[position - 1].id,
                                                    1
                                                )
                                            } else {
                                                getSubCategoryList(
                                                    categoryList.data[position - 1].id,
                                                    0
                                                )
                                            }
                                        } else {
                                            selectedCategoryId = null
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {

                                    }
                                }

                            if (jobDataModel != null) {
                                val defaultRelation = jobDataModel!!.category_name
                                val defaultIndex = categoryName.indexOf(defaultRelation)
                                if (defaultIndex != -1) {
                                    binding.spinnerCategory.setSelection(defaultIndex)
                                    selectedCategoryId = defaultIndex.toString()
                                }
                            }


                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for get sub category list form database
    private fun getSubCategoryList(stateId: String, start: Int) {
        lifecycleScope.launch {

            Log.e("State Id", stateId)

            employerViewModel.getSubCategoryList(sessionManager.getBearerToken(), stateId)
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                            val subCategoryList =
                                Gson().fromJson(jsonObjectData, SubCategoryList::class.java)


                            val subCategoryName = mutableListOf("Select")
                            subCategoryName.addAll(subCategoryList.data.map { it.name })
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                subCategoryName
                            )

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerSubCategory.adapter = adapter

                            binding.spinnerSubCategory.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        selectedSubCategoryId = if (position > 0) {
                                            subCategoryList.data[position - 1].id
                                        } else {
                                            null
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }
                            if (jobDataModel != null && start == 1) {
                                Log.e("Sub Category", jobDataModel!!.sub_category_name)
                                Log.e("Sub Category List", subCategoryList.toString())
                                val defaultRelation = jobDataModel!!.sub_category_name
                                val defaultIndex = subCategoryName.indexOf(defaultRelation)
                                Log.e("Position", defaultIndex.toString())
                                if (defaultIndex != -1) {
                                    binding.spinnerSubCategory.setSelection(defaultIndex)
                                    selectedSubCategoryId = if (defaultIndex > 0) {
                                        subCategoryList.data[defaultIndex - 1].id
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

    // This Function is used for get all filter list form database
    private fun getEducationList() {
        lifecycleScope.launch {

            employerViewModel.getEducation(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            val allFilterList =
                                Gson().fromJson(jsonObjectData, EducationList::class.java)

                            // Set Salary Range
                            val education = mutableListOf("Select")
                            education.addAll(allFilterList.data.map { it.name.toString() })
                            val educationAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                education
                            )
                            educationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerEducation.adapter = educationAdapter
                            binding.spinnerEducation.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        selectedEducation = if (position > 0) {
                                            allFilterList.data[position - 1].id
                                        } else {
                                            null
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }


                            if (jobDataModel != null) {
                                if (jobDataModel!!.required_education_label != "") {
                                    val defaultIndex =
                                        education.indexOf(jobDataModel!!.required_education_label)
                                    if (defaultIndex != -1) {
                                        binding.spinnerEducation.setSelection(defaultIndex)
                                        selectedEducation = if (defaultIndex > 0) {
                                            allFilterList.data[defaultIndex - 1].id
                                        } else {
                                            null
                                        }
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

    // This Function is used for get all filter list form database
    private fun getAllFilterList() {
        lifecycleScope.launch {

            employerViewModel.getAllFilterList(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            val allFilterList =
                                Gson().fromJson(jsonObjectData, AllFilterList::class.java)

                            // Set Salary Range
                            val salaryRange = mutableListOf("Select")
                            salaryRange.addAll(allFilterList.data.salaryRange.map { it.name.toString() })
                            val salaryAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                salaryRange
                            )
                            salaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerSalary.adapter = salaryAdapter
                            binding.spinnerSalary.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        selectedSalaryRange = if (position > 0) {
                                            allFilterList.data.salaryRange[position - 1].id
                                        } else {
                                            null
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }


                            if (jobDataModel != null) {
                                if (jobDataModel!!.salary_label != "") {
                                    val defaultIndex =
                                        salaryRange.indexOf(jobDataModel!!.salary_label)
                                    if (defaultIndex != -1) {
                                        binding.spinnerSalary.setSelection(defaultIndex)
                                        selectedSalaryRange = if (defaultIndex > 0) {
                                            allFilterList.data.salaryRange[defaultIndex - 1].id
                                        } else {
                                            null
                                        }
                                    }
                                }
                            }

                            // Set Genders
                            val genderNames = mutableListOf("Select")
                            genderNames.addAll(allFilterList.data.gender.map { it.name.toString() })
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                genderNames
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerGender.adapter = adapter
                            binding.spinnerGender.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        selectedGenderId = if (position > 0) {
                                            allFilterList.data.gender[position - 1].id
                                        } else {
                                            null
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }

                            if (jobDataModel != null) {
                                if (jobDataModel!!.gender_label != "") {
                                    val defaultIndex =
                                        genderNames.indexOf(jobDataModel!!.gender_label)
                                    if (defaultIndex != -1) {
                                        binding.spinnerGender.setSelection(defaultIndex)
                                        selectedGenderId = if (defaultIndex > 0) {
                                            allFilterList.data.gender[defaultIndex - 1].id
                                        } else {
                                            null
                                        }
                                    }
                                }
                            }


                            // Set Profile Type
                            val profileTypeName = mutableListOf("Select")
                            profileTypeName.addAll(allFilterList.data.profileType.map { it.name.toString() })
                            val profileTypeAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                profileTypeName
                            )
                            profileTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerProfileType.adapter = profileTypeAdapter
                            binding.spinnerProfileType.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        profileTypeId = if (position > 0) {
                                            allFilterList.data.profileType[position - 1].id
                                        } else {
                                            null
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }

                            if (jobDataModel != null) {
                                if (jobDataModel!!.profile_type_label != "") {
                                    val defaultIndex =
                                        profileTypeName.indexOf(jobDataModel!!.profile_type_label)
                                    if (defaultIndex != -1) {
                                        binding.spinnerProfileType.setSelection(defaultIndex)
                                        profileTypeId = if (defaultIndex > 0) {
                                            allFilterList.data.profileType[defaultIndex - 1].id
                                        } else {
                                            null
                                        }
                                    }
                                }
                            }

                            // Set Experience Type
                            val experienceName = mutableListOf("Select")
                            experienceName.addAll(allFilterList.data.experiences.map { it.name.toString() })
                            val experienceAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                experienceName
                            )
                            experienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerExperience.adapter = experienceAdapter
                            binding.spinnerExperience.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        experienceId = if (position > 0) {
                                            allFilterList.data.experiences[position - 1].id
                                        } else {
                                            null
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }

                            if (jobDataModel != null) {
                                if (jobDataModel!!.experience_label != "") {
                                    val defaultIndex =
                                        experienceName.indexOf(jobDataModel!!.experience_label)
                                    if (defaultIndex != -1) {
                                        binding.spinnerExperience.setSelection(defaultIndex)
                                        experienceId = if (defaultIndex > 0) {
                                            allFilterList.data.experiences[defaultIndex - 1].id
                                        } else {
                                            null
                                        }
                                    }
                                }
                            }

                            // Set Job Type
                            val jobTypeName = mutableListOf("Select")
                            jobTypeName.addAll(allFilterList.data.jobType.map { it.name.toString() })
                            val jobTypeAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                jobTypeName
                            )
                            jobTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerJobType.adapter = jobTypeAdapter
                            binding.spinnerJobType.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        jobTypeId = if (position > 0) {
                                            allFilterList.data.jobType[position - 1].id
                                        } else {
                                            null
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }

                            if (jobDataModel != null) {
                                if (jobDataModel!!.type_label != "") {
                                    val defaultIndex =
                                        jobTypeName.indexOf(jobDataModel!!.type_label)
                                    if (defaultIndex != -1) {
                                        binding.spinnerJobType.setSelection(defaultIndex)
                                        jobTypeId = if (defaultIndex > 0) {
                                            allFilterList.data.jobType[defaultIndex - 1].id
                                        } else {
                                            null
                                        }
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

    // This Function is used for get state list form database
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

                            if (jobDataModel != null) {
                                if (jobDataModel!!.state_name != "") {
                                    val defaultIndex = stateNames.indexOf(jobDataModel!!.state_name)
                                    if (defaultIndex != -1) {
                                        binding.spinnerState.setSelection(defaultIndex)
                                        selectedStateId = if (defaultIndex > 0) {
                                            stateList.data[defaultIndex - 1].id
                                        } else {
                                            null
                                        }
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

    // This Function is used for get city list form database
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

                            if (jobDataModel != null) {
                                if (jobDataModel!!.city_name != "" && start == 1) {
                                    Log.e("city", jobDataModel!!.city_name)
                                    Log.e("Sub Category List", cityNames.toString())
                                    val defaultIndex = cityNames.indexOf(jobDataModel!!.city_name)
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
                            }
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for open date picker dialog
    private fun openDatePicker(callback: (String) -> Unit) {
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

    // This Function is used for open alert error dialog box
    private fun alertErrorDialog(msg: String?) {
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

    // This Function is used for check response of api
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
            val sharedPreferences: SharedPreferences =
                requireActivity().getSharedPreferences("staffAppTempStorage", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.clear()
            editor.apply()
            val intent = Intent(requireActivity(), IdentityActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return null
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


}
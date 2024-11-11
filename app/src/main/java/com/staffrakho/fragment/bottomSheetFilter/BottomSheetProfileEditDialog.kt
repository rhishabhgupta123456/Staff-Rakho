package com.staffrakho.fragment.bottomSheetFilter


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
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
import com.staffrakho.databinding.BottomSheetProfileEditBinding
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BottomSheetProfileEditDialog(
    private val dob: String,
    val gender: String,
    private val skill: String,
    private val transportFacility: String,
    private val physicalFit: String,
    private val disability: String,
    private val profileType: String,
    private val about: String,
    private val state: String,
    private val city: String,
    private val address: String,
    private val zip: String,
    private val phone: String,
    private val role: String,
    private val subDistrict: String,
    private val village: String,
    private val age: String,
    private val disabilityDetails: String,
    private val maritalStatus: String,
    private val shortTermJobStatus: String,
    private val currentJobStatus: String,
) : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var binding: BottomSheetProfileEditBinding
    lateinit var sessionManager: SessionManager
    private lateinit var jobSeekerViewModel: JobSeekerViewModel
    var selectedTransportId: String? = null
    var selectedGenderId: String? = null
    var shortTermJobID: String? = null
    var profileTypeId: String? = null
    var physicalFitId: String? = null
    var selectedStateId: String? = null
    var selectCityId: String? = null
    var selectedDebilityId: String? = null
    var ageRangeID: String? = null
    var selectedMaritalStatusId: String? = null
    var selectedCurrentJobStatusId: String? = null

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
        binding = BottomSheetProfileEditBinding.inflate(
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
        jobSeekerViewModel = ViewModelProvider(this)[JobSeekerViewModel::class.java]

        binding.tvDate.setOnClickListener(this)
        binding.btnApplyFilter.setOnClickListener(this)
        binding.btCloseFilter.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)

        binding.tvDate.text = dob
        binding.etSkill.setText(skill)
        binding.etBio.setText(about)
        binding.etAddress.setText(address)
        binding.etZip.setText(zip)
        binding.etRole.setText(role)
        binding.etSubDistrict.setText(subDistrict)
        binding.etVilage.setText(village)
        binding.etDetailofDisability.setText(disabilityDetails)
        binding.etContactNumber.text = phone


        setDisabilitySpinner()


        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            getAllFilterList()
            getStateList()
        }


    }

    private fun setDisabilitySpinner() {
        val disabilityList = ArrayList<String>()
        disabilityList.add("Select")
        disabilityList.add("Yes")
        disabilityList.add("No")

        val disabilityListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            disabilityList
        )
        disabilityListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDisability.adapter = disabilityListAdapter
        binding.spinnerDisability.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedDebilityId = if (position > 0) {
                        disabilityList[position]
                    } else {
                        null
                    }

                    if (position == 1) {
                        binding.DetailofDisabilityHead.visibility = View.VISIBLE
                        binding.etDetailofDisability.visibility = View.VISIBLE
                        binding.etDetailofDisability.text = null
                    } else {
                        binding.DetailofDisabilityHead.visibility = View.GONE
                        binding.etDetailofDisability.visibility = View.GONE
                        binding.etDetailofDisability.text = null
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (disability != "") {
            val defaultIndex = disabilityList.indexOf(disability)
            if (defaultIndex != -1) {
                binding.spinnerDisability.setSelection(defaultIndex)
                selectedDebilityId = if (defaultIndex > 0) {
                    disabilityList[defaultIndex]
                } else {
                    null
                }

                if (defaultIndex == 1) {
                    binding.DetailofDisabilityHead.visibility = View.VISIBLE
                    binding.etDetailofDisability.visibility = View.VISIBLE
                } else {
                    binding.DetailofDisabilityHead.visibility = View.GONE
                    binding.etDetailofDisability.visibility = View.GONE
                }
            }
        }

    }

    private fun setCurrentJobSpinner() {
        val currentJobList = ArrayList<String>()
        currentJobList.add("Select")
        currentJobList.add("Currently Not Working(I Need job)")
        currentJobList.add("Currently working (Not looking for job)")
        currentJobList.add("Currently working (Interested in new job)")

        val disabilityListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currentJobList
        )
        disabilityListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrentJobStatus.adapter = disabilityListAdapter
        binding.spinnerCurrentJobStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedCurrentJobStatusId = if (position > 0) {
                        currentJobList[position]
                    } else {
                        null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (currentJobStatus != "") {
            val defaultIndex = currentJobList.indexOf(currentJobStatus)
            if (defaultIndex != -1) {
                binding.spinnerCurrentJobStatus.setSelection(defaultIndex)
                selectedCurrentJobStatusId = if (defaultIndex > 0) {
                    currentJobList[defaultIndex]
                } else {
                    null
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

            R.id.tvDate -> {
                openDatePicker { selectedDate ->
                    binding.tvDate.text = selectedDate
                }
            }
        }

    }

    // This Function is used for check validation in all field
    private fun checkValidation(): Boolean {
        if (binding.tvDate.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_date_of_birth))
            return false
        } else if (binding.etRole.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_user_role))
            return false
        } else if (selectedGenderId == null) {
            alertErrorDialog(getString(R.string.select_gender))
            return false
        } else if (selectedStateId == null) {
            alertErrorDialog(getString(R.string.please_select_the_state))
            return false
        } else if (selectCityId == null) {
            alertErrorDialog(getString(R.string.please_select_the_city))
            return false
        } else if (binding.etSubDistrict.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_sub_district))
            return false
        } else if (binding.etVilage.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_village))
            return false
        } else if (binding.etAddress.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_address))
            return false
        } else if (binding.etZip.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_zip_code))
            return false
        } else if (binding.etSkill.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_skills))
            return false
        } else if (selectedTransportId == null) {
            alertErrorDialog(getString(R.string.please_select_transport_facility))
            return false
        } else if (selectedMaritalStatusId == null) {
            alertErrorDialog(getString(R.string.please_select_the_marital_status))
            return false
        } else if (ageRangeID == null) {
            alertErrorDialog(getString(R.string.please_select_the_age_range))
            return false
        } else if (disability == null) {
            alertErrorDialog(getString(R.string.please_select_the_age_range))
            return false
        } else if (physicalFitId == null) {
            alertErrorDialog(getString(R.string.select_the_physically_fit))
            return false
        } else if (selectedDebilityId == null) {
            alertErrorDialog(getString(R.string.select_the_disability))
            return false
        } else if (profileTypeId == null) {
            alertErrorDialog(getString(R.string.select_the_profile_type))
            return false
        } else if (binding.etBio.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_bio))
            return false
        } else {
            return true
        }


    }

    // This Function is used for update user profile
    private fun updateProfile() {

        lifecycleScope.launch {

            jobSeekerViewModel.editProfile(
                sessionManager.getBearerToken(),
                binding.tvDate.text.toString(),
                selectedGenderId,
                selectedStateId,
                selectCityId,
                binding.etAddress.text.toString(),
                binding.etZip.text.toString(),
                binding.etSkill.text.toString(),
                selectedTransportId,
                physicalFitId,
                selectedDebilityId,
                profileTypeId,
                binding.etBio.text.toString(),
                binding.etRole.text.toString(),
                binding.etSubDistrict.text.toString(),
                binding.etDetailofDisability.text.toString(),
                ageRangeID,
                binding.etVilage.text.toString(),
                selectedMaritalStatusId,
                selectedCurrentJobStatusId
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
                            findNavController().navigate(R.id.openProfileScreen)
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for get all filter list from database
    private fun getAllFilterList() {
        lifecycleScope.launch {
            jobSeekerViewModel.getAllFilterList(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            val allFilterList =
                                Gson().fromJson(jsonObjectData, AllFilterList::class.java)

                            setGenderSpinner(allFilterList)
                            setTransportSpinner(allFilterList)
                            setPhysicallySpinner(allFilterList)
                            setProfileSpinner(allFilterList)
                            setShortTermJobSpinner(allFilterList)
                            setAgeRange(allFilterList)
                            setMaritalStatus(allFilterList)
                            setCurrentJobStatus(allFilterList)

                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    private fun setMaritalStatus(allFilterList: AllFilterList) {
        val data = allFilterList.data.marrital_status
        val list = mutableListOf("Select")
        list.addAll(data.map { it.name.toString() })
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMaritalStatus.adapter = adapter
        binding.spinnerMaritalStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedMaritalStatusId = if (position > 0) {
                        data[position - 1].id
                    } else {
                        null
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (maritalStatus != "") {
            val defaultIndex = list.indexOf(maritalStatus)
            if (defaultIndex != -1) {
                binding.spinnerMaritalStatus.setSelection(defaultIndex)
                selectedMaritalStatusId = if (defaultIndex > 0) {
                    data[defaultIndex - 1].id
                } else {
                    null
                }
            }
        }
    }

    private fun setCurrentJobStatus(allFilterList: AllFilterList) {
        val data = allFilterList.data.marrital_status
        val list = mutableListOf("Select")
        list.addAll(data.map { it.name.toString() })
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrentJobStatus.adapter = adapter
        binding.spinnerCurrentJobStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedCurrentJobStatusId = if (position > 0) {
                        data[position - 1].id
                    } else {
                        null
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (currentJobStatus != "") {
            val defaultIndex = list.indexOf(currentJobStatus)
            if (defaultIndex != -1) {
                binding.spinnerCurrentJobStatus.setSelection(defaultIndex)
                selectedCurrentJobStatusId = if (defaultIndex > 0) {
                    data[defaultIndex - 1].id
                } else {
                    null
                }
            }
        }
    }

    private fun setAgeRange(allFilterList: AllFilterList) {
        val data = allFilterList.data.age_range
        val list = mutableListOf("Select")
        list.addAll(data.map { it.name.toString() })
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAge.adapter = adapter
        binding.spinnerAge.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    ageRangeID = if (position > 0) {
                        data[position - 1].id
                    } else {
                        null
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (age != "") {
            val defaultIndex = list.indexOf(age)
            if (defaultIndex != -1) {
                binding.spinnerAge.setSelection(defaultIndex)
                ageRangeID = if (defaultIndex > 0) {
                    data[defaultIndex - 1].id
                } else {
                    null
                }
            }
        }
    }

    private fun setShortTermJobSpinner(allFilterList: AllFilterList) {
        val data = allFilterList.data.ready_for_short_terms_job
        val list = mutableListOf("Select")
        list.addAll(data.map { it.name.toString() })
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerShortTermJob.adapter = adapter
        binding.spinnerShortTermJob.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    shortTermJobID = if (position > 0) {
                        data[position - 1].id
                    } else {
                        null
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (shortTermJobStatus != "") {
            val defaultIndex = list.indexOf(shortTermJobStatus)
            if (defaultIndex != -1) {
                binding.spinnerShortTermJob.setSelection(defaultIndex)
                shortTermJobID = if (defaultIndex > 0) {
                    data[defaultIndex - 1].id
                } else {
                    null
                }
            }
        }
    }

    private fun setProfileSpinner(allFilterList: AllFilterList) {
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

        if (profileType != "") {
            val defaultIndex = profileTypeName.indexOf(profileType)
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

    private fun setPhysicallySpinner(allFilterList: AllFilterList) {
        val physicalFitName = mutableListOf("Select")
        physicalFitName.addAll(allFilterList.data.physicalFit.map { it.name.toString() })
        val physicalFitAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            physicalFitName
        )
        physicalFitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPhysicallyFit.adapter = physicalFitAdapter
        binding.spinnerPhysicallyFit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    physicalFitId = if (position > 0) {
                        allFilterList.data.physicalFit[position - 1].id
                    } else {
                        null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (physicalFit != "") {
            val defaultIndex = physicalFitName.indexOf(physicalFit)
            if (defaultIndex != -1) {
                binding.spinnerPhysicallyFit.setSelection(defaultIndex)
                physicalFitId = if (defaultIndex > 0) {
                    allFilterList.data.physicalFit[defaultIndex - 1].id
                } else {
                    null
                }
            }
        }
    }

    private fun setTransportSpinner(allFilterList: AllFilterList) {
        val transportName = mutableListOf("Select")
        transportName.addAll(allFilterList.data.transportFacility.map { it.name.toString() })
        val transportAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            transportName
        )
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTransportFacility.adapter = transportAdapter
        binding.spinnerTransportFacility.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedTransportId = if (position > 0) {
                        allFilterList.data.transportFacility[position - 1].id
                    } else {
                        null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (transportFacility != "") {
            val defaultIndex = transportName.indexOf(transportFacility)
            if (defaultIndex != -1) {
                binding.spinnerTransportFacility.setSelection(defaultIndex)
                selectedTransportId = if (defaultIndex > 0) {
                    allFilterList.data.transportFacility[defaultIndex - 1].id
                } else {
                    null
                }
            }
        }
    }

    private fun setGenderSpinner(allFilterList: AllFilterList) {
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

        if (gender != "") {
            val defaultIndex = genderNames.indexOf(gender)
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

    // This Function is used for get state list from database
    private fun getStateList() {
        var start = 0
        lifecycleScope.launch {

            jobSeekerViewModel.getStateList(sessionManager.getBearerToken())
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

    // This Function is used for get city list from database
    private fun getCityList(stateId: String, start: Int) {
        lifecycleScope.launch {

            Log.e("State Id", stateId)

            jobSeekerViewModel.getCityList(sessionManager.getBearerToken(), stateId)
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

    // This Function is used for open alert error dialog
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

}
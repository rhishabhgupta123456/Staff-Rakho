package com.staffrakho.fragment.bottomSheetFilter

import android.annotation.SuppressLint
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
import com.staffrakho.dataModel.CategoryList
import com.staffrakho.dataModel.CityList
import com.staffrakho.dataModel.StateList
import com.staffrakho.dataModel.SubCategoryList
import com.staffrakho.databinding.BottomSheetEmployeeFilterDialogBinding
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch

class BottomSheetEmployeeFilterDialog : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var binding: BottomSheetEmployeeFilterDialogBinding
    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel

    private var categoryId: String = ""
    private var subCategoryId: String = ""
    private var pinCode: String = ""
    private var jobType: String = ""
    private var stateId: String = ""
    private var cityId: String = ""

    private var businessType: String = ""
    private var genderID: String = ""
    private var selectedSalaryRange: String = ""
    private var companyLookId: String = ""
    private var selectedMaritalStatusId: String = ""

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
        binding = BottomSheetEmployeeFilterDialogBinding.inflate(
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

        binding.btnApplyFilter.setOnClickListener(this)
        binding.btCloseFilter.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)
        binding.btAdvanceFilter.setOnClickListener(this)
        binding.advanceFilterBox.visibility = View.GONE

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            getStateList()
            getCategoryList()
            getAllFilterList()
        }

    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnApplyFilter -> {
                dismiss()
                val bundle = Bundle()
                bundle.putString(AppConstant.JOB_TYPE, jobType)
                bundle.putString(AppConstant.CATEGORY_ID, categoryId)
                bundle.putString(AppConstant.SUB_CATEGORY_ID, subCategoryId)
                bundle.putString(AppConstant.PIN_CODE, binding.etZip.text.toString())
                bundle.putString(AppConstant.STATE_ID, stateId)
                bundle.putString(AppConstant.CITY_ID, cityId)

                bundle.putString(AppConstant.GENDER, genderID)
                bundle.putString(AppConstant.SALARY_RANGE, selectedSalaryRange)
                bundle.putString(AppConstant.MARITAL_STATUS, selectedMaritalStatusId)
                bundle.putString(AppConstant.BUSINESS_TYPE, businessType)
                bundle.putString(AppConstant.COMPANY_LOOK, companyLookId)
                findNavController().navigate(R.id.openSearchEmployeeScreen, bundle)
            }

            R.id.btCloseFilter -> {
                dismiss()
            }

            R.id.btnClose -> {
                dismiss()
            }


            R.id.btAdvanceFilter ->{
                if (binding.advanceFilterBox.visibility == View.VISIBLE){
                    binding.advanceFilterBox.visibility = View.GONE
                }else{
                    binding.advanceFilterBox.visibility = View.VISIBLE
                }
            }

        }

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


                            val relationsName = mutableListOf("Select")
                            relationsName.addAll(allFilterList.data.jobType.map { it.name.toString() })

                            val relationsAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                relationsName
                            )
                            relationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerJobType.adapter = relationsAdapter
                            binding.spinnerJobType.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        jobType = if (position > 0) {
                                            allFilterList.data.jobType[position-1].id
                                        } else {
                                            ""
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }

                            setBusinessType(allFilterList)
                            setGenderRange(allFilterList)
                            setSalaryRange(allFilterList)
                            setMaritalStatus(allFilterList)
                            setCompanyLook(allFilterList)

                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    private fun setCompanyLook(allFilterList : AllFilterList) {
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
                        ""
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

    }

    private fun setBusinessType(allFilterList: AllFilterList) {
        val businessTypeName = mutableListOf("Select")
        businessTypeName.addAll(allFilterList.data.businessType.map { it.name.toString() })

        val businessTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            businessTypeName
        )
        businessTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBusinessType.adapter = businessTypeAdapter
        binding.spinnerBusinessType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    businessType = if (position > 0) {
                        allFilterList.data.businessType[position - 1].id
                    } else {
                        ""
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
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
                        ""
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

    }

    private fun setSalaryRange(allFilterList: AllFilterList){
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
                        ""
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

    }

    private fun setGenderRange(allFilterList: AllFilterList) {
        val data = allFilterList.data.gender
        val list = mutableListOf("Select")
        list.addAll(data.map { it.name.toString() })
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinneGender.adapter = adapter
        binding.spinneGender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    genderID = if (position > 0) {
                        data[position - 1].id
                    } else {
                        ""
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

    }



    // This Function is used for get category List from database
    private fun getCategoryList() {
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
                                            categoryId = categoryList.data[position-1].id
                                            getSubCategoryList(categoryList.data[position-1].id)
                                        } else {
                                            categoryId = ""
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }


                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for get sub category List from database
    private fun getSubCategoryList(stateId: String) {
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
                                        subCategoryId = if (position > 0) {
                                            subCategoryList.data[position-1].id
                                        }else{
                                            ""
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
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
        lifecycleScope.launch {

            employerViewModel.getStateList(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                            val stateList = Gson().fromJson(jsonObjectData, StateList::class.java)


                            val stateNames = mutableListOf("select")
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
                                            stateId = stateList.data[position-1].id
                                            getCityList(stateId)
                                        } else {
                                            stateId = ""
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
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
    private fun getCityList(stateId: String) {
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
                                        cityId = if (position > 0) {
                                            cityList.data[position-1].id
                                        } else {
                                            ""
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for check api response
    fun checkResponse(jsonObject : BaseResponse<JsonObject>): JsonObject? {

        if (!jsonObject.isIsError) {
            if (jsonObject.response != null) {
                try {
                    val jsonObjectData: JsonObject = jsonObject.response!!
                    val status = jsonObjectData["success"].asBoolean

                    if (status){
                        return jsonObjectData
                    }else{
                        alertErrorDialog(jsonObjectData["message"].asString)
                    }

                }catch (e:Exception){
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

    // This Function is used for show error alert box
    fun  alertErrorDialog(msg:String?){
        val alertErrorDialog= Dialog(requireActivity())
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
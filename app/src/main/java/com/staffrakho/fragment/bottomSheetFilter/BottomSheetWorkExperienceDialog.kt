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
import com.staffrakho.dataModel.CategoryList
import com.staffrakho.dataModel.SubCategoryList
import com.staffrakho.dataModel.WorkExperienceDataModel
import com.staffrakho.databinding.BottomSheetWorkExprienceBinding
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BottomSheetWorkExperienceDialog(private var workExperienceDataModel: WorkExperienceDataModel?) :
    BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var binding: BottomSheetWorkExprienceBinding
    lateinit var sessionManager: SessionManager
    private lateinit var jobSeekerViewModel: JobSeekerViewModel
    private var selectedCategoryId: String? = null
    private var selectedSubCategoryId: String? = null


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
        binding = BottomSheetWorkExprienceBinding.inflate(
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

        binding.tvFromDate.setOnClickListener(this)
        binding.tvToDate.setOnClickListener(this)
        binding.btnApplyFilter.setOnClickListener(this)
        binding.btCloseFilter.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)

        if (workExperienceDataModel != null) {
            binding.etShopName.setText(workExperienceDataModel!!.shop_name)
            binding.etReasonForLeave.setText(workExperienceDataModel!!.reason_for_change)
            binding.etRolesResponsibilities.setText(workExperienceDataModel!!.role)
            binding.tvFromDate.text = workExperienceDataModel!!.from_date
            binding.tvToDate.text = workExperienceDataModel!!.to_date

            binding.btnApplyFilter.text = getString(R.string.update)
        }

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            getCategoryList()
        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnApplyFilter -> {
                if (checkValidation()) {
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    }else{
                        if (workExperienceDataModel != null) {
                            updateWorkExperience()
                        } else {
                            addWorkExperience()
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

            R.id.tvFromDate -> {
                openDatePicker { selectedDate ->
                    binding.tvFromDate.text = selectedDate
                }
            }

            R.id.tvToDate -> {
                openDatePicker { selectedDate ->
                    binding.tvToDate.text = selectedDate
                }
            }
        }

    }

    private fun checkValidation(): Boolean {
        if (selectedCategoryId == null) {
            alertErrorDialog("Select the Category")
            return false
        } else if (selectedSubCategoryId == null) {
            alertErrorDialog("Select the Sub Category")
            return false
        } else if (binding.etShopName.text.toString().isEmpty()) {
            alertErrorDialog("Fill the Shop Name")
            return false
        } else if (binding.etReasonForLeave.text.toString().isEmpty()) {
            alertErrorDialog("Fill the Reason from leave")
            return false
        } else if (binding.etRolesResponsibilities.text.toString().isEmpty()) {
            alertErrorDialog("Fill the Roles & Responsibility")
            return false
        } else if (binding.tvFromDate.text.toString().isEmpty()) {
            alertErrorDialog("Fill the Job Start Date")
            return false
        } else if (binding.tvToDate.text.toString().isEmpty()) {
            alertErrorDialog("Fill the Job End Date")
            return false
        } else {
            return true
        }

    }

    // This Function is used for add work experience
    private fun addWorkExperience() {
        lifecycleScope.launch {
            jobSeekerViewModel.addWorkExperience(
                sessionManager.getBearerToken(),
                selectedCategoryId!!,
                selectedSubCategoryId!!,
                binding.tvFromDate.text.toString(),
                binding.tvToDate.text.toString(),
                binding.etReasonForLeave.text.toString(),
                binding.etShopName.text.toString(),
                binding.etRolesResponsibilities.text.toString(),
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            Toast.makeText(requireContext(), jsonObjectData["message"].asString, Toast.LENGTH_SHORT).show()
                            dismiss()
                            val bundle = Bundle()
                            bundle.putString(AppConstant.SCREEN,AppConstant.MENU_SCREEN)
                            bundle.putInt(AppConstant.SCREEN,2)
                            findNavController().navigate(R.id.openProfileScreen,bundle)
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for update work experience
    private fun updateWorkExperience() {
        lifecycleScope.launch {

            jobSeekerViewModel.updateWorkExperience(
                sessionManager.getBearerToken(),
                workExperienceDataModel!!.id,
                selectedCategoryId!!,
                selectedSubCategoryId!!,
                binding.tvFromDate.text.toString(),
                binding.tvToDate.text.toString(),
                binding.etReasonForLeave.text.toString(),
                binding.etShopName.text.toString(),
                binding.etRolesResponsibilities.text.toString(),
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            Toast.makeText(requireContext(), jsonObjectData["message"].asString, Toast.LENGTH_SHORT).show()
                            dismiss()
                            val bundle = Bundle()
                            bundle.putInt(AppConstant.SCREEN,2)
                            findNavController().navigate(R.id.openProfileScreen,bundle)
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for get category list from database
    private fun getCategoryList() {
        var start = 0
        lifecycleScope.launch {

            jobSeekerViewModel.getCategoryList(sessionManager.getBearerToken())
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
                                            selectedCategoryId = categoryList.data[position-1].id
                                            if (start == 0){
                                                start = 1
                                                getSubCategoryList(categoryList.data[position-1].id, 1)
                                            }else{
                                                getSubCategoryList(categoryList.data[position-1].id , 0)
                                            }
                                          } else {
                                            selectedCategoryId = null
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {

                                    }
                                }

                            if (workExperienceDataModel != null) {
                                val defaultRelation = workExperienceDataModel!!.category
                                val defaultIndex = categoryName.indexOf(defaultRelation)
                                if (defaultIndex != -1) {
                                    binding.spinnerCategory.setSelection(defaultIndex)
                                    selectedCategoryId = if (defaultIndex > 0) {
                                        categoryList.data[defaultIndex-1].id
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

    // This Function is used for get sub category list from database
    private fun getSubCategoryList(categoryId: String , start : Int) {
        lifecycleScope.launch {

            Log.e("State Id", categoryId)

            jobSeekerViewModel.getSubCategoryList(sessionManager.getBearerToken(), categoryId)
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
                                               subCategoryList.data[position-1].id
                                        } else {
                                            null
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }

                            if (workExperienceDataModel != null && start == 1) {
                                Log.e("Sub Category" , workExperienceDataModel!!.sub_category)
                                Log.e("Sub Category List",subCategoryList.toString())
                                val defaultRelation = workExperienceDataModel!!.sub_category
                                val defaultIndex = subCategoryName.indexOf(defaultRelation)
                                Log.e("Position",defaultIndex.toString())
                                if (defaultIndex != -1) {
                                    binding.spinnerSubCategory.setSelection(defaultIndex)
                                    selectedSubCategoryId = if (defaultIndex > 0) {
                                        subCategoryList.data[defaultIndex-1].id
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

    // This Function is used for open date picker
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

    // This Function is used for check response of api
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
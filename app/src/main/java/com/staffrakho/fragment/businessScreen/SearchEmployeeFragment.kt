package com.staffrakho.fragment.businessScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.adapter.UserProfileAdapter
import com.staffrakho.dataModel.UserData
import com.staffrakho.dataModel.UserProfileDataModel
import com.staffrakho.databinding.FragmentSearchEmployeeBinding
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetEmployeeFilterDialog
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetJobFilterDialog
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class SearchEmployeeFragment : BaseFragment() {

    lateinit var binding: FragmentSearchEmployeeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel

    private var categoryId: String = ""
    private var subCategoryId: String = ""
    private var jobType: String = ""
    private var stateId: String = ""
    private var cityId: String = ""
    private var pinCode: String = ""

    private var businessType: String = ""
    private var genderID: String = ""
    private var selectedSalaryRange: String = ""
    private var companyLookId: String = ""
    private var selectedMaritalStatusId: String = ""

    private var jobList = ArrayList<UserData>()
    private lateinit var jobAdapter: UserProfileAdapter
    private var isLoading = false
    private var currentPage = 1
    private var totPage = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchEmployeeBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        employerViewModel = ViewModelProvider(this)[EmployerViewModel::class.java]

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.openHomeScreen)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        categoryId = if (requireArguments().getString(AppConstant.CATEGORY_ID) != null) {
            requireArguments().getString(AppConstant.CATEGORY_ID)!!
        } else {
            ""
        }

        subCategoryId = if (requireArguments().getString(AppConstant.SUB_CATEGORY_ID) != null) {
            requireArguments().getString(AppConstant.SUB_CATEGORY_ID)!!
        } else {
            ""
        }


        pinCode = if (requireArguments().getString(AppConstant.PIN_CODE) != null) {
            requireArguments().getString(AppConstant.PIN_CODE)!!
        } else {
            ""
        }

        jobType = if (requireArguments().getString(AppConstant.JOB_TYPE) != null) {
            requireArguments().getString(AppConstant.JOB_TYPE)!!
        } else {
            ""
        }

        stateId = if (requireArguments().getString(AppConstant.STATE_ID) != null) {
            requireArguments().getString(AppConstant.STATE_ID)!!
        } else {
            ""
        }
        cityId = if (requireArguments().getString(AppConstant.CITY_ID) != null) {
            requireArguments().getString(AppConstant.CITY_ID)!!
        } else {
            ""
        }

        businessType = if (requireArguments().getString(AppConstant.BUSINESS_TYPE) != null) {
            requireArguments().getString(AppConstant.BUSINESS_TYPE)!!
        } else {
            ""
        }

        genderID = if (requireArguments().getString(AppConstant.GENDER) != null) {
            requireArguments().getString(AppConstant.GENDER)!!
        } else {
            ""
        }


        selectedSalaryRange = if (requireArguments().getString(AppConstant.SALARY_RANGE) != null) {
            requireArguments().getString(AppConstant.SALARY_RANGE)!!
        } else {
            ""
        }

        companyLookId = if (requireArguments().getString(AppConstant.COMPANY_LOOK) != null) {
            requireArguments().getString(AppConstant.COMPANY_LOOK)!!
        } else {
            ""
        }


        selectedMaritalStatusId = if (requireArguments().getString(AppConstant.MARITAL_STATUS) != null) {
            requireArguments().getString(AppConstant.MARITAL_STATUS)!!
        } else {
            ""
        }


        binding.btFindEmployee.setOnClickListener {
            val bottomSheetFragment = BottomSheetJobFilterDialog()
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.openHomeScreen)
        }




        jobAdapter =
            UserProfileAdapter(jobList, requireActivity(), AppConstant.SEARCH_EMPLOYEE_SCREEN)
        binding.searchEmployeeRecycleView.adapter = jobAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.searchEmployeeRecycleView.layoutManager = layoutManager

        binding.searchEmployeeRecycleView.setOnScrollChangeListener { _, _, _, _, _ ->
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

            if (!isNetworkAvailable()) {
                alertErrorDialog(getString(R.string.no_internet))
            }else{
                if (!isLoading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        if (totPage > currentPage) {
                            isLoading = true
                            currentPage++
                            findMoreJobs()
                        }
                    }
                }
            }
        }


        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            findMoreJobs()
        }

    }

    // This Function is used for get more and filter jobs
    private fun findMoreJobs() {
        lifecycleScope.launch {

            binding.tvProgressBar.visibility = View.VISIBLE

            employerViewModel.findPerfectEmployee(
                sessionManager.getBearerToken(),
                categoryId,
                subCategoryId,
                jobType,
                stateId,
                cityId,
                currentPage.toString(),
            ).observe(viewLifecycleOwner) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE
                isLoading = false
                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    try {

                        val list = Gson().fromJson(jsonObjectData, UserProfileDataModel::class.java)
                        jobAdapter.addItems(list.data)

                        if (list.data.isEmpty()){
                            binding.noRecordDataBox.visibility = View.VISIBLE
                        }else{
                            binding.noRecordDataBox.visibility = View.GONE

                        }

                        if (list.pagination != null) {
                            totPage = list.pagination.total_pages
                        }

                    } catch (e: Exception) {
                        binding.noRecordDataBox.visibility = View.VISIBLE
                        alertErrorDialog(e.message.toString())
                    }
                }else{
                    binding.noRecordDataBox.visibility = View.VISIBLE
                }
            }

        }
    }

}
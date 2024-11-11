package com.staffrakho.fragment.jobScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.adapter.JobsListAdapter
import com.staffrakho.dataModel.AppliedJobDataModel
import com.staffrakho.dataModel.JobList
import com.staffrakho.databinding.FragmentSearchJobBinding
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetJobFilterDialog
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class SearchJobFragment : BaseFragment() {

    lateinit var binding: FragmentSearchJobBinding
    private lateinit var sessionManager: SessionManager
    lateinit var jobSeekerViewModel: JobSeekerViewModel

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


    private var jobList = ArrayList<JobList>()
    private lateinit var jobAdapter: JobsListAdapter
    private var isLoading = false
    private var currentPage = 1
    private var totPage = 1

    private var sortOrder: String = "desc"
    private var sortField: String = "created_at"

    private var interstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchJobBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        jobSeekerViewModel = ViewModelProvider(this)[JobSeekerViewModel::class.java]

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

        binding.sortIcon.setOnClickListener() {
            openMenu()
        }



        jobAdapter = JobsListAdapter(jobList, requireContext())
        binding.searchJobJobRecycleView.adapter = jobAdapter
        jobAdapter.setOnRequestAction(object : JobsListAdapter.OnRequestAction {

            override fun viewJobs(item: JobList) {
                loadInterstitialAd(item)
            }

            override fun savedJob(item: JobList) {
                if (!isNetworkAvailable()) {
                    alertErrorDialog(getString(R.string.no_internet))
                } else {
                    savedTheJob(item)
                }

            }

            override fun unSavedJob(item: JobList) {
                if (!isNetworkAvailable()) {
                    alertErrorDialog(getString(R.string.no_internet))
                } else {
                    unSavedTheJob(item)
                }

            }

        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.searchJobJobRecycleView.layoutManager = layoutManager
        binding.searchJobJobRecycleView.setOnScrollChangeListener { _, _, _, _, _ ->
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

            if (!isNetworkAvailable()) {
                alertErrorDialog(getString(R.string.no_internet))
            } else {
                if (!isLoading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        if (totPage > currentPage) {
                            isLoading = true
                            currentPage++
                            findMoreJobs(false)
                        }
                    }
                }
            }

        }


        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            findMoreJobs(true)
        }

    }

    private fun loadInterstitialAd(item: JobList) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireActivity(), AppConstant.ADD_UNIT_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null  // Load a new ad after the old one is dismissed
                            showJobDetailsPage(item)
                        }

                        override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                            interstitialAd = null
                            showJobDetailsPage(item)
                        }
                    }
                    showInterstitialAd(item)
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    interstitialAd = null
                }
            })
    }

    private fun showInterstitialAd(item: JobList) {
        interstitialAd?.show(requireActivity()) ?: run {
            showJobDetailsPage(item)
        }
    }

    private fun showJobDetailsPage(item: JobList){
        sessionManager.setSourceFragment(AppConstant.SEARCH_JOB_SCREEN)
        val bundle = Bundle()
        bundle.putString(AppConstant.JOB_DETAIL, Gson().toJson(item))
        bundle.putString(
            AppConstant.CATEGORY_ID,
            requireArguments().getString(AppConstant.CATEGORY_ID)
        )
        bundle.putString(
            AppConstant.SUB_CATEGORY_ID,
            requireArguments().getString(AppConstant.SUB_CATEGORY_ID)
        )
        bundle.putString(
            AppConstant.JOB_TYPE,
            requireArguments().getString(AppConstant.JOB_TYPE)
        )
        bundle.putString(
            AppConstant.STATE_ID,
            requireArguments().getString(AppConstant.STATE_ID)
        )
        bundle.putString(
            AppConstant.CITY_ID,
            requireArguments().getString(AppConstant.CITY_ID)
        )
        bundle.putString(
            AppConstant.BUSINESS_TYPE,
            requireArguments().getString(AppConstant.BUSINESS_TYPE)
        )
        findNavController().navigate(R.id.openJobDetailsScreen, bundle)

    }

    private fun savedTheJob(item: JobList) {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            jobSeekerViewModel.savedTheJob(sessionManager.getBearerToken(), item.id)
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                            Toast.makeText(
                                requireContext(),
                                jsonObjectData["message"].asJsonArray[0].asString,
                                Toast.LENGTH_SHORT
                            ).show()

                            currentPage = 1
                            jobList.clear()

                            findMoreJobs(true)


                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }


    }

    private fun unSavedTheJob(item: JobList) {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            jobSeekerViewModel.unSavedJob(sessionManager.getBearerToken(), item.id)
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                            Toast.makeText(
                                requireContext(),
                                jsonObjectData["message"].asJsonArray[0].asString,
                                Toast.LENGTH_SHORT
                            ).show()

                            currentPage = 1
                            jobList.clear()

                            findMoreJobs(true)



                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }

    }


    // This Function is used for open Popup Menu
    private fun openMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.sortIcon)
        popupMenu.menuInflater.inflate(R.menu.sort_job_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
            when (menuItem.itemId) {

                R.id.btDateAcc -> {
                    sortOrder = "asc"
                    sortField = "created_at"
                    currentPage = 1
                    jobList.clear()
                    findMoreJobs(true)
                }

                R.id.btDateDesc -> {
                    sortOrder = "desc"
                    sortField = "created_at"
                    currentPage = 1
                    jobList.clear()
                    findMoreJobs(true)
                }

                R.id.btSalaryAcc -> {
                    sortOrder = "asc"
                    sortField = "salary"
                    currentPage = 1
                    jobList.clear()
                    findMoreJobs(true)
                }

                R.id.btSalaryDesc -> {
                    sortOrder = "desc"
                    sortField = "salary"
                    currentPage = 1
                    jobList.clear()
                    findMoreJobs(true)
                }

            }
            true
        }


        // Showing the popup menu
        popupMenu.show()
    }


    // This Function is used for get Searchable Jobs
    private fun findMoreJobs(update : Boolean) {
        lifecycleScope.launch {

            binding.tvProgressBar.visibility = View.VISIBLE

            jobSeekerViewModel.findJobList(
                sessionManager.getBearerToken(),
                categoryId,
                subCategoryId,
                jobType,
                stateId,
                cityId,
                genderID,
                selectedSalaryRange,
                pinCode,
                businessType,
                companyLookId,
                currentPage.toString(),
                sortOrder,
                sortField
            ).observe(viewLifecycleOwner) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE
                isLoading = false
                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    try {

                        val list = Gson().fromJson(jsonObjectData, AppliedJobDataModel::class.java)

                        if (list.data.isEmpty()) {
                            binding.noRecordDataBox.visibility = View.VISIBLE
                        } else {
                            binding.noRecordDataBox.visibility = View.GONE

                        }


                        if (update){
                            jobAdapter.updateAllList(list.data)
                        }else{
                            jobAdapter.addItems(list.data)
                        }



                        if (list.pagination != null) {
                            totPage = list.pagination.total_pages
                        }

                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                } else {
                    binding.noRecordDataBox.visibility = View.VISIBLE
                }

            }

        }
    }

}
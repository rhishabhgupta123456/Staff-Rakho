package com.staffrakho.fragment.jobScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.activity.JobActivity
import com.staffrakho.adapter.CategoryAdapter
import com.staffrakho.adapter.DashboardCounterAdapter
import com.staffrakho.adapter.JobsListAdapter
import com.staffrakho.dataModel.AppliedJobDataModel
import com.staffrakho.dataModel.DashboardCounterDataModel
import com.staffrakho.dataModel.JobList
import com.staffrakho.dataModel.PopularCategoryDataModel
import com.staffrakho.databinding.FragmentJobHomeBinding
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetJobFilterDialog
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class JobHomeFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentJobHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var jobSeekerViewModel: JobSeekerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentJobHomeBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        jobSeekerViewModel = ViewModelProvider(this)[JobSeekerViewModel::class.java]

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    closeAppAlertDialog()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.btJobs.setOnClickListener(this)
        binding.profileBox.setOnClickListener(this)
        binding.btRecentlyJobs.setOnClickListener(this)
        binding.btFavouriteJobs.setOnClickListener(this)
        binding.btNewJobs.setOnClickListener(this)


        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            getPopularCategory()
            getUserProfile()
            getProfileMatchingJobs()
            dashboardCounter()
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btJobs -> {
                val bundle = Bundle()
                bundle.putString(AppConstant.CATEGORY_ID, "")
                bundle.putString(AppConstant.CITY_ID, "")
                bundle.putString(AppConstant.STATE_ID, "")
                bundle.putString(AppConstant.JOB_TYPE, "")
                bundle.putString(AppConstant.SUB_CATEGORY_ID, "")
                findNavController().navigate(R.id.openSearchJobScreen, bundle)
            }

            R.id.profileBox -> {
                sessionManager.setSourceFragment(AppConstant.HOME_SCREEN)
                findNavController().navigate(R.id.openProfileScreen)
            }

            R.id.btRecentlyJobs -> {
                findNavController().navigate(R.id.openRecentJobsFragment)
            }

            R.id.btFavouriteJobs -> {
                findNavController().navigate(R.id.openFavouriteFragment)
            }

            R.id.btNewJobs -> {
                findNavController().navigate(R.id.openNewJobsFragment)
            }

        }
    }

    // This Function is used for get popular category from database
    private fun getPopularCategory() {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            jobSeekerViewModel.getPopularCategory(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        binding.tvProgressBar.visibility = View.GONE
                        try {
                            val category = Gson().fromJson(
                                jsonObjectData,
                                PopularCategoryDataModel::class.java
                            )
                            binding.popularCategoriesRecycleView.adapter =
                                CategoryAdapter(category.data, requireActivity())
                            binding.popularCategoriesRecycleView.layoutManager = GridLayoutManager(
                                requireContext(),
                                2,
                                GridLayoutManager.VERTICAL,
                                false
                            )
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }

    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as JobActivity
        activity.openHomeScreen()
    }

    // This Function is used for get get user profile from database
    @SuppressLint("SetTextI18n")
    private fun getUserProfile() {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            jobSeekerViewModel.getUserProfile(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        binding.tvProgressBar.visibility = View.GONE
                        try {

                            sessionManager.setUserEmail(checkFieldSting(jsonObjectData["data"].asJsonObject["email"]))
                            sessionManager.setUserPhone(checkFieldSting(jsonObjectData["data"].asJsonObject["mobile"]))

                            binding.tvUserName.text =
                                jsonObjectData["data"].asJsonObject["name"].asString + " \uD83D\uDC4B"

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + jsonObjectData["data"].asJsonObject["avatar"].asString)
                                .placeholder(R.drawable.demo_user)
                                .into(binding.tvProfile)


                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }
    }

    // This Function is used for get Recent jobs from database
    private fun getProfileMatchingJobs() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            jobSeekerViewModel.getProfileMatchingJobs(
                sessionManager.getBearerToken(),
            ).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        val jobList =
                            Gson().fromJson(jsonObjectData, AppliedJobDataModel::class.java)
                        binding.matchingJobsbRecycleView.layoutManager =
                            LinearLayoutManager(requireContext())
                        val jobAdapter =
                            JobsListAdapter(jobList.data, requireContext())
                        binding.matchingJobsbRecycleView.adapter = jobAdapter
                        jobAdapter.setOnRequestAction(object : JobsListAdapter.OnRequestAction {
                            override fun viewJobs(item: JobList) {
                                sessionManager.setSourceFragment(AppConstant.HOME_SCREEN)
                                val bundle = Bundle()
                                bundle.putString(AppConstant.JOB_DETAIL, Gson().toJson(item))
                                findNavController().navigate(R.id.openJobDetailsScreen, bundle)
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

                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }

        }
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

                            getProfileMatchingJobs()


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

                            getProfileMatchingJobs()


                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }

    }

    // This Function is used for get dashboard counter from database
    private fun dashboardCounter() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            jobSeekerViewModel.getDashboardCounter(
                sessionManager.getBearerToken(),
            ).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        val jobList =
                            Gson().fromJson(jsonObjectData, DashboardCounterDataModel::class.java)
                        binding.dashboardCounterRecycleView.layoutManager = GridLayoutManager(
                            requireContext(),
                            2,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                        binding.dashboardCounterRecycleView.adapter =
                            DashboardCounterAdapter(jobList.data, requireActivity())
                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }

        }
    }


}



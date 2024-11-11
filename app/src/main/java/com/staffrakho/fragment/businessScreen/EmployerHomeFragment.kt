package com.staffrakho.fragment.businessScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.activity.EmployerActivity
import com.staffrakho.adapter.DashboardCounterAdapter
import com.staffrakho.adapter.UserProfileAdapter
import com.staffrakho.dataModel.DashboardCounterDataModel
import com.staffrakho.dataModel.UserProfileDataModel
import com.staffrakho.databinding.FragmentEmployerHomeBinding
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetEmployeeFilterDialog
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class EmployerHomeFragment : BaseFragment() {

    lateinit var binding: FragmentEmployerHomeBinding
    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEmployerHomeBinding.inflate(
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
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    closeAppAlertDialog()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btFindEmployee.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstant.CATEGORY_ID, "")
            bundle.putString(AppConstant.CITY_ID, "")
            bundle.putString(AppConstant.STATE_ID, "")
            bundle.putString(AppConstant.JOB_TYPE, "")
            bundle.putString(AppConstant.SUB_CATEGORY_ID, "")
            findNavController().navigate(R.id.openSearchEmployeeScreen, bundle)
        }

        binding.profileBox.setOnClickListener {
            sessionManager.setSourceFragment(AppConstant.HOME_SCREEN)
            findNavController().navigate(R.id.openProfileScreen)
        }


        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            getUserProfile()
            dashboardCounter()
            getRecentProfile()
        }
    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as EmployerActivity
        activity.openHomeScreen()
    }

    // This Function is used for get dashboard counter from database
    private fun dashboardCounter() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            employerViewModel.getUserEmployerDashBoard(
                sessionManager.getBearerToken(),
            ).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        val jobList =
                            Gson().fromJson(jsonObjectData, DashboardCounterDataModel::class.java)
                        binding.dashboardCounterRecycleView.layoutManager = GridLayoutManager(
                            requireContext(), 2,
                            GridLayoutManager.VERTICAL, false
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

    // This Function is used for get recent profile from database
    private fun getRecentProfile() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            employerViewModel.getRecentProfiles(
                sessionManager.getBearerToken(),
            ).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        val jobList =
                            Gson().fromJson(jsonObjectData, UserProfileDataModel::class.java)
                        binding.recentProfileRecycleView.adapter =
                            UserProfileAdapter(
                                jobList.data,
                                requireActivity(),
                                AppConstant.HOME_SCREEN
                            )
                        binding.recentProfileRecycleView.layoutManager =
                            LinearLayoutManager(requireContext())

                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }

        }
    }

    // This Function is used for get user profile from database
    @SuppressLint("SetTextI18n")
    private fun getUserProfile() {

        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            employerViewModel.getUserEmployerProfile(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        binding.tvProgressBar.visibility = View.GONE
                        try {

                            sessionManager.setUserEmail(checkFieldSting(jsonObjectData["data"].asJsonObject["email"]))
                            sessionManager.setUserPhone(checkFieldSting(jsonObjectData["data"].asJsonObject["mobile"]))

                            binding.tvCompanyName.text =
                                jsonObjectData["data"].asJsonObject["company_name"].asString + " \uD83D\uDC4B"

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + jsonObjectData["data"].asJsonObject["logo"].asString)
                                .placeholder(R.drawable.company_icon)
                                .into(binding.tvProfile)

                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }
    }

}
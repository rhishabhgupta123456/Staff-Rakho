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
import com.staffrakho.dataModel.UserProfileDataModel
import com.staffrakho.databinding.FragmentApplicantsBinding
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class ApplicantsFragment : BaseFragment() {

    lateinit var binding: FragmentApplicantsBinding
    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentApplicantsBinding.inflate(
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
                    findNavController().navigate(R.id.openPostJobsScreen)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.btBack.setOnClickListener{
            findNavController().navigate(R.id.openPostJobsScreen)
        }

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            getApplicantsList()
        }

    }

    // This Function is used for get applicants list from database
    private fun getApplicantsList() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            employerViewModel.getApplicantsList(
                sessionManager.getBearerToken(),).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        val jobList =
                            Gson().fromJson(jsonObjectData, UserProfileDataModel::class.java)

                        if (jobList.data.isEmpty()){
                            binding.noRecordDataBox.visibility = View.VISIBLE
                        }else{
                            binding.noRecordDataBox.visibility = View.GONE

                        }

                        binding.applicantsRecycleView.adapter =
                            UserProfileAdapter(jobList.data, requireActivity(), AppConstant.APPLICANTS_SCREEN)
                        binding.applicantsRecycleView.layoutManager = LinearLayoutManager(requireContext())

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
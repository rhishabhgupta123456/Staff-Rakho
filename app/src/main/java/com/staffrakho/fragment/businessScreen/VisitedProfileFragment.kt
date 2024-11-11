package com.staffrakho.fragment.businessScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.staffrakho.databinding.FragmentVistedProfileBinding
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch

class VisitedProfileFragment : BaseFragment() {


    lateinit var binding: FragmentVistedProfileBinding
    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentVistedProfileBinding.inflate(
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
                    findNavController().navigate(R.id.openHomeScreen)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.btBack.setOnClickListener{
            findNavController().navigate(R.id.openHomeScreen)
        }

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            getVisitedProfile()
        }

    }

    private fun getVisitedProfile() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            employerViewModel.getVisitedProfile(
                sessionManager.getBearerToken(),
            ).observe(viewLifecycleOwner) { jsonObject ->

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

                        binding.visitedProfileRecycleView.adapter =
                            UserProfileAdapter(jobList.data, requireActivity(), AppConstant.VISITED_PROFILE_SCREEN)
                        binding.visitedProfileRecycleView.layoutManager = LinearLayoutManager(requireContext())

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
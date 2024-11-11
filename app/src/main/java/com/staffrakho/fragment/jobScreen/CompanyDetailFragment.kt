package com.staffrakho.fragment.jobScreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.staffrakho.R
import com.staffrakho.databinding.FragmentCompanyDetailBinding
import com.staffrakho.databinding.FragmentProfileBusinessBinding
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetEmployerProfileEditDialog
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch

class CompanyDetailFragment : BaseFragment() , View.OnClickListener {


    lateinit var binding : FragmentCompanyDetailBinding
    lateinit var sessionManager: SessionManager
    lateinit var jobSeekerViewModel: JobSeekerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCompanyDetailBinding.inflate(
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
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val bundle = Bundle()
                    bundle.putString(AppConstant.JOB_DETAIL , requireArguments().getString(AppConstant.JOB_DETAIL))
                    findNavController().navigate(R.id.openJobDetailsScreen,bundle)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.btBack.setOnClickListener(this)
        binding.btDetails.setOnClickListener(this)
        binding.btPhotos.setOnClickListener(this)

        getCompanyDetails()
        showDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun getCompanyDetails(){
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            jobSeekerViewModel.getCompanyDetails(sessionManager.getBearerToken() ,
                requireArguments().getString(AppConstant.COMPANY_ID).toString()
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        binding.tvProgressBar.visibility = View.GONE
                        try {

                            binding.etCompanyAndStoreName.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["cname"])
                            binding.etAddress.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["address"])

                            binding.etState.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["state_name"])
                            binding.etCity.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["city_name"])
                            binding.etZip.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["zip"])

                            binding.etOpeningHours.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["from_time"]) + " - " + checkFieldSting(
                                    jsonObjectData["data"].asJsonObject["to_time"]
                                )


                            binding.etWorkingHoursBoys.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["boys_from_time"]) + " - " + checkFieldSting(
                                    jsonObjectData["data"].asJsonObject["boys_to_time"]
                                )


                            binding.etWorkingHoursGirls.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["girls_from_time"]) + " - " + checkFieldSting(
                                    jsonObjectData["data"].asJsonObject["girls_to_time"]
                                )

                            binding.etOpeningDays.text = checkFieldSting(jsonObjectData["data"].asJsonObject["opening_days"])


                            binding.etContactNo.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["mobile"])
                            binding.etNoOfFloor.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["floor"])
                            binding.etCompanyLook.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["company_look_label"])

                            if (checkFieldArray(jsonObjectData["data"].asJsonObject["business_type"]) != null){
                                binding.etTypeOfBusiness.text =
                                    checkFieldArray(jsonObjectData["data"].asJsonObject["business_type"])!!.joinToString(",")
                            }


                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["logo"]))
                                .placeholder(R.drawable.company_icon).into(binding.tvProfile)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["banner"]))
                                .placeholder(R.drawable.no_image).into(binding.tvBannerImage)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo1"]))
                                .placeholder(R.drawable.no_image).into(binding.tvIp1)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo2"]))
                                .placeholder(R.drawable.no_image).into(binding.tvIp2)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo3"]))
                                .placeholder(R.drawable.no_image).into(binding.tvIp3)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo4"]))
                                .placeholder(R.drawable.no_image).into(binding.tvEp1)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo5"]))
                                .placeholder(R.drawable.no_image).into(binding.tvEp2)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo6"]))
                                .placeholder(R.drawable.no_image).into(binding.tvEp3)


                            showDetails()

                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }

    }

    // This Function is used for display details screen
    private fun showDetails() {
        binding.btDetails.setTextColor(resources.getColor(R.color.blueTextColor))
        binding.btPhotos.setTextColor(resources.getColor(R.color.lightTextColor))
        binding.tvPersonalDetail.visibility = View.VISIBLE
        binding.tvPhotosBox.visibility = View.GONE
    }

    // This Function is used for display photos screen
    private fun showPhotos() {
        binding.btDetails.setTextColor(resources.getColor(R.color.lightTextColor))
        binding.btPhotos.setTextColor(resources.getColor(R.color.blueTextColor))
        binding.tvPhotosBox.visibility = View.VISIBLE
        binding.tvPersonalDetail.visibility = View.GONE
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.btBack -> {
                val bundle = Bundle()
                bundle.putString(AppConstant.JOB_DETAIL , requireArguments().getString(AppConstant.JOB_DETAIL))
                findNavController().navigate(R.id.openJobDetailsScreen,bundle)
            }

            R.id.btDetails -> {
                showDetails()
            }

            R.id.btPhotos -> {
                showPhotos()
            }

        }
    }





}
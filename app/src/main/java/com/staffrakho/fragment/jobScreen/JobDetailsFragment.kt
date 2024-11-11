package com.staffrakho.fragment.jobScreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.staffrakho.R
import com.staffrakho.dataModel.JobList
import com.staffrakho.databinding.FragmentJobDetailsBinding
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch
import kotlin.random.Random


class JobDetailsFragment : BaseFragment() {

    lateinit var binding: FragmentJobDetailsBinding
    private lateinit var sessionManager: SessionManager
    lateinit var jobSeekerViewModel: JobSeekerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJobDetailsBinding.inflate(
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
                    if (sessionManager.getSourceFragment() == AppConstant.HOME_SCREEN) {
                        findNavController().navigate(R.id.openHomeScreen)
                    } else if (sessionManager.getSourceFragment() == AppConstant.JOB_APPLIED_SCREEN) {
                        findNavController().navigate(R.id.openAppliedJobScreen)
                    } else if (sessionManager.getSourceFragment() == AppConstant.SEARCH_JOB_SCREEN) {
                        val bundle = Bundle()
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
                        findNavController().navigate(R.id.openSearchJobScreen, bundle)
                    } else {
                        alertErrorDialog("Path Not Found")
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val arrayListJson = requireArguments().getString(AppConstant.JOB_DETAIL)
        val arrayType = object : TypeToken<JobList>() {}.type
        val jobDetails: JobList = Gson().fromJson(arrayListJson, arrayType)

        Log.e("Attorney Detail", jobDetails.toString())

        setScreen(jobDetails)


        binding.btBack.setOnClickListener {
            if (sessionManager.getSourceFragment() == AppConstant.HOME_SCREEN) {
                findNavController().navigate(R.id.openHomeScreen)
            } else if (sessionManager.getSourceFragment() == AppConstant.JOB_APPLIED_SCREEN) {
                findNavController().navigate(R.id.openAppliedJobScreen)
            } else if (sessionManager.getSourceFragment() == AppConstant.SEARCH_JOB_SCREEN) {
                val bundle = Bundle()
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
                findNavController().navigate(R.id.openSearchJobScreen, bundle)
            } else {
                alertErrorDialog("Path Not Found")
            }
        }

        binding.tvDefalutImage.setOnClickListener(){
            val bundle = Bundle()
            bundle.putString(AppConstant.JOB_DETAIL , requireArguments().getString(AppConstant.JOB_DETAIL))
            bundle.putString(AppConstant.COMPANY_ID , jobDetails.company_id)
            findNavController().navigate(R.id.openCompanyDetailFragment, bundle)
        }

        binding.tvPhone.setOnClickListener() {
            if (binding.tvPhone.text.isNotEmpty()) {
                val phoneNumber = binding.tvPhone.text.toString()
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            }
        }

        binding.tvEmail.setOnClickListener() {
            if (binding.tvEmail.text.isNotEmpty()) {
                val email = binding.tvEmail.text.toString()
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                    putExtra(Intent.EXTRA_SUBJECT, "") // Optional: set a subject if needed
                    setPackage("com.google.android.gm") // Ensures Gmail is opened
                }

                try {
                    startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {
                    // Handle if Gmail is not installed
                    Toast.makeText(requireContext(), "Gmail app is not installed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.tvWhatsup.setOnClickListener() {
            if (binding.tvWhatsup.text.isNotEmpty()) {
                val phoneNumber = binding.tvWhatsup.text.toString()
                val url = "https://wa.me/$phoneNumber"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.whatsapp")
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "WhatsApp is not installed.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }


        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            lifecycleScope.launch {
                jobSeekerViewModel.visitedJob(sessionManager.getBearerToken(), jobDetails.id)
            }
        }

    }

    // This Function is used for set screen
    private fun setScreen(dataItem: JobList) {
        binding.tvJobTitle.text = dataItem.roles
        binding.tvCompanyName.text = dataItem.company_name
        binding.tvLocation.text = dataItem.address
        binding.tvSalary.text = dataItem.salary_label
        binding.tvPinCode.text = dataItem.pin_code
        binding.tvRequiredEducation.text = dataItem.required_education_label
        binding.tvPhone.text = dataItem.phone
        binding.tvEmail.text = dataItem.email
        binding.tvWhatsup.text = dataItem.whatsapp


        if (dataItem.company_logo == null || dataItem.company_logo == "") {
            binding.tvLogo.visibility = View.GONE
            binding.tvDefalutImage.visibility = View.VISIBLE

            if (dataItem.company_name != null) {
                if (dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                        .joinToString("") { it[0].uppercase() }.length >= 2) {
                    binding.tvDefalutImage.text =
                        dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                            .joinToString("") { it[0].uppercase() }.substring(0, 2)
                } else {
                    binding.tvDefalutImage.text =
                        dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                            .joinToString("") { it[0].uppercase() }
                }
            } else {
                binding.tvDefalutImage.text = null
            }

            when (Random.nextInt(1, 11)) {
                1 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor1
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor1))
                }

                2 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor2
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor2))
                }

                3 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor3
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor3))
                }

                4 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor4
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor4))
                }

                5 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor5
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor5))
                }

                6 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor6
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor6))
                }

                7 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor7
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor7))
                }

                else -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor8
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor8))
                }
            }

        } else {
            binding.tvLogo.visibility = View.VISIBLE
            binding.tvDefalutImage.visibility = View.GONE

            Glide.with(requireActivity()).load(AppConstant.MEDIA_BASE_URL + dataItem.company_logo)
                .into(binding.tvLogo)
        }

        if (dataItem.already_applied == "0") {
            binding.btStatus.setBackgroundResource(R.drawable.btdark_blue_bg)
            binding.btStatus.setTextColor(resources.getColor(R.color.white))
            binding.btStatus.text = getString(R.string.Apply)
        }

        if (dataItem.already_applied == "1") {
            binding.btStatus.setBackgroundResource(R.drawable.btdark_blue_bg)
            binding.btStatus.setTextColor(resources.getColor(R.color.white))
            binding.btStatus.text = getString(R.string.Applied)
        }

        binding.btStatus.setOnClickListener {
            if (dataItem.already_applied == "0" && binding.btStatus.text == getString(R.string.Apply)) {
                if (!isNetworkAvailable()) {
                    alertErrorDialog(getString(R.string.no_internet))
                } else {
                    applyOnThisJob(dataItem.id)
                }

            } else {
                Toast.makeText(requireContext(), "Already Applied Job", Toast.LENGTH_SHORT).show()
            }
        }




        binding.tvLastDate.text = dataItem.last_date
        binding.tvBusinessType.text = dataItem.business_type_label
        binding.tvCompanyLook.text = dataItem.company_look_label
        binding.tvProfileType.text = dataItem.profile_type_label
        binding.tvSubCategory.text = dataItem.sub_category_name
        binding.tvRoles.text = dataItem.roles
        binding.tvJobDescription.text = dataItem.description
        binding.tvJobExprience.text = dataItem.experience_label
        binding.tvCategory.text = dataItem.category_name
        binding.tvTotalVacancy.text = dataItem.number_of_vacancy
        binding.tvPosition.text = dataItem.position
        binding.tvType.text = dataItem.type_label
        binding.tvGender.text = dataItem.gender
        binding.tvState.text = dataItem.state_name
        binding.tvCity.text = dataItem.city_name

    }

    // This Function is used for apply the jobs
    private fun applyOnThisJob(id: Int) {
        binding.tvProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            jobSeekerViewModel.applyTheJob(sessionManager.getBearerToken(), id.toString())
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            binding.btStatus.setBackgroundResource(R.drawable.btdark_blue_bg)
                            binding.btStatus.setTextColor(resources.getColor(R.color.white))
                            binding.btStatus.text = getString(R.string.Applied)
                            Toast.makeText(
                                requireContext(),
                                jsonObjectData["message"].asString,
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            alertErrorDialog(e.message)
                        }
                    }
                }
        }

    }


}
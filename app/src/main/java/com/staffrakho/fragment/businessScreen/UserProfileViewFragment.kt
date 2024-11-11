package com.staffrakho.fragment.businessScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.staffrakho.R
import com.staffrakho.activity.ChatActivity
import com.staffrakho.adapter.FamilyRelationAdapter
import com.staffrakho.adapter.WorkExperienceAdapter
import com.staffrakho.dataModel.JobList
import com.staffrakho.dataModel.UserData
import com.staffrakho.databinding.FragmentUserProfileViewBinding
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class UserProfileViewFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentUserProfileViewBinding
    private var resumeUrl: String? = null

    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel

    private var isShortListed: Int = 0
    private var userId: Int = 0
    private var userName: String? = ""
    private var userProfielPicture: String? = ""

    private var interstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentUserProfileViewBinding.inflate(
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
                    if (sessionManager.getSourceFragment() == AppConstant.HOME_SCREEN) {
                        findNavController().navigate(R.id.openHomeScreen)
                    } else if (sessionManager.getSourceFragment() == AppConstant.APPLICANTS_SCREEN) {
                        findNavController().navigate(R.id.openApplicantsScreen)
                    } else if (sessionManager.getSourceFragment() == AppConstant.POST_JOB_SCREEN) {
                        findNavController().navigate(R.id.openPostJobsScreen)
                    } else if (sessionManager.getSourceFragment() == AppConstant.NEW_PROFILE_SCREEN) {
                        findNavController().navigate(R.id.openNewProfileFragment)
                    } else if (sessionManager.getSourceFragment() == AppConstant.VISITED_PROFILE_SCREEN) {
                        findNavController().navigate(R.id.openVisitedProfileFragment)
                    } else if (sessionManager.getSourceFragment() == AppConstant.SHORTLISTED_PROFILE_SCREEN) {
                        findNavController().navigate(R.id.openShortListedProfileFragment)
                    } else if (sessionManager.getSourceFragment() == AppConstant.SEARCH_EMPLOYEE_SCREEN) {
                        findNavController().navigate(R.id.openHomeScreen)
                    } else {
                        alertErrorDialog("Path Not Found")
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        val arrayListJson = requireArguments().getString(AppConstant.USER_DETAIL)
        val type = object : TypeToken<UserData>() {}.type
        val userData: UserData = Gson().fromJson(arrayListJson, type)

        isShortListed = userData.is_shortlisted
        userId = userData.id

        if (isShortListed == 1) {
            binding.btStatus.text = getString(R.string.already_shortlist)
        } else {
            binding.btStatus.text = getString(R.string.shortlist)
        }

        setProfile(userData)

        binding.btBack.setOnClickListener(this)
         binding.btViewResume.setOnClickListener(this)
        binding.btStatus.setOnClickListener(this)
        binding.btMessage.setOnClickListener(this)

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            lifecycleScope.launch {
                employerViewModel.visitedUser(sessionManager.getBearerToken(), userId)
            }
        }


    }

    // This Function is used for set screen
    private fun setProfile(userData: UserData) {
        userName = userData.name
        userProfielPicture = userData.avatar

        binding.etName.text = userData.name
        binding.etEmail.text = userData.email
        binding.etPhone.text = userData.mobile
        binding.etGender.text = userData.gender_label
        binding.etRole.text = userData.role
        binding.etDob.text = userData.dob
        binding.etBio.text = userData.bio
        binding.etSkill.text = userData.skills
        binding.etTransport.text = userData.transport_facility
        binding.etPhysically.text = userData.physically_fit
        binding.etProfileType.text = userData.profile_type
        binding.etDisability.text = userData.disability

        resumeUrl = userData.resume

        Glide.with(requireActivity())
            .load(AppConstant.MEDIA_BASE_URL + userProfielPicture)
            .placeholder(R.drawable.demo_user).into(binding.tvProfile)


        if (userData.workExperience != null) {
            if (userData.workExperience.isEmpty()) {
                binding.tvNoWorkExperience.visibility = View.VISIBLE
                binding.workExprienceRecycleView.visibility = View.GONE
            } else {
                binding.workExprienceRecycleView.adapter =
                    WorkExperienceAdapter(userData.workExperience, requireContext(), 0)
                binding.workExprienceRecycleView.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.tvNoWorkExperience.visibility = View.GONE
                binding.workExprienceRecycleView.visibility = View.VISIBLE
            }

        } else {
            binding.tvNoWorkExperience.visibility = View.VISIBLE
            binding.workExprienceRecycleView.visibility = View.GONE
        }


        if (userData.family != null) {
            if (userData.family.isEmpty()) {
                binding.tvNoFamilyRecord.visibility = View.VISIBLE
                binding.familyBackgroundRecycleView.visibility = View.GONE

            } else {
                binding.familyBackgroundRecycleView.adapter =
                    FamilyRelationAdapter(userData.family, requireContext(), 0)
                binding.familyBackgroundRecycleView.layoutManager =
                    LinearLayoutManager(requireContext())

                binding.tvNoFamilyRecord.visibility = View.GONE
                binding.familyBackgroundRecycleView.visibility = View.VISIBLE
            }

        } else {
            binding.tvNoFamilyRecord.visibility = View.VISIBLE
            binding.familyBackgroundRecycleView.visibility = View.GONE
        }

    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireActivity(), AppConstant.ADD_UNIT_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null  // Load a new ad after the old one is dismissed
                            showJobDetailsPage()
                        }

                        override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                            interstitialAd = null
                            showJobDetailsPage()
                        }
                    }
                    showInterstitialAd()
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    interstitialAd = null
                }
            })
    }

    private fun showInterstitialAd() {
        interstitialAd?.show(requireActivity()) ?: run {
            showJobDetailsPage()
        }
    }

    private fun showJobDetailsPage(){
        createChannelList()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btMessage -> {
                loadInterstitialAd()
            }

            R.id.btStatus -> {
                if (isShortListed == 0 && binding.btStatus.text == getString(R.string.shortlist)) {
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        shortListUser()
                    }
                } else if (isShortListed == 1 && binding.btStatus.text == getString(R.string.already_shortlist)) {
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        deleteShortListUser()
                    }
                }
            }


            R.id.btBack -> {
                if (sessionManager.getSourceFragment() == AppConstant.HOME_SCREEN) {
                    findNavController().navigate(R.id.openHomeScreen)
                } else if (sessionManager.getSourceFragment() == AppConstant.APPLICANTS_SCREEN) {
                    findNavController().navigate(R.id.openApplicantsScreen)
                } else if (sessionManager.getSourceFragment() == AppConstant.NEW_PROFILE_SCREEN) {
                    findNavController().navigate(R.id.openNewProfileFragment)
                } else if (sessionManager.getSourceFragment() == AppConstant.VISITED_PROFILE_SCREEN) {
                    findNavController().navigate(R.id.openVisitedProfileFragment)
                } else if (sessionManager.getSourceFragment() == AppConstant.SHORTLISTED_PROFILE_SCREEN) {
                    findNavController().navigate(R.id.openShortListedProfileFragment)
                } else if (sessionManager.getSourceFragment() == AppConstant.SEARCH_EMPLOYEE_SCREEN) {
                    findNavController().navigate(R.id.openHomeScreen)
                } else {
                    alertErrorDialog("Path Not Found")
                }
            }


            R.id.btViewResume -> {
                if (resumeUrl != null) {
                    val bundle = Bundle()
                    bundle.putString(
                        AppConstant.PDF_URL,
                        AppConstant.MEDIA_BASE_URL + resumeUrl
                    )
                    bundle.putString(
                        AppConstant.USER_DETAIL,
                        requireArguments().getString(AppConstant.USER_DETAIL)
                    )
                    bundle.putString(
                        AppConstant.SOURCE_FRAGMENT,
                        AppConstant.USER_PROFILE_VIEW_SCREEN
                    )
                    findNavController().navigate(R.id.openPDFScreen, bundle)
                } else {
                    Toast.makeText(requireContext(), "Resume is not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    // This Function is used for apply the jobs
    private fun shortListUser() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            employerViewModel.shortListUser(sessionManager.getBearerToken(), userId)
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            binding.btStatus.setBackgroundResource(R.drawable.btdark_blue_bg)
                            binding.btStatus.setTextColor(resources.getColor(R.color.white))
                            binding.btStatus.text = getString(R.string.already_shortlist)
                            isShortListed = 1
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

    private fun createChannelList() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            employerViewModel.createChannelList(
                sessionManager.getBearerToken(),
                userId.toString()
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                            val data = checkFieldObject(jsonObjectData["data"])

                            if (data != null) {
                                val channelId = checkFieldSting(data.get("id"))
                                val intent = Intent(requireActivity(), ChatActivity::class.java)
                                intent.putExtra(AppConstant.CHANNEL_ID, channelId)
                                intent.putExtra(AppConstant.RECEIVER_NAME, userName)
                                intent.putExtra(
                                    AppConstant.RECEIVER_PROFILE_PICTURE,
                                    userProfielPicture
                                )
                                requireActivity().startActivity(intent)
                            }


                        } catch (e: Exception) {
                            alertErrorDialog(e.message)
                        }
                    }
                }
        }

    }


    private fun deleteShortListUser() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {

            employerViewModel.deleteShortListUser(sessionManager.getBearerToken(), userId)
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            binding.btStatus.setBackgroundResource(R.drawable.btdark_blue_bg)
                            binding.btStatus.setTextColor(resources.getColor(R.color.white))
                            binding.btStatus.text = getString(R.string.shortlist)
                            isShortListed = 0
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
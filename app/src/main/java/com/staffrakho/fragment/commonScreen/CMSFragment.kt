package com.staffrakho.fragment.commonScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.dataModel.CMSDataModel
import com.staffrakho.databinding.FragmentCmsBinding
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class CMSFragment : BaseFragment() {


    lateinit var binding: FragmentCmsBinding
    lateinit var sessionManager: SessionManager
    lateinit var jobSeekerViewModel: JobSeekerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentCmsBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        jobSeekerViewModel = ViewModelProvider(this)[JobSeekerViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.openMenuScreen)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.openMenuScreen)
        }


        setScreen()

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            getCMS()
        }

    }

    // This Function is used for set screen
    @SuppressLint("SetTextI18n")
    private fun setScreen() {
        when (sessionManager.getSourceFragment()) {

            AppConstant.TERM_AND_CONDITION -> {
                binding.tvPageTitle.text = getString(R.string.terms_amp_conditions)
                binding.tvImage.visibility = View.VISIBLE
            }

            AppConstant.PRIVACY_AND_POLICY -> {
                binding.tvPageTitle.text = getString(R.string.privacy_policy)
                binding.tvImage.visibility = View.VISIBLE
            }

            AppConstant.ABOUT_US -> {
                binding.tvPageTitle.text = getString(R.string.about_us)
                binding.tvImage.visibility = View.VISIBLE
            }

            AppConstant.CONTACT_US -> {
                binding.tvPageTitle.text = getString(R.string.contact_us)
                binding.tvImage.visibility = View.GONE
            }

            else -> {
                alertErrorDialog(getString(R.string.route_not_found))
            }

        }
    }

    // This Function is used for get CMS Data from database
    private fun getCMS() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            jobSeekerViewModel.getCMS(
                sessionManager.getBearerToken(),
            ).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {

                    try {
                        val cmsData = Gson().fromJson(jsonObjectData, CMSDataModel::class.java)

                         when (sessionManager.getSourceFragment()) {

                            AppConstant.TERM_AND_CONDITION -> {
                                binding.tvHead.text = cmsData.data.terms_conditions.page_title
                                binding.tvContent.text = htmlToPlainText(cmsData.data.terms_conditions.page_content)
                                Glide.with(requireActivity())
                                    .load(AppConstant.MEDIA_BASE_URL + cmsData.data.terms_conditions.image)
                                    .into(binding.tvImage)
                            }

                            AppConstant.PRIVACY_AND_POLICY -> {
                                binding.tvHead.text = cmsData.data.privacy_policy.page_title
                                binding.tvContent.text = htmlToPlainText(cmsData.data.privacy_policy.page_content)
                                Glide.with(requireActivity())
                                    .load(AppConstant.MEDIA_BASE_URL + cmsData.data.privacy_policy.image)
                                    .into(binding.tvImage)
                            }

                            AppConstant.ABOUT_US -> {
                                binding.tvHead.text = cmsData.data.about_us.page_title
                                binding.tvContent.text = htmlToPlainText(cmsData.data.about_us.page_content)
                                Glide.with(requireActivity())
                                    .load(AppConstant.MEDIA_BASE_URL + cmsData.data.about_us.image)
                                    .into(binding.tvImage)
                            }

                            AppConstant.CONTACT_US -> {
                                binding.tvHead.text = cmsData.data.contact_us.page_title
                                binding.tvContent.text = htmlToPlainText(cmsData.data.contact_us.page_content)
                                Glide.with(requireActivity())
                                    .load(AppConstant.MEDIA_BASE_URL + cmsData.data.contact_us.image)
                                    .into(binding.tvImage)
                            }

                            else -> {
                                alertErrorDialog(getString(R.string.route_not_found))
                            }

                        }

                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }

        }

    }

}
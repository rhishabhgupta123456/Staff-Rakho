package com.staffrakho.fragment.jobScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.staffrakho.databinding.FragmentFavouriteBinding
import com.staffrakho.databinding.FragmentNewJobsBinding
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class FavouriteFragment : BaseFragment() {


    lateinit var binding: FragmentFavouriteBinding
    private lateinit var sessionManager: SessionManager
    lateinit var jobSeekerViewModel: JobSeekerViewModel

    private var interstitialAd: InterstitialAd? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavouriteBinding.inflate(
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

        binding.btBack.setOnClickListener() {
            findNavController().navigate(R.id.openHomeScreen)
        }


        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            getFavouriteJobs()
        }

    }


    private fun getFavouriteJobs() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            jobSeekerViewModel.getFavouriteJobs(
                sessionManager.getBearerToken(),
            ).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        val jobList =
                            Gson().fromJson(jsonObjectData, AppliedJobDataModel::class.java)
                        binding.recycleView.layoutManager =
                            LinearLayoutManager(requireContext())
                        val jobAdapter =
                            JobsListAdapter(jobList.data, requireContext())
                        binding.recycleView.adapter = jobAdapter
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

                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }

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
        sessionManager.setSourceFragment(AppConstant.FAVORITE_JOBS)
        val bundle = Bundle()
        bundle.putString(AppConstant.JOB_DETAIL, Gson().toJson(item))
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

                            getFavouriteJobs()


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

                            getFavouriteJobs()


                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }

    }


}
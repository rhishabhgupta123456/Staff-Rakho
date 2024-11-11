package com.staffrakho.fragment.jobScreen

import android.os.Bundle
import android.util.Log
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
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.activity.JobActivity
import com.staffrakho.adapter.JobsListAdapter
import com.staffrakho.dataModel.AppliedJobDataModel
import com.staffrakho.dataModel.JobList
import com.staffrakho.databinding.FragmentAppliedJobBinding
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class AppliedJobFragment : BaseFragment() {

    lateinit var binding: FragmentAppliedJobBinding
    private lateinit var sessionManager: SessionManager
    lateinit var jobSeekerViewModel: JobSeekerViewModel

    private var sortOrder: String = "desc"
    private var sortField: String = "created_at"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAppliedJobBinding.inflate(
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
                    findNavController().navigate(R.id.openHomeScreen)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        sessionManager.setSourceFragment(AppConstant.JOB_APPLIED_SCREEN)

        binding.appliedJobRefresh.setOnRefreshListener {
            if (!isNetworkAvailable()) {
                binding.appliedJobRefresh.isRefreshing = false
                alertErrorDialog(getString(R.string.no_internet))
            } else {
                getAppliedJob()
            }
        }

        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.openHomeScreen)
        }


        binding.sortIcon.setOnClickListener {
            openMenu()
        }

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            getAppliedJob()
        }


    }

    private fun openMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.sortIcon)
        popupMenu.menuInflater.inflate(R.menu.sort_job_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
            when (menuItem.itemId) {

                R.id.btDateAcc -> {
                    sortOrder = "asc"
                    sortField = "created_at"
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        getAppliedJob()
                    }
                }

                R.id.btDateDesc -> {
                    sortOrder = "desc"
                    sortField = "created_at"
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        getAppliedJob()
                    }
                }

                R.id.btSalaryAcc -> {
                    sortOrder = "asc"
                    sortField = "salary"
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        getAppliedJob()
                    }
                }

                R.id.btSalaryDesc -> {
                    sortOrder = "desc"
                    sortField = "salary"
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        getAppliedJob()
                    }
                }

            }
            true
        }


        // Showing the popup menu
        popupMenu.show()
    }


    // This Function is used for get all Applied jobs from database
    private fun getAppliedJob() {
        binding.tvProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            jobSeekerViewModel.getAppliedJob(
                sessionManager.getBearerToken(),
                sortOrder,
                sortField
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.appliedJobRefresh.isRefreshing = false
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            val jobList =
                                Gson().fromJson(jsonObjectData, AppliedJobDataModel::class.java)

                            Log.e("Applied Job", jobList.toString())

                            if (jobList.data.isEmpty()) {
                                binding.noRecordDataBox.visibility = View.VISIBLE
                            } else {
                                binding.noRecordDataBox.visibility = View.GONE

                            }

                            binding.appliedJobRecyclerView.layoutManager =
                                LinearLayoutManager(requireContext())
                            val jobAdapter = JobsListAdapter(jobList.data, requireContext(),)
                            binding.appliedJobRecyclerView.adapter = jobAdapter

                            jobAdapter.setOnRequestAction(object : JobsListAdapter.OnRequestAction {
                                override fun viewJobs(item: JobList) {
                                    sessionManager.setSourceFragment(AppConstant.JOB_APPLIED_SCREEN)
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
                            binding.noRecordDataBox.visibility = View.VISIBLE
                            alertErrorDialog(e.message)
                        }
                    } else {
                        binding.noRecordDataBox.visibility = View.VISIBLE
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

                            getAppliedJob()


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

                            getAppliedJob()


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
        activity.openAppliedJobScreen()
    }

}
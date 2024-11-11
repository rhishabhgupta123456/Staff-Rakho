package com.staffrakho.fragment.businessScreen

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.activity.EmployerActivity
import com.staffrakho.adapter.ApplicantsAdapter
import com.staffrakho.adapter.PostJobAdapter
import com.staffrakho.adapter.UserProfileAdapter
import com.staffrakho.dataModel.MyJobs
import com.staffrakho.dataModel.PostJobDataModel
import com.staffrakho.dataModel.UserProfileDataModel
import com.staffrakho.databinding.FragmentPostJobBinding
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetJobDialog
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class PostJobFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentPostJobBinding
    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentPostJobBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
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


        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.openHomeScreen)
        }



        binding.btOpenPostJob.setOnClickListener(){
            openPostJob()
        }


        binding.btOpenApplicantRecieve.setOnClickListener(){
            openApplicantList()
        }

        binding.btAddJob.setOnClickListener(this)

        openPostJob()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btAddJob -> {
                val bottomSheetFragment = BottomSheetJobDialog(null)
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
            }

         }
    }


    private fun openPostJob(){
        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            getPostJobs()
        }

        binding.subHeader.visibility = View.VISIBLE

    }

    private fun openApplicantList(){
        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            getApplicantsList()
        }

        binding.subHeader.visibility = View.GONE

    }


    // This Function is used for get applicants list from database
    private fun getApplicantsList() {
        binding.recyclerView.visibility = View.GONE
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            employerViewModel.getApplicantsList(sessionManager.getBearerToken()).observe(viewLifecycleOwner) { jsonObject ->
                binding.recyclerView.visibility = View.VISIBLE

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

                        binding.recyclerView.adapter =
                            ApplicantsAdapter(jobList.data, requireActivity(), AppConstant.POST_JOB_SCREEN)
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

                    } catch (e: Exception) {
                        binding.recyclerView.visibility = View.GONE
                        binding.noRecordDataBox.visibility = View.VISIBLE
                        alertErrorDialog(e.message.toString())
                    }
                }else{
                    binding.recyclerView.visibility = View.GONE
                    binding.noRecordDataBox.visibility = View.VISIBLE
                }

            }

        }
    }

    // This Function is used for add new jobs
    private fun getPostJobs() {
        binding.recyclerView.visibility = View.GONE
        binding.tvProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            employerViewModel.getAllMyJobs(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            val jobList =
                                Gson().fromJson(jsonObjectData, PostJobDataModel::class.java)
                            binding.recyclerView.visibility = View.VISIBLE

                            if (jobList.data.isEmpty()) {
                                binding.noRecordDataBox.visibility = View.VISIBLE
                            } else {
                                binding.noRecordDataBox.visibility = View.GONE

                            }

                            Log.e("Applied Job", jobList.toString())

                            val adapter = PostJobAdapter(jobList.data, requireActivity())
                            binding.recyclerView.adapter = adapter
                            binding.recyclerView.layoutManager =
                                LinearLayoutManager(requireContext())

                            adapter.setOnRequestAction(object : PostJobAdapter.OnRequestAction {
                                override fun editItem(item: MyJobs) {
                                    editJobs(item)
                                }

                                override fun deleteItem(item: MyJobs) {
                                    if (!isNetworkAvailable()) {
                                        alertErrorDialog(getString(R.string.no_internet))
                                    } else {
                                        deleteAccountAlertBox(item)
                                    }

                                }

                                override fun viewItem(item: MyJobs) {
                                    val bundle = Bundle()
                                    bundle.putString(AppConstant.JOB_DETAIL, Gson().toJson(item))
                                    findNavController().navigate(R.id.openPostJobViewScreen, bundle)
                                }

                                override fun changeJobState(item: MyJobs) {
                                    changeJobStateActivate(item)
                                }

                                override fun viewApplicantsItem(item: MyJobs) {
                                    sessionManager.setJobId(item.id)
                                    findNavController().navigate(R.id.openApplicantsScreen)
                                }

                            })

                        } catch (e: Exception) {
                            binding.noRecordDataBox.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                            alertErrorDialog(e.message)
                        }
                    } else {
                        binding.recyclerView.visibility = View.GONE
                        binding.noRecordDataBox.visibility = View.VISIBLE
                    }
                }
        }

    }

    // This Function is used for open delete account alert dialog
    @SuppressLint("SetTextI18n")
    private fun deleteAccountAlertBox(item: MyJobs) {
        val deleteAccountDialog = Dialog(requireContext())
        deleteAccountDialog.setContentView(R.layout.delete_item_alert_dialog)
        deleteAccountDialog.setCancelable(true)

        val btYes: TextView = deleteAccountDialog.findViewById(R.id.yes)
        val btNo: TextView = deleteAccountDialog.findViewById(R.id.no)
        val title: TextView = deleteAccountDialog.findViewById(R.id.title)

        title.text = getString(R.string.are_you_want_to_delete_this_job)

        btNo.setOnClickListener {
            deleteAccountDialog.dismiss()
        }

        btYes.setOnClickListener {
            deleteAccountDialog.dismiss()
            if (!isNetworkAvailable()) {
                alertErrorDialog(getString(R.string.no_internet))
            } else {
                if (!isNetworkAvailable()) {
                    alertErrorDialog(getString(R.string.no_internet))
                } else {
                    deleteJobs(item)
                }

            }

        }

        deleteAccountDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteAccountDialog.show()
    }

    // This Function is used for edit job
    private fun editJobs(item: MyJobs) {
        val bottomSheetFragment = BottomSheetJobDialog(item)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    // This Function is used for delete job
    private fun deleteJobs(item: MyJobs) {
        lifecycleScope.launch {
            employerViewModel.deleteMyJobs(
                sessionManager.getBearerToken(),
                item.id
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.job_deleted_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        if (!isNetworkAvailable()) {
                            alertErrorDialog(getString(R.string.no_internet))
                        } else {
                            getPostJobs()
                        }
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }
    }

    // This Function is used for delete job
    private fun changeJobStateActivate(item: MyJobs) {
        lifecycleScope.launch {
            employerViewModel.changeJobStateActivate(
                sessionManager.getBearerToken(),
                item.id
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        if (!isNetworkAvailable()) {
                            alertErrorDialog(getString(R.string.no_internet))
                        } else {
                            getPostJobs()
                        }
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as EmployerActivity
        activity.openMyJobScreen()
    }


}
package com.staffrakho.fragment.commonScreen

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
import com.staffrakho.activity.EmployerActivity
import com.staffrakho.activity.JobActivity
import com.staffrakho.adapter.MessageInboxAdapter
import com.staffrakho.dataModel.MessageInboxDataModel
import com.staffrakho.dataModel.MessageInboxList
import com.staffrakho.databinding.FragmentMessageInboxBinding
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class MessageInboxFragment : BaseFragment() {

    private lateinit var binding: FragmentMessageInboxBinding
    private lateinit var sessionManager: SessionManager
    lateinit var jobSeekerViewModel: JobSeekerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMessageInboxBinding.inflate(
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

        binding.notificationRefresh.setOnRefreshListener {
            getChannelList()
        }

        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.openHomeScreen)
        }


    }




    private fun getChannelList() {

        binding.tvProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            jobSeekerViewModel.getChannelList(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.notificationRefresh.isRefreshing = false
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            val channelList =
                                Gson().fromJson(jsonObjectData, MessageInboxDataModel::class.java)

                            val adapter =         MessageInboxAdapter(channelList.data, requireActivity())
                            binding.notificationRecyclerView.adapter = adapter

                            binding.notificationRecyclerView.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )

                            adapter.setOnRequestAction(object : MessageInboxAdapter.OnRequestAction{
                                override fun clearChat(item: MessageInboxList) {
                                    clearAllChat(item)
                                }

                                override fun deleteChat(item: MessageInboxList) {
                                    deleteChannel(item)
                                }

                            } )


                        } catch (e: Exception) {
                            alertErrorDialog(e.message)
                        }
                    }
                }
        }

    }


    override fun onResume() {
        super.onResume()
        getChannelList()
        if (sessionManager.getUserType() == AppConstant.JOB_PERSON) {
            val activity = requireActivity() as JobActivity
            activity.openMessageScreen()
        } else {
            val activity = requireActivity() as EmployerActivity
            activity.openMessageScreen()
        }
    }

    private fun deleteChannel(item: MessageInboxList) {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {

            jobSeekerViewModel.deleteChannel(sessionManager.getBearerToken(), item.id.toInt())
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            getChannelList()
                        } catch (e: Exception) {
                            alertErrorDialog(e.message)
                        }
                    }
                }
        }

    }

    private fun clearAllChat(item: MessageInboxList) {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {

            jobSeekerViewModel.clearAllChat(sessionManager.getBearerToken(), item.id.toInt(), sessionManager.getUserId().toInt())
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {

                        } catch (e: Exception) {
                            alertErrorDialog(e.message)
                        }
                    }
                }
        }

    }


}
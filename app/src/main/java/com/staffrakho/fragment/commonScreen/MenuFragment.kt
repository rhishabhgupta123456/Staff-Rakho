package com.staffrakho.fragment.commonScreen

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.staffrakho.R
import com.staffrakho.activity.EmployerActivity
import com.staffrakho.activity.JobActivity
import com.staffrakho.databinding.FragmentMenuBinding
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch


class MenuFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentMenuBinding
    private lateinit var sessionManager: SessionManager
    lateinit var jobSeekerViewModel: JobSeekerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMenuBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
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

        binding.btAboutUS.setOnClickListener(this)
        binding.btAccountSetting.setOnClickListener(this)
        binding.btPrivacyAndPolicy.setOnClickListener(this)
        binding.btTermAndCondition.setOnClickListener(this)
        binding.btMyProfile.setOnClickListener(this)
        binding.btDeleteAccount.setOnClickListener(this)
        binding.btContactUs.setOnClickListener(this)
        binding.btLogOut.setOnClickListener(this)
        binding.btBack.setOnClickListener(this)
        binding.btLanguageChange.setOnClickListener(this)

        binding.tvLanguage.text = sessionManager.getLanguage()

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.btBack -> {
                findNavController().navigate(R.id.openHomeScreen)
            }

            R.id.btAccountSetting ->{
                findNavController().navigate(R.id.openAccountSettingFragment)
            }

            R.id.btMyProfile -> {
                sessionManager.setSourceFragment(AppConstant.MENU_SCREEN)
                val bundle = Bundle()
                bundle.putString(AppConstant.SOURCE_FRAGMENT , AppConstant.MENU_SCREEN)
                findNavController().navigate(R.id.openProfileScreen,bundle)
            }

            R.id.btAboutUS -> {
                sessionManager.setSourceFragment(AppConstant.ABOUT_US)
                findNavController().navigate(R.id.openCMSScreen)
            }

            R.id.btPrivacyAndPolicy -> {
                sessionManager.setSourceFragment(AppConstant.PRIVACY_AND_POLICY)
                findNavController().navigate(R.id.openCMSScreen)
            }

            R.id.btTermAndCondition -> {
                sessionManager.setSourceFragment(AppConstant.TERM_AND_CONDITION)
                findNavController().navigate(R.id.openCMSScreen)
            }

            R.id.btContactUs -> {
                sessionManager.setSourceFragment(AppConstant.CONTACT_US)
                findNavController().navigate(R.id.openCMSScreen)
            }

            R.id.btDeleteAccount -> {
                deleteAccountAlertBox()
            }

            R.id.btLogOut -> {
                logOutAlertBox()
            }

            R.id.btLanguageChange -> {
                if (sessionManager.getLanguage() == getString(R.string.english)) {
                    setLocale("hi")
                    binding.tvLanguage.text = getString(R.string.hindi)
                    sessionManager.setLanguage(getString(R.string.hindi))
                    findNavController().navigate(R.id.openMenuScreen)
                } else {
                    setLocale("eng")
                    binding.tvLanguage.text = getString(R.string.english)
                    sessionManager.setLanguage(getString(R.string.english))
                    findNavController().navigate(R.id.openMenuScreen)
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        if (sessionManager.getUserType() == AppConstant.JOB_PERSON) {
            val activity = requireActivity() as JobActivity
            activity.openMenuScreen()
        } else {
            val activity = requireActivity() as EmployerActivity
            activity.openMenuScreen()
        }
    }

    // This Function is used for open delete alert box
    private fun deleteAccountAlertBox() {
        val deleteAccountDialog = Dialog(requireContext())
        deleteAccountDialog.setContentView(R.layout.delete_alert_dialog)
        deleteAccountDialog.setCancelable(true)

        val btYes: TextView = deleteAccountDialog.findViewById(R.id.yes)
        val btNo: TextView = deleteAccountDialog.findViewById(R.id.no)

        btNo.setOnClickListener {
            deleteAccountDialog.dismiss()
        }

        btYes.setOnClickListener {
            deleteAccountDialog.dismiss()
            if (!isNetworkAvailable()) {
                alertErrorDialog(getString(R.string.no_internet))
            } else {
                deleteUser()
            }

        }

        deleteAccountDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteAccountDialog.show()
    }

    // This Function is used for open log out alert box
    private fun logOutAlertBox() {
        val logOutAlertDialog = Dialog(requireContext())
        logOutAlertDialog.setContentView(R.layout.log_out_alert_dialog)
        logOutAlertDialog.setCancelable(false)

        val yes: TextView = logOutAlertDialog.findViewById(R.id.yes)
        val no: TextView = logOutAlertDialog.findViewById(R.id.no)

        no.setOnClickListener {
            logOutAlertDialog.dismiss()
        }

        yes.setOnClickListener {
            logOutAlertDialog.dismiss()
            if (!isNetworkAvailable()) {
                alertErrorDialog(getString(R.string.no_internet))
            } else {
                logOutAccount()
            }
        }

        logOutAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logOutAlertDialog.show()
    }

    // This Function is used for delete user screen
    private fun deleteUser() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            jobSeekerViewModel.deleteAccount(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    binding.tvProgressBar.visibility = View.GONE
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            logOutAccount()
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }

                }
        }

    }

}
package com.staffrakho.fragment.commonScreen

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.staffrakho.R
import com.staffrakho.databinding.FragmentAccountSettingBinding
import com.staffrakho.databinding.FragmentCmsBinding
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import com.staffrakho.utility.ValidationData
import kotlinx.coroutines.launch

class AccountSettingFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentAccountSettingBinding
    lateinit var sessionManager: SessionManager

    lateinit var jobSeekerViewModel: JobSeekerViewModel
    private var eye = false
    private var confirmEye = false
    private var currentEye = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentAccountSettingBinding.inflate(
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
                    findNavController().navigate(R.id.openMenuScreen)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.btBack.setOnClickListener(this)
        binding.btSavePhone.setOnClickListener(this)
        binding.btSaveEmail.setOnClickListener(this)
        binding.btChangeEmail.setOnClickListener(this)
        binding.btChangePhone.setOnClickListener(this)
        binding.btChangePassword.setOnClickListener(this)
        binding.confirmEye.setOnClickListener(this)
        binding.Eye.setOnClickListener(this)
        binding.currentEye.setOnClickListener(this)
        binding.btResetPassword.setOnClickListener(this)

        openDefaultScreen()
        binding.tvPhone.text = sessionManager.getUserPhone()
        binding.tvEmail.text = sessionManager.getUserEmail()


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btBack -> {
                findNavController().navigate(R.id.openMenuScreen)
            }

            R.id.btChangePhone -> {
                openChangePhone()
            }

            R.id.btResetPassword -> {
                if (checkValidationForChangePassword()) {
                    changePassword()
                }
            }

            R.id.btChangePassword -> {
                openChangePassword()
            }

            R.id.btSavePhone -> {
                if (binding.etPhone.text.isEmpty()) {
                    alertErrorDialog(getString(R.string.fill_phone))
                } else if (!ValidationData.isPhoneNumber(binding.etPhone.text.toString())) {
                    alertErrorDialog(getString(R.string.fill_valid_phone))
                } else {
                    if (binding.mobileVerificationBox.visibility == View.VISIBLE) {
                        mobileUpdate()
                    } else {
                        mobileUpdateRequest()
                    }
                }
            }

            R.id.Eye -> {
                if (eye) {
                    eye = false
                    binding.Eye.setImageResource(R.drawable.hidepass_icon)
                    binding.etPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                } else {
                    eye = true
                    binding.Eye.setImageResource(R.drawable.showpass_icon)
                    binding.etPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }

            }

            R.id.currentEye -> {
                if (currentEye) {
                    currentEye = false
                    binding.currentEye.setImageResource(R.drawable.hidepass_icon)
                    binding.etCurrentPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                } else {
                    currentEye = true
                    binding.currentEye.setImageResource(R.drawable.showpass_icon)
                    binding.etCurrentPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }

            }

            R.id.confirmEye -> {
                if (confirmEye) {
                    confirmEye = false
                    binding.confirmEye.setImageResource(R.drawable.hidepass_icon)
                    binding.etConfirmPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()

                } else {
                    confirmEye = true
                    binding.confirmEye.setImageResource(R.drawable.showpass_icon)
                    binding.etConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }

            }


            R.id.btChangeEmail -> {
                openChangeEmail()
            }

            R.id.btSaveEmail -> {
                if (binding.etEmail.text.isEmpty()) {
                    alertErrorDialog(getString(R.string.fill_email))
                } else if (!ValidationData.isEmail(binding.etEmail.text.toString())) {
                    alertErrorDialog(getString(R.string.fill_valid_email))
                } else {
                    if (binding.emailVerificationBox.visibility == View.VISIBLE) {
                        emailUpdate()
                    } else {
                        emailUpdateRequest()
                    }
                }
            }


        }

    }

    private fun checkValidationForChangePassword(): Boolean {
        if (binding.etCurrentPassword.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_current_password))
            return false
        } else if (binding.etPassword.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_new_password))
            return false
        } else if (binding.etConfirmPassword.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_confirm_new_password))
            return false
        } else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()){
            alertErrorDialog(getString(R.string.new_password_and_confirm_new_password_is_not_matched))
            return false
        }else {
            return true
        }


    }



    private fun mobileUpdateRequest() {
        binding.tvProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            jobSeekerViewModel.mobileUpdateRequest(
                sessionManager.getBearerToken(),
                binding.etPhone.text.toString(),
                sessionManager.getUserType()
            ).observe(viewLifecycleOwner) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE
                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    try {
                        binding.etPhone.isEnabled = false
                        binding.mobileVerificationBox.visibility = View.VISIBLE
                        binding.btSavePhone.text = getString(R.string.change_mobile_number)
                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }
        }

    }

    private fun mobileUpdate() {
        binding.tvProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.GONE
            jobSeekerViewModel.mobileUpdate(
                sessionManager.getBearerToken(),
                binding.etPhone.text.toString(),
                binding.etMobileOTP.text.toString(),
                sessionManager.getUserType()
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        binding.tvPhone.text = binding.etPhone.text.toString()
                        sessionManager.setUserPhone(binding.etPhone.text.toString())
                        openDefaultScreen()
                        binding.etPhone.text = null
                        binding.etMobileOTP.text = null
                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }
        }
    }

    private fun emailUpdateRequest() {

        Log.e("Email", binding.etEmail.text.toString())

        binding.tvProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            jobSeekerViewModel.emailUpdateRequest(
                sessionManager.getBearerToken(),
                binding.etEmail.text.toString(),
                sessionManager.getUserType()
            ).observe(viewLifecycleOwner) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE
                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    try {
                        binding.etEmail.isEnabled = false
                        binding.emailVerificationBox.visibility = View.VISIBLE
                        binding.btSaveEmail.text = getString(R.string.change_email)
                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }
        }

    }

    private fun changePassword() {

        Log.e("Email", binding.etEmail.text.toString())

        binding.tvProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            jobSeekerViewModel.changePassword(
                sessionManager.getBearerToken(),
                binding.etCurrentPassword.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfirmPassword.text.toString(),
            ).observe(viewLifecycleOwner) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE
                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    try {
                        Toast.makeText(requireContext(),
                            getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show()
                        logOutAccount()
                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }
        }

    }

    private fun emailUpdate() {
        binding.tvProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.GONE
            jobSeekerViewModel.emailUpdate(
                sessionManager.getBearerToken(),
                binding.etEmail.text.toString(),
                binding.etEmailOTP.text.toString(),
                sessionManager.getUserType()
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        binding.tvEmail.text = binding.etEmail.text.toString()
                        sessionManager.setUserEmail(binding.etEmail.text.toString())
                        openDefaultScreen()
                        binding.etEmail.text = null
                        binding.etEmailOTP.text = null
                    } catch (e: Exception) {
                        alertErrorDialog(e.message.toString())
                    }
                }

            }
        }

    }


    private fun openDefaultScreen() {
        binding.showEmailBox.visibility = View.VISIBLE
        binding.btChangeEmail.visibility = View.VISIBLE
        binding.editEmailBox.visibility = View.GONE
        binding.emailVerificationBox.visibility = View.GONE
        binding.showPhoneBox.visibility = View.VISIBLE
        binding.btChangePhone.visibility = View.VISIBLE
        binding.editPhoneBox.visibility = View.GONE
        binding.mobileVerificationBox.visibility = View.GONE
        binding.btSavePhone.text = getString(R.string.continue1)
        binding.btSaveEmail.text = getString(R.string.continue1)
        binding.editPasswordBox.visibility = View.GONE
    }

    private fun openChangeEmail() {
        binding.showEmailBox.visibility = View.VISIBLE
        binding.btChangeEmail.visibility = View.GONE
        binding.emailVerificationBox.visibility = View.GONE
        binding.editEmailBox.visibility = View.VISIBLE
        binding.editPasswordBox.visibility = View.GONE
    }

    private fun openChangePhone() {
        binding.showPhoneBox.visibility = View.VISIBLE
        binding.btChangePhone.visibility = View.GONE
        binding.mobileVerificationBox.visibility = View.GONE
        binding.editPhoneBox.visibility = View.VISIBLE
        binding.editPasswordBox.visibility = View.GONE
    }

    private fun openChangePassword() {
        binding.showPhoneBox.visibility = View.GONE
        binding.btChangePhone.visibility = View.GONE
        binding.mobileVerificationBox.visibility = View.GONE
        binding.editPhoneBox.visibility = View.GONE
        binding.editPasswordBox.visibility = View.VISIBLE
    }


}
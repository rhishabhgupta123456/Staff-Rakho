package com.staffrakho.fragment.authScreens

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.staffrakho.R
import com.staffrakho.databinding.FragmentResetPasswordScreenBinding
import com.staffrakho.networkModel.authNetworkPannel.AuthViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import com.staffrakho.utility.ValidationData
import kotlinx.coroutines.launch


class ResetPasswordScreen : BaseFragment()  , View.OnClickListener {

    private lateinit var binding : FragmentResetPasswordScreenBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var authViewModel: AuthViewModel
    private var confirmEye = false
    private var eye = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordScreenBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.openForgetPasswordScreen)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.btBack.setOnClickListener(this)
        binding.btResetPassword.setOnClickListener(this)
        binding.confirmEye.setOnClickListener(this)
        binding.Eye.setOnClickListener(this)


    }

    override fun onClick(p0: View?) {
        when(p0!!.id){

            R.id.btBack ->{
                findNavController().navigate(R.id.openForgetPasswordScreen)
            }

            R.id.btResetPassword ->{
                if (binding.etPassword.text.isEmpty()) {
                    alertErrorDialog(getString(R.string.fill_password))
                    binding.etPassword.requestFocus()
                } else if (!ValidationData.passCheck(binding.etPassword.text.toString())) {
                    alertErrorDialog(getString(R.string.password_validation_text))
                } else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                    alertErrorDialog(getString(R.string.confirm_password_validation_text))
                } else  if (!isNetworkAvailable()) {
                    alertErrorDialog(getString(R.string.no_internet))
                }else{
                    resetPassword()
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


        }
    }




    private fun resetPassword() {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE
            authViewModel.resetPassword(
                requireArguments().getString(AppConstant.EMAIL).toString(),
                sessionManager.getUserType(),
                binding.etPassword.text.toString(),
                binding.etConfirmPassword.text.toString(),
                requireArguments().getString(AppConstant.OTP).toString(),
            ).observe(viewLifecycleOwner) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    binding.tvProgressBar.visibility = View.GONE
                    try {
                        passwordChangeAlertBox()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }
            }

        }
    }

    // This function is used for open change password alert box
    private fun passwordChangeAlertBox() {
        val postDialog = Dialog(requireContext())
        postDialog.setContentView(R.layout.alert_dialog_password_change)
        postDialog.setCancelable(true)

        val submitBtn: TextView = postDialog.findViewById(R.id.pass_btn_okay)

        submitBtn.setOnClickListener {
            postDialog.dismiss()
            findNavController().navigate(R.id.openSignInScreen)
        }
        postDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        postDialog.show()

    }


}
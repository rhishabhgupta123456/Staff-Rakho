package com.staffrakho.fragment.authScreens

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.ozcanalasalvar.otp_view.view.OtpView
import com.staffrakho.R
import com.staffrakho.activity.EmployerActivity
import com.staffrakho.activity.JobActivity
import com.staffrakho.databinding.FragmentOtpVerificationScreenBinding
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager


class OtpVerificationScreen : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentOtpVerificationScreenBinding
    private lateinit var sessionManager: SessionManager
    var otpValue: String = ""
    private var receiveOtp: String = ""
    private var email: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpVerificationScreenBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.REGISTRATION_SCREEN) {
                        findNavController().navigate(R.id.openRegistrationScreen)
                    }else if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.FORGET_PASSWORD_SCREEN) {
                        findNavController().navigate(R.id.openForgetPasswordScreen)
                    }else {
                        Toast.makeText(requireContext(), "Path Not Find", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        email = requireArguments().getString(AppConstant.EMAIL).toString()
        receiveOtp = requireArguments().getString(AppConstant.OTP).toString()

        binding.otpViewBox.setTextChangeListener(object : OtpView.ChangeListener {
            override fun onTextChange(value: String, completed: Boolean) {
                if (completed) {
                    otpValue = value
                }
            }
        })

        binding.btVerify.setOnClickListener(this)
        binding.btBack.setOnClickListener(this)

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.btBack -> {
                if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.REGISTRATION_SCREEN) {
                    findNavController().navigate(R.id.openRegistrationScreen)
                }else if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.FORGET_PASSWORD_SCREEN) {
                    findNavController().navigate(R.id.openForgetPasswordScreen)
                }else {
                    Toast.makeText(requireContext(), "Path Not Find", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.btVerify ->{
                if(checkValidation()){
                    if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.REGISTRATION_SCREEN) {
                        if (!isNetworkAvailable()) {
                            alertErrorDialog(getString(R.string.no_internet))
                        } else {
                            userRegisterSuccessFullAlertBox()
                        }
                    }else if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.FORGET_PASSWORD_SCREEN) {
                        val bundle = Bundle()
                        bundle.putString(AppConstant.EMAIL, email)
                        bundle.putString(AppConstant.OTP, receiveOtp)
                        findNavController().navigate(R.id.resetPasswordScreen, bundle)
                    }else {
                        Toast.makeText(requireContext(), "Path Not Find", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // This function is use for check validation in all screen field
    private fun checkValidation(): Boolean {
        if (otpValue.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_otp))
            return false
        } else if (otpValue.length <= 5) {
            alertErrorDialog(getString(R.string.fill_complete_otp))
            return false
        } else if (otpValue != receiveOtp) {
            alertErrorDialog(getString(R.string.fill_complete_otp))
            return false
        } else {
            return true
        }
    }

    // This Function is used for open Register SuccessFull Dialog
    private fun userRegisterSuccessFullAlertBox() {
        val postDialog = Dialog(requireContext())
        postDialog.setContentView(R.layout.alert_dialog_successful_sign_up)
        postDialog.setCancelable(true)

        val submit: TextView = postDialog.findViewById(R.id.btn_okay)

        submit.setOnClickListener {
            postDialog.dismiss()
            openHomeScreen()
        }

        postDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        postDialog.show()
    }

    // This Function is used for open Home Panel
    private fun openHomeScreen() {
        if (sessionManager.getUserType() == AppConstant.JOB_PERSON){
            val intent = Intent(requireContext() , JobActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }else if (sessionManager.getUserType() == AppConstant.BUSINESS_PERSON){
            val intent = Intent(requireContext() , EmployerActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }else{
            sessionEndDialog(getString(R.string.userType_not_found))
        }
    }


}
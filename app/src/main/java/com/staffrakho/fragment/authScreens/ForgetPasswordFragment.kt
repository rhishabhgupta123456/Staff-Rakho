package com.staffrakho.fragment.authScreens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.staffrakho.R
import com.staffrakho.databinding.FragmentForgetPasswordBinding
import com.staffrakho.networkModel.authNetworkPannel.AuthViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import com.staffrakho.utility.ValidationData
import kotlinx.coroutines.launch


class ForgetPasswordFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentForgetPasswordBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgetPasswordBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.openSignInScreen)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btBack.setOnClickListener(this)
        binding.btSendCode.setOnClickListener(this)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btBack -> {
                findNavController().navigate(R.id.openSignInScreen)
            }

            R.id.btSendCode -> {
                if (binding.etEmail.text.toString().isEmpty()) {
                    alertErrorDialog(getString(R.string.fill_email))
                } else if (!ValidationData.isPhoneNumber(binding.etEmail.text.toString())) {
                    alertErrorDialog(getString(R.string.fill_valid_phone))
                } else if (!isNetworkAvailable()) {
                    alertErrorDialog(getString(R.string.no_internet))
                }else{
                    forgetPassword()
                }

            }
        }

    }

    // This Function is used for get Forget Password OTP and open Otp verification screen
    private fun forgetPassword() {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            authViewModel.forgetPassword(
                binding.etEmail.text.toString(),
                sessionManager.getUserType()
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    binding.tvProgressBar.visibility = View.GONE
                    try {
                        val data = jsonObjectData["message"].asString
                        val regex = Regex("OTP\\s*=\\s*(\\d+)")
                        val matchResult = regex.find(data)
                        val otp = matchResult?.groups?.get(1)?.value
                        Log.e("OTP",otp.toString())
                        val bundle = Bundle()
                        bundle.putString(
                            AppConstant.SOURCE_FRAGMENT,
                            AppConstant.FORGET_PASSWORD_SCREEN
                        )
                        bundle.putString(AppConstant.EMAIL, binding.etEmail.text.toString())
                        bundle.putString(AppConstant.OTP, otp)
                        findNavController().navigate(R.id.openOtpVerificationScreen, bundle)

                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }
            }

        }
    }


}
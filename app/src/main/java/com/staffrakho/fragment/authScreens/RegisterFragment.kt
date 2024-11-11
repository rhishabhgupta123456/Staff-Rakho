package com.staffrakho.fragment.authScreens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.messaging.FirebaseMessaging
import com.staffrakho.R
import com.staffrakho.activity.EmployerActivity
import com.staffrakho.activity.IdentityActivity
import com.staffrakho.activity.JobActivity
import com.staffrakho.databinding.FragmentRegisterBinding
import com.staffrakho.networkModel.authNetworkPannel.AuthViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import com.staffrakho.utility.ValidationData
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var authViewModel: AuthViewModel
    private var confirmEye = false
    private var eye = false
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(
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

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.data != null) {
                    try {
                        if (result.resultCode == Activity.RESULT_OK) {
                            Log.e("Result Code ", result.toString())
                            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                            val account: GoogleSignInAccount? =
                                task.getResult(ApiException::class.java)


                            if (account != null) {
                                if (isNetworkAvailable()) {
                                    Log.e("Google Sign In Email", account.email.toString())
                                    getDeviceToken(account.displayName!!, account.email!!)
                                } else {
                                    alertErrorDialog(getString(R.string.no_internet))
                                }
                            }
                        } else if (result.resultCode == Activity.RESULT_CANCELED) {
                            alertErrorDialog("Sign in failed !")
                            Log.e("Social Login", "User canceled autocomplete")
                        }
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                        Log.e("Auth Error", result.resultCode.toString())
                    }
                } else {
                    alertErrorDialog("Gmail Not Found !")
                }

            }


        binding.etDob.setOnClickListener(this)
        binding.btBack.setOnClickListener(this)
        binding.btRegisterAccount.setOnClickListener(this)
        binding.btGmailLogin.setOnClickListener(this)
        binding.confirmEye.setOnClickListener(this)
        binding.Eye.setOnClickListener(this)

        screenText()
        logInButtonClickable()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.etDob -> {
                openDatePicker { selectedDate ->
                    binding.etDob.text = selectedDate
                }
            }

            R.id.btBack -> {
                findNavController().navigate(R.id.openSignInScreen)
            }

            R.id.btRegisterAccount -> {
                if (sessionManager.getUserType() == AppConstant.JOB_PERSON) {
                    if (checkValidation()) {
                        signUpAccount()
                    }
                } else {
                    if (checkValidationForEmployer()) {
                        employerRegister()
                    }
                }
            }

            R.id.btGmailLogin -> {
                if (!isNetworkAvailable()) {
                    alertErrorDialog(getString(R.string.no_internet))
                }else{
                    binding.tvProgressBar.visibility = View.VISIBLE
                    val mGoogleSignInClient = GoogleSignIn.getClient(
                        requireActivity(),
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build()
                    )

                    mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
                        val signInIntent: Intent = mGoogleSignInClient.signInIntent
                        activityResultLauncher.launch(signInIntent)
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

    // This function is use for check validation in all screen field of Employer
    private fun checkValidationForEmployer(): Boolean {
        if (binding.etName.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_name))
            binding.etName.requestFocus()
            return false
        } else if (binding.etEmail.text.isNotEmpty() && !ValidationData.isEmail(binding.etEmail.text.toString())) {
            alertErrorDialog(getString(R.string.fill_valid_email))
            binding.etEmail.requestFocus()
            return false
        }else if (binding.etPhone.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_phone))
            binding.etEmail.requestFocus()
            return false
        } else if (!ValidationData.isPhoneNumber(binding.etPhone.text.toString())) {
            alertErrorDialog(getString(R.string.fill_valid_phone))
            return false
        } else if (binding.etCompnayName.text.toString().isEmpty()) {
            alertErrorDialog("Please enter the company nae ")
            return false
        } else if (binding.etPassword.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_password))
            binding.etPassword.requestFocus()
            return false
        } else if (!ValidationData.passCheck(binding.etPassword.text.toString())) {
            alertErrorDialog(getString(R.string.password_validation_text))
            return false
        } else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
            alertErrorDialog(getString(R.string.confirm_password_validation_text))
            return false
        } else if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
            return false
        } else {
            return true
        }

    }

    // This function is used for get otp for register and open verification screeen
    private fun signUpAccount() {
        val gender = if (binding.btMale.isChecked) {
            "male"
        } else {
            "female"
        }

        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            authViewModel.register(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPhone.text.toString(),
                binding.etPassword.text.toString(),
                gender,
                binding.etDob.text.toString(),
            ).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    try {
                        Log.e("Sign Up Response", jsonObjectData.toString())
                        val data = jsonObjectData["data"].asJsonObject
                        sessionManager.setUserID(data["id"].asString)
                        sessionManager.setBearerToken(data["token"].asString)
                        sessionManager.setUserType(data["user_type"].asString)
                        Log.e("Token", sessionManager.getBearerToken())
                        userRegisterSuccessFullAlertBox()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }

                }

            }


        }

    }

   // This function is used for get otp for register and open verification screeen
    private fun employerRegister() {

        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            authViewModel.employerRegister(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPhone.text.toString(),
                binding.etPassword.text.toString(),
                binding.etCompnayName.text.toString(),
            ).observe(viewLifecycleOwner) { jsonObject ->

                binding.tvProgressBar.visibility = View.GONE

                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    try {
                        Log.e("Sign Up Response", jsonObjectData.toString())
                        val data = jsonObjectData["data"].asJsonObject
                        sessionManager.setUserID(data["id"].asString)
                        sessionManager.setBearerToken(data["token"].asString)
                        sessionManager.setUserType(data["user_type"].asString)
                        Log.e("Token", sessionManager.getBearerToken())
                        userRegisterSuccessFullAlertBox()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }

                }

            }


        }

    }

    // This function is used for register successFull Alert Box
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

    // This function is used for open Home Panel
    private fun openHomeScreen() {
        if (sessionManager.getUserType() == AppConstant.JOB_PERSON) {
            val intent = Intent(requireContext(), JobActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else if (sessionManager.getUserType() == AppConstant.BUSINESS_PERSON) {
            val intent = Intent(requireContext(), EmployerActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            sessionEndDialog(getString(R.string.userType_not_found))
        }
    }

    // This function is used for Set Screen
    private fun screenText() {
        when (sessionManager.getUserType()) {
            AppConstant.JOB_PERSON -> {
                binding.genderLable.visibility = View.VISIBLE
                binding.genderBox.visibility = View.VISIBLE
                binding.dobLable.visibility = View.VISIBLE
                binding.dobBox.visibility = View.VISIBLE
                binding.tvCompanyHead.visibility = View.GONE
                binding.companyBox.visibility = View.GONE
                binding.tvGoogleLoginLine.visibility = View.GONE
                binding.btGmailLogin.visibility = View.GONE
                binding.tvPageDescription.text = getString(R.string.jobRegisterText)
            }

            AppConstant.BUSINESS_PERSON -> {
                binding.genderLable.visibility = View.GONE
                binding.genderBox.visibility = View.GONE
                binding.dobLable.visibility = View.GONE
                binding.dobBox.visibility = View.GONE
                binding.tvCompanyHead.visibility = View.VISIBLE
                binding.companyBox.visibility = View.VISIBLE
                binding.tvGoogleLoginLine.visibility = View.GONE
                binding.btGmailLogin.visibility = View.GONE
                binding.tvPageDescription.text = getString(R.string.businessRegisterText)
            }

            else -> {
                val intent = Intent(requireContext(), IdentityActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    // This function is used for log in button Clickable
    private fun logInButtonClickable() {

        val spannedString = SpannableString(getString(R.string.login_account))

        val loginPageStart = object : ClickableSpan() {

            override fun onClick(p0: View) {
                findNavController().navigate(R.id.openSignInScreen)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.blueTextColor)
                ds.isUnderlineText = false
            }

        }


        val loginPageStartIndex = spannedString.toString().indexOf(getString(R.string.login))
        val loginPageEndIndex = loginPageStartIndex + "Log in".length



        spannedString.setSpan(
            loginPageStart,
            loginPageStartIndex,
            loginPageEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.btOpenLoginScreen.text = spannedString
        binding.btOpenLoginScreen.movementMethod = LinkMovementMethod.getInstance()
    }

    // This function is use for check validation in all screen field for jobSkeer
    private fun checkValidation(): Boolean {
        if (binding.etName.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_name))
            binding.etName.requestFocus()
            return false
        } else if (binding.etEmail.text.isNotEmpty() && !ValidationData.isEmail(binding.etEmail.text.toString())) {
            alertErrorDialog(getString(R.string.fill_valid_email))
            binding.etName.requestFocus()
            return false
        } else if (binding.etPhone.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_phone))
            binding.etPhone.requestFocus()
            return false
        } else if (!ValidationData.isPhoneNumber(binding.etPhone.text.toString())) {
            alertErrorDialog(getString(R.string.fill_valid_phone))
            return false
        } else if (!binding.btMale.isChecked && !binding.btFemale.isChecked) {
            alertErrorDialog(getString(R.string.select_gender))
            return false
        } else if (binding.etDob.text.toString().isEmpty()) {
            alertErrorDialog(getString(R.string.select_dob))
            return false
        } else if (binding.etPassword.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_password))
            binding.etPassword.requestFocus()
            return false
        } else if (!ValidationData.passCheck(binding.etPassword.text.toString())) {
            alertErrorDialog(getString(R.string.password_validation_text))
            return false
        } else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
            alertErrorDialog(getString(R.string.confirm_password_validation_text))
            return false
        } else if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
            return false
        } else {
            return true
        }
    }

    // This function is used for get device token of firebase
    private fun getDeviceToken(displayName: String, email: String) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                socialLogIn(displayName, email, task.result!!)
            } else {
                Log.e("Token Error", task.exception?.message.toString())
            }
        }

    }

    // This function is used for social login
    private fun socialLogIn(name: String, email: String, token: String) {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            authViewModel.socialLogIn(
                name,
                email,
                "google",
                token,
                "seeker",
            ).observe(viewLifecycleOwner) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE
                val jsonObjectData = checkResponse(jsonObject)
                if (jsonObjectData != null) {
                    try {
                        val data = jsonObjectData["data"].asJsonObject
                        sessionManager.setUserID(data["id"].asString)
                        sessionManager.setBearerToken(data["token"].asString)
                        sessionManager.setUserType(data["user_type"].asString)
                        Log.e("Token", sessionManager.getBearerToken())
                        userRegisterSuccessFullAlertBox()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }
            }

        }
    }

}
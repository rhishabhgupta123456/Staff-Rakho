package com.staffrakho.fragment.authScreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import com.staffrakho.databinding.FragmentSignInBinding
import com.staffrakho.networkModel.authNetworkPannel.AuthViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.SessionManager
import com.staffrakho.utility.ValidationData
import kotlinx.coroutines.launch

class SignInFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var authViewModel: AuthViewModel
    private var eye = false
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentSignInBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val intent = Intent(requireContext(), IdentityActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
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

        binding.btBack.setOnClickListener(this)
        binding.btOpenForgetPasswordScreen.setOnClickListener(this)
        binding.btGmailLogin.setOnClickListener(this)
        binding.btLogin.setOnClickListener(this)
        binding.Eye.setOnClickListener(this)

        screenText()
        registerButtonClickable()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.btBack -> {
                val intent = Intent(requireContext(), IdentityActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            R.id.btOpenForgetPasswordScreen -> {
                findNavController().navigate(R.id.openForgetPasswordScreen)
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

            R.id.btLogin -> {
                if (checkValidation()) {
                    login()
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

        }

    }

    // This function is used for User Login
    private fun login() {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            authViewModel.login(
                binding.etMail.text.toString(),
                binding.etPassword.text.toString(),
                sessionManager.getUserType()
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
                        openHomeScreen()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }
            }

        }
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

    // This function is used for set Screen
    private fun screenText() {
        when (sessionManager.getUserType()) {
            AppConstant.JOB_PERSON -> {
                binding.tvPageDescription.text = getString(R.string.jobLogInText)
                binding.tvGoogleLoginLine.visibility = View.GONE
                binding.btGmailLogin.visibility = View.GONE
            }

            AppConstant.BUSINESS_PERSON -> {
                binding.tvPageDescription.text = getString(R.string.businessLogInText)
                binding.tvGoogleLoginLine.visibility = View.GONE
                binding.btGmailLogin.visibility = View.GONE

            }

            else -> {
                val intent = Intent(requireContext(), IdentityActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    // This function is used for register button Clickable
    private fun registerButtonClickable() {
        val spannedString = SpannableString(getString(R.string.register_account))

        val registerPageStart = object : ClickableSpan() {
            override fun onClick(p0: View) {
                findNavController().navigate(R.id.openRegistrationScreen)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.blueTextColor)
                ds.isUnderlineText = false
            }

        }


        val registerPageStartIndex = spannedString.toString().indexOf(getString(R.string.register))
        val registerPageEndIndex = registerPageStartIndex + getString(R.string.register).length - 1

        spannedString.setSpan(
            registerPageStart,
            registerPageStartIndex,
            registerPageEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.btOpenRegisterScreen.text = spannedString
        binding.btOpenRegisterScreen.movementMethod = LinkMovementMethod.getInstance()
    }

    // This function is use for check validation in all screen field
    private fun checkValidation(): Boolean {
        if (binding.etMail.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_email))
            binding.etMail.requestFocus()
            return false
        } else if (!ValidationData.isPhoneNumber(binding.etMail.text.toString())) {
            alertErrorDialog(getString(R.string.fill_valid_phone))
            binding.etMail.requestFocus()
            return false
        } else if (binding.etPassword.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_email))
            binding.etPassword.requestFocus()
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
                        openHomeScreen()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }
            }

        }
    }


}
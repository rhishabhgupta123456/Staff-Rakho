package com.staffrakho.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.staffrakho.R
import com.staffrakho.databinding.ActivityIdentityBinding
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseActivity
import com.staffrakho.utility.SessionManager

class IdentityActivity : BaseActivity() {

    private lateinit var binding: ActivityIdentityBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        if(sessionManager.getLanguage() == getString(R.string.english)){
            setLocale("eng")
        }else if (sessionManager.getLanguage() == getString(R.string.hindi)) {
            setLocale("hi")
        }

        binding.btNext.setOnClickListener{
            openOnBoardingPanel()
        }

        binding.btEnglish.setOnClickListener{
            setLocale("eng")
            sessionManager.setLanguage(getString(R.string.english))
            val intent = Intent(this, IdentityActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btHindi.setOnClickListener{
            setLocale("hi")
            sessionManager.setLanguage(getString(R.string.hindi))
            val intent = Intent(this, IdentityActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    // This Function is used for open Authentication Panel
    private fun openOnBoardingPanel() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
    }

}
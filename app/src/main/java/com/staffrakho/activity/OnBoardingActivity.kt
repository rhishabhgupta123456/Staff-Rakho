package com.staffrakho.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.staffrakho.R
import com.staffrakho.adapter.OnBoardingAdapter
import com.staffrakho.dataModel.OnBoardingModel
import com.staffrakho.databinding.ActivityOnBoardingBinding
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseActivity
import com.staffrakho.utility.SessionManager



class OnBoardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: OnBoardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@OnBoardingActivity, IdentityActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        onBackPressedDispatcher.addCallback(this, callback)

        if(sessionManager.getLanguage() == getString(R.string.english)){
            setLocale("eng")
        }else if (sessionManager.getLanguage() == getString(R.string.hindi)) {
            setLocale("hi")
        }



        binding.btJobSeeker.setOnClickListener{
            sessionManager.setUserType(AppConstant.JOB_PERSON)
            openAuthPanel()
        }

        binding.btBusinessMan.setOnClickListener{
            sessionManager.setUserType(AppConstant.BUSINESS_PERSON)
            openAuthPanel()
        }



    }

    private fun openAuthPanel() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }


}
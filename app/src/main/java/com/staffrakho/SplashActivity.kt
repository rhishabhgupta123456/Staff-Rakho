package com.staffrakho

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.staffrakho.activity.EmployerActivity
import com.staffrakho.activity.IdentityActivity
import com.staffrakho.activity.JobActivity
import com.staffrakho.databinding.ActivitySplashBinding
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseActivity
import com.staffrakho.utility.SessionManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        if(sessionManager.getLanguage() == getString(R.string.english)){
            setLocale("eng")
        }else if (sessionManager.getLanguage() == getString(R.string.hindi)) {
            setLocale("hi")
        }

        Handler(Looper.myLooper()!!).postDelayed({
            if (sessionManager.getBearerToken() == "Bearer " || sessionManager.getUserType() == "")
            {
                val intent = Intent(this, IdentityActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Log.e("user type" ,sessionManager.getUserType() )
                if (sessionManager.getUserType() == AppConstant.JOB_PERSON) {
                    val intent = Intent(this, JobActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (sessionManager.getUserType() == AppConstant.BUSINESS_PERSON) {
                    val intent = Intent(this, EmployerActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    sessionEndDialog(getString(R.string.userType_not_found))
                }
            }

        }, 3000)

    }




}
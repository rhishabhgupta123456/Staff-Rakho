package com.staffrakho.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.staffrakho.R
import com.staffrakho.databinding.ActivityJobBinding
import com.staffrakho.utility.BaseActivity
import com.staffrakho.utility.SessionManager

class JobActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityJobBinding
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        if (sessionManager.getLanguage() == getString(R.string.english)) {
            setLocale("eng")
        } else if (sessionManager.getLanguage() == getString(R.string.hindi)) {
            setLocale("hi")
        }


        binding.btHomeBottomMenu.setOnClickListener(this)
        binding.btAppliedJobsBottomMenu.setOnClickListener(this)
        binding.btMessageBottomMenu.setOnClickListener(this)
        binding.btMenuBottomMenu.setOnClickListener(this)

        openHomeScreen()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btHomeBottomMenu -> {
                findNavController(R.id.fragmentJobContainerView).navigate(R.id.HomeScreen)
                openHomeScreen()
            }

            R.id.btAppliedJobsBottomMenu -> {
                findNavController(R.id.fragmentJobContainerView).navigate(R.id.appliedJobScreen)
                openAppliedJobScreen()
            }

            R.id.btMessageBottomMenu -> {
                findNavController(R.id.fragmentJobContainerView).navigate(R.id.messageScreen)
                openMessageScreen()
            }

            R.id.btMenuBottomMenu -> {
                findNavController(R.id.fragmentJobContainerView).navigate(R.id.menuScreen)
                openMenuScreen()
            }
        }

    }

    // This Function is used for open Home Screen
    fun openHomeScreen() {
        //Home Icon
        binding.homeIcon.setColorFilter(getColor(R.color.selectMenuColor), PorterDuff.Mode.SRC_IN)

        //applied Jobs Icon
        binding.appliedJobsIcon.setColorFilter(
            getColor(R.color.unSelectMenuColor),
            PorterDuff.Mode.SRC_IN
        )

        //Message Icon
        binding.messageIcon.setColorFilter(
            getColor(R.color.unSelectMenuColor),
            PorterDuff.Mode.SRC_IN
        )

        //Menu Icon
        binding.menuIcon.setColorFilter(getColor(R.color.unSelectMenuColor), PorterDuff.Mode.SRC_IN)
    }

    // This Function is used for open Applied Job Screen
    fun openAppliedJobScreen() {
        //Home Icon
        binding.homeIcon.setColorFilter(getColor(R.color.unSelectMenuColor), PorterDuff.Mode.SRC_IN)

        //applied Jobs Icon
        binding.appliedJobsIcon.setColorFilter(
            getColor(R.color.selectMenuColor),
            PorterDuff.Mode.SRC_IN
        )

        //Message Icon
        binding.messageIcon.setColorFilter(
            getColor(R.color.unSelectMenuColor),
            PorterDuff.Mode.SRC_IN
        )

        //Menu Icon
        binding.menuIcon.setColorFilter(getColor(R.color.unSelectMenuColor), PorterDuff.Mode.SRC_IN)
    }

    // This Function is used for open Message Screen
    fun openMessageScreen() {
        //Home Icon
        binding.homeIcon.setColorFilter(getColor(R.color.unSelectMenuColor), PorterDuff.Mode.SRC_IN)

        //applied Jobs Icon
        binding.appliedJobsIcon.setColorFilter(
            getColor(R.color.unSelectMenuColor),
            PorterDuff.Mode.SRC_IN
        )

        //Message Icon
        binding.messageIcon.setColorFilter(
            getColor(R.color.selectMenuColor),
            PorterDuff.Mode.SRC_IN
        )

        //Menu Icon
        binding.menuIcon.setColorFilter(getColor(R.color.unSelectMenuColor), PorterDuff.Mode.SRC_IN)
    }

    // This Function is used for open Menu Screen
    fun openMenuScreen() {
        //Home Icon
        binding.homeIcon.setColorFilter(getColor(R.color.unSelectMenuColor), PorterDuff.Mode.SRC_IN)

        //applied Jobs Icon
        binding.appliedJobsIcon.setColorFilter(
            getColor(R.color.unSelectMenuColor),
            PorterDuff.Mode.SRC_IN
        )

        //Message Icon
        binding.messageIcon.setColorFilter(
            getColor(R.color.unSelectMenuColor),
            PorterDuff.Mode.SRC_IN
        )

        //Menu Icon
        binding.menuIcon.setColorFilter(getColor(R.color.selectMenuColor), PorterDuff.Mode.SRC_IN)
    }

}
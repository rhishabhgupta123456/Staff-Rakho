package com.staffrakho.fragment.businessScreen

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.databinding.FragmentProfileBusinessBinding
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetEmployerProfileEditDialog
import com.staffrakho.networkModel.employerNetworkPannel.EmployerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.MediaUtility
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ProfileBusinessFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentProfileBusinessBinding
    lateinit var sessionManager: SessionManager
    private lateinit var employerViewModel: EmployerViewModel
    private var btActionUpload: Int? = null
    private var fromTime: String = ""
    private var toTime: String = ""
    private var fromTimeBoys: String = ""
    private var toTimeBoys: String = ""
    private var fromTimeGirls: String = ""
    private var toTimeGirls: String = ""
    var daysArray = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentProfileBusinessBinding.inflate(
                LayoutInflater.from(requireActivity()),
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        employerViewModel = ViewModelProvider(this)[EmployerViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (sessionManager.getSourceFragment() == AppConstant.HOME_SCREEN) {
                        findNavController().navigate(R.id.openHomeScreen)
                    } else if (sessionManager.getSourceFragment() == AppConstant.MENU_SCREEN) {
                        findNavController().navigate(R.id.openMenuScreen)
                    } else {
                        alertErrorDialog("Path Not Found")
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.btBack.setOnClickListener(this)
        binding.btEdit.setOnClickListener(this)
        binding.btDetails.setOnClickListener(this)
        binding.btPhotos.setOnClickListener(this)
        binding.btProfileUpload.setOnClickListener(this)
        binding.btBannerUplaod.setOnClickListener(this)
        binding.btIp1.setOnClickListener(this)
        binding.btIp2.setOnClickListener(this)
        binding.btIp3.setOnClickListener(this)
        binding.btEp1.setOnClickListener(this)
        binding.btEp2.setOnClickListener(this)
        binding.btEp3.setOnClickListener(this)

        getUserProfile()
        showDetails()
    }

    // This Function is used for display details screen
    private fun showDetails() {
        binding.btDetails.setTextColor(resources.getColor(R.color.blueTextColor))
        binding.btPhotos.setTextColor(resources.getColor(R.color.lightTextColor))
        binding.tvPersonalDetail.visibility = View.VISIBLE
        binding.tvPhotosBox.visibility = View.GONE
    }

    // This Function is used for display photos screen
    private fun showPhotos() {
        binding.btDetails.setTextColor(resources.getColor(R.color.lightTextColor))
        binding.btPhotos.setTextColor(resources.getColor(R.color.blueTextColor))
        binding.tvPhotosBox.visibility = View.VISIBLE
        binding.tvPersonalDetail.visibility = View.GONE
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.btBack -> {
                if (sessionManager.getSourceFragment() == AppConstant.HOME_SCREEN) {
                    findNavController().navigate(R.id.openHomeScreen)
                } else if (sessionManager.getSourceFragment() == AppConstant.MENU_SCREEN) {
                    findNavController().navigate(R.id.openMenuScreen)
                } else {
                    alertErrorDialog("Path Not Found")
                }
            }

            R.id.btEdit -> {
                val bottomSheetFragment = BottomSheetEmployerProfileEditDialog(
                    binding.etCompanyAndStoreName.text.toString(),
                    binding.etContactNo.text.toString(),
                    binding.etAddress.text.toString(),
                    binding.etState.text.toString(),
                    binding.etCity.text.toString(),
                    binding.etZip.text.toString(),
                    binding.etTypeOfBusiness.text.toString(),
                    binding.etNoOfFloor.text.toString(),
                    binding.etCompanyLook.text.toString(),
                    fromTime,
                    toTime,
                    fromTimeBoys,
                    toTimeBoys,
                    fromTimeGirls,
                    toTimeGirls,
                    binding.etOpeningDays.text.toString()
                )
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
            }

            R.id.btDetails -> {
                showDetails()
            }

            R.id.btPhotos -> {
                showPhotos()
            }

            R.id.btProfileUpload -> {
                btActionUpload = 1
                browseCameraAndGallery()
            }

            R.id.btBannerUplaod -> {
                btActionUpload = 2
                browseCameraAndGallery()
            }

            R.id.btIp1 -> {
                btActionUpload = 3
                browseCameraAndGallery()
            }

            R.id.btIp2 -> {
                btActionUpload = 4
                browseCameraAndGallery()
            }

            R.id.btIp3 -> {
                btActionUpload = 5
                browseCameraAndGallery()
            }

            R.id.btEp1 -> {
                btActionUpload = 6
                browseCameraAndGallery()
            }

            R.id.btEp2 -> {
                btActionUpload = 7
                browseCameraAndGallery()
            }

            R.id.btEp3 -> {
                btActionUpload = 8
                browseCameraAndGallery()
            }


        }
    }

    // this function is used for open the menu for opening gallery of camera
    private fun browseCameraAndGallery() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose Image", "Cancel")
        val builder = AlertDialog.Builder(
            requireContext()
        )
        builder.setTitle("Choose File")
        builder.setItems(
            items
        ) { dialog: DialogInterface, item: Int ->
            if (items[item] == "Take Photo") {
                try {
                    cameraIntent()
                } catch (e: java.lang.Exception) {
                    Log.v("Exception", e.message!!)
                }
            } else if (items[item] == "Choose Image") {
                try {
                    galleryIntent()
                } catch (e: java.lang.Exception) {
                    Log.v("Exception", e.message!!)
                }
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    // this function is used for open the camera
    private fun cameraIntent() {
        ImagePicker.with(this)
            .crop(150f, 150f)
            .cameraOnly()
            .compress(1024)
            .maxResultSize(
                1080,
                1080
            )
            .start()
    }

    // this function is used for open the gallery
    private fun galleryIntent() {
        ImagePicker.with(this)
            .crop(150f, 150f)
            .galleryOnly()
            .compress(1024)
            .maxResultSize(
                1080,
                1080
            )
            .start()
    }

    // This Function is used for get User Profile
    @SuppressLint("SetTextI18n")
    private fun getUserProfile() { // 1 - personal //2 - Professional

        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            employerViewModel.getUserEmployerProfile(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        binding.tvProgressBar.visibility = View.GONE
                        try {

                            sessionManager.setUserEmail(checkFieldSting(jsonObjectData["data"].asJsonObject["email"]))
                            sessionManager.setUserPhone(checkFieldSting(jsonObjectData["data"].asJsonObject["mobile"]))

                            binding.etCompanyAndStoreName.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["company_name"])

                            binding.etAddress.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["address"])

                            binding.etState.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["state_name"])
                            binding.etCity.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["city_name"])
                            binding.etZip.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["zip"])

                            binding.etOpeningHours.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["from_time"]) + " - " + checkFieldSting(
                                    jsonObjectData["data"].asJsonObject["to_time"]
                                )

                            fromTime =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["from_time"])
                            toTime = checkFieldSting(jsonObjectData["data"].asJsonObject["to_time"])

                            binding.etWorkingHoursBoys.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["boys_from_time"]) + " - " + checkFieldSting(
                                    jsonObjectData["data"].asJsonObject["boys_to_time"]
                                )

                            fromTimeBoys =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["boys_from_time"])
                            toTimeBoys =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["boys_to_time"])

                            binding.etWorkingHoursGirls.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["girls_from_time"]) + " - " + checkFieldSting(
                                    jsonObjectData["data"].asJsonObject["girls_to_time"]
                                )

                            fromTimeGirls =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["girls_from_time"])
                            toTimeGirls =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["girls_to_time"])


                            binding.etContactNo.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["mobile"])
                            binding.etNoOfFloor.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["floor"])
                            binding.etCompanyLook.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["company_look_label"])

                            binding.etTypeOfBusiness.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["business_type"])

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["logo"]))
                                .placeholder(R.drawable.company_icon).into(binding.tvProfile)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["banner"]))
                                .placeholder(R.drawable.no_image).into(binding.tvBannerImage)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo1"]))
                                .placeholder(R.drawable.no_image).into(binding.tvIp1)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo2"]))
                                .placeholder(R.drawable.no_image).into(binding.tvIp2)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo3"]))
                                .placeholder(R.drawable.no_image).into(binding.tvIp3)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo4"]))
                                .placeholder(R.drawable.no_image).into(binding.tvEp1)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo5"]))
                                .placeholder(R.drawable.no_image).into(binding.tvEp2)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["photo6"]))
                                .placeholder(R.drawable.no_image).into(binding.tvEp3)


                            if (checkFieldArray(jsonObjectData["data"].asJsonObject["opening_days"]) != null){
                                binding.etOpeningDays.text = checkFieldArray(jsonObjectData["data"].asJsonObject["opening_days"])!!.joinToString(",")
                            }

                            showDetails()

                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }
    }

    @Deprecated("Deprecated in Java")
    // this function is used for to get image from camera or gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ImagePicker.REQUEST_CODE) {
            data?.let {

                onSelectFromGalleryResultant(it)
            }
        }


    }

    // this function is used for to get image from gallery
    private fun onSelectFromGalleryResultant(data: Intent) {
        try {
            when (btActionUpload) {
                1 -> { // Logo Upload
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateEmployerLogo(
                            File(
                                MediaUtility.getPath(
                                    requireContext(),
                                    data.data!!
                                )
                            )
                        )
                    }

                }

                2 -> { // Upload Banner
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateEmployerBanner(
                            File(
                                MediaUtility.getPath(
                                    requireContext(),
                                    data.data!!
                                )
                            )
                        )
                    }

                }

                3 -> { // Upload Interior Photo 1

                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateEmployerInteriorPhoto(
                            File(
                                MediaUtility.getPath(
                                    requireContext(),
                                    data.data!!
                                )
                            ), 1
                        )
                    }

                }

                4 -> {// Upload Interior Photo 2
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateEmployerInteriorPhoto(
                            File(
                                MediaUtility.getPath(
                                    requireContext(),
                                    data.data!!
                                )
                            ), 2
                        )

                    }

                }

                5 -> {// Upload Interior Photo 3
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateEmployerInteriorPhoto(
                            File(
                                MediaUtility.getPath(
                                    requireContext(),
                                    data.data!!
                                )
                            ), 3
                        )
                    }

                }

                6 -> { // Upload Exterior Photo 1
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateEmployerExteriorPhoto(
                            File(
                                MediaUtility.getPath(
                                    requireContext(),
                                    data.data!!
                                )
                            ), 1
                        )
                    }


                }

                7 -> {// Upload Exterior Photo 2
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateEmployerExteriorPhoto(
                            File(
                                MediaUtility.getPath(
                                    requireContext(),
                                    data.data!!
                                )
                            ), 2
                        )
                    }

                }

                8 -> {// Upload Exterior Photo 3
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateEmployerExteriorPhoto(
                            File(
                                MediaUtility.getPath(
                                    requireContext(),
                                    data.data!!
                                )
                            ), 3
                        )
                    }

                }

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // this function is used for edit attorney profile
    private fun updateEmployerLogo(image: File?) {

        val imageRequestBody = image?.asRequestBody("application/octet-stream".toMediaType())
        val imagePart = imageRequestBody?.let {
            MultipartBody.Part.createFormData("logo", image.name, it)
        }

        lifecycleScope.launch {
            employerViewModel.updateEmployerLogo(
                sessionManager.getBearerToken(),
                imagePart
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        Glide.with(requireActivity())
                            .load(image)
                            .into(binding.tvProfile)

                        Toast.makeText(
                            requireContext(),
                            jsonObjectData["message"].asString,
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }
    }

    // this function is used for edit attorney profile
    private fun updateEmployerBanner(image: File?) {

        val imageRequestBody = image?.asRequestBody("application/octet-stream".toMediaType())
        val imagePart = imageRequestBody?.let {
            MultipartBody.Part.createFormData("banner", image.name, it)
        }

        lifecycleScope.launch {
            employerViewModel.updateEmployerBanner(
                sessionManager.getBearerToken(),
                imagePart
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        Glide.with(requireActivity())
                            .load(image)
                            .into(binding.tvBannerImage)

                        Toast.makeText(
                            requireContext(),
                            jsonObjectData["message"].asString,
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }
    }

    // this function is used for edit attorney profile
    private fun updateEmployerInteriorPhoto(image: File?, type: Int) {

        val bodyName = when (type) {
            1 -> "photo1"
            2 -> "photo2"
            else -> "photo3"
        }

        val imageRequestBody = image?.asRequestBody("application/octet-stream".toMediaType())
        val imagePart = imageRequestBody?.let {
            MultipartBody.Part.createFormData(bodyName, image.name, it)
        }

        lifecycleScope.launch {
            employerViewModel.updateEmployerInteriorPhoto(
                sessionManager.getBearerToken(),
                imagePart
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {

                        when (type) {
                            1 -> {
                                Glide.with(requireActivity())
                                    .load(image)
                                    .placeholder(R.drawable.no_image)
                                    .into(binding.tvIp1)
                            }

                            2 -> {
                                Glide.with(requireActivity())
                                    .load(image)
                                    .placeholder(R.drawable.no_image)
                                    .into(binding.tvIp2)
                            }

                            else -> {
                                Glide.with(requireActivity())
                                    .load(image)
                                    .placeholder(R.drawable.no_image)
                                    .into(binding.tvIp3)
                            }
                        }

                        Toast.makeText(
                            requireContext(),
                            jsonObjectData["message"].asString,
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }
    }

    // this function is used for edit attorney profile
    private fun updateEmployerExteriorPhoto(image: File?, type: Int) {

        val bodyName = when (type) {
            1 -> "photo4"
            2 -> "photo5"
            else -> "photo6"
        }

        val imageRequestBody = image?.asRequestBody("application/octet-stream".toMediaType())
        val imagePart = imageRequestBody?.let {
            MultipartBody.Part.createFormData(bodyName, image.name, it)
        }

        lifecycleScope.launch {
            employerViewModel.updateEmployerExteriorPhoto(
                sessionManager.getBearerToken(),
                imagePart
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        when (type) {
                            1 -> {
                                Glide.with(requireActivity())
                                    .load(image)
                                    .placeholder(R.drawable.no_image)
                                    .into(binding.tvEp1)
                            }

                            2 -> {
                                Glide.with(requireActivity())
                                    .load(image)
                                    .placeholder(R.drawable.no_image)
                                    .into(binding.tvEp2)
                            }

                            else -> {
                                Glide.with(requireActivity())
                                    .load(image)
                                    .placeholder(R.drawable.no_image)
                                    .into(binding.tvEp3)
                            }
                        }

                        Toast.makeText(
                            requireContext(),
                            jsonObjectData["message"].asString,
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }
    }


}
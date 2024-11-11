package com.staffrakho.fragment.jobScreen

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.staffrakho.R
import com.staffrakho.adapter.FamilyRelationAdapter
import com.staffrakho.adapter.WorkExperienceAdapter
import com.staffrakho.dataModel.FamilyDataModel
import com.staffrakho.dataModel.FamilyList
import com.staffrakho.dataModel.WorkExperienceDataModel
import com.staffrakho.databinding.FragmentProfileBinding
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetFamilyMemberDialog
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetProfileEditDialog
import com.staffrakho.fragment.bottomSheetFilter.BottomSheetWorkExperienceDialog
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import com.staffrakho.utility.MediaUtility
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class ProfileJobSeekerFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentProfileBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var jobSeekerViewModel: JobSeekerViewModel
    private var resumeUrl: String = ""

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        try {
            val data = result.data
            if (data != null) {
                val pdfUri: Uri? = data.data!!
                Log.e("PDF", pdfUri.toString())
                if (pdfUri != null) {
                    val pdfFile = getFileFromUri(requireContext(), pdfUri)
                    Log.e("Pdf File ", pdfFile!!.name.toString())
                    Log.e("Pdf File Path ", pdfFile.path.toString())
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    } else {
                        updateResume(pdfFile)
                    }

                } else {
                    Log.e("PDF", "No PDF selected")
                }

            }
        } catch (e: Exception) {
            alertErrorDialog(e.toString())
        }
    }

    // This Function is used for convert URI in FILE
    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            val tempFile = File(context.cacheDir, "tempFile.pdf")
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()
            return tempFile
        }
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentProfileBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        jobSeekerViewModel = ViewModelProvider(this)[JobSeekerViewModel::class.java]

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.HOME_SCREEN) {
                        findNavController().navigate(R.id.openHomeScreen)
                    } else if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.MENU_SCREEN) {
                        findNavController().navigate(R.id.openMenuScreen)
                    } else {
                        alertErrorDialog(getString(R.string.path_not_found))
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btBack.setOnClickListener(this)
        binding.btDownload.setOnClickListener(this)
        binding.btEdit.setOnClickListener(this)
        binding.btProfileUpload.setOnClickListener(this)
        binding.btUpdateResume.setOnClickListener(this)
        binding.btAddExpriecne.setOnClickListener(this)
        binding.btAddFamilyBackground.setOnClickListener(this)



        getUserProfile()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btBack -> {
                if (sessionManager.getSourceFragment() == AppConstant.HOME_SCREEN) {
                    findNavController().navigate(R.id.openHomeScreen)
                } else if (sessionManager.getSourceFragment() == AppConstant.MENU_SCREEN) {
                    findNavController().navigate(R.id.openMenuScreen)
                } else {
                    alertErrorDialog(getString(R.string.path_not_found))
                }
            }


            R.id.btDownload -> {
                if (binding.btDownload.text != getString(R.string.upload)) {
                    val bundle = Bundle()
                    bundle.putString(AppConstant.PDF_URL, resumeUrl)
                    bundle.putString(AppConstant.SOURCE_FRAGMENT, AppConstant.USER_PROFILE_SCREEN)
                    findNavController().navigate(R.id.openPDFScreen, bundle)
                    // openPdf(resumeUrl)
                }
            }


            R.id.btEdit -> {
                val bottomSheetFragment = BottomSheetProfileEditDialog(
                    binding.tvDate.text.toString(),
                    binding.etGender.text.toString(),
                    binding.etSkill.text.toString(),
                    binding.etTransport.text.toString(),
                    binding.etPhysically.text.toString(),
                    binding.etDisability.text.toString(),
                    binding.etProfileType.text.toString(),
                    binding.etBio.text.toString(),
                    binding.etState.text.toString(),
                    binding.etCity.text.toString(),
                    binding.etAddress.text.toString(),
                    binding.etZip.text.toString(),
                    binding.etPhone.text.toString(),
                    binding.etRole.text.toString(),
                    binding.etSubDistrict.text.toString(),
                    binding.etVillage.text.toString(),
                    binding.etAge.text.toString(),
                    binding.etDisabilityDetails.text.toString(),
                    binding.etMaritalStatus.text.toString(),
                    binding.etshortTermWork.text.toString(),
                    binding.etCurrentlyJobStatus.text.toString(),
                )
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
            }


            R.id.btAddExpriecne -> {
                val bottomSheetFragment = BottomSheetWorkExperienceDialog(null)
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
            }

            R.id.btAddFamilyBackground -> {
                val bottomSheetFragment = BottomSheetFamilyMemberDialog(null)
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
            }

            R.id.btProfileUpload -> {
                browseCameraAndGallery()
            }

            R.id.btUpdateResume -> {
                selectPDF()
            }
        }
    }

    // This Function is used for select PDF
    private fun selectPDF() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        Intent.createChooser(intent, "Select PDF")
        resultLauncher.launch(intent)
    }

    // This Function is used for get user profile from database
    @SuppressLint("SetTextI18n")
    private fun getUserProfile() { // 1 - personal //2 - Professional
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            jobSeekerViewModel.getUserProfile(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData = checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        binding.tvProgressBar.visibility = View.GONE
                        try {
                            sessionManager.setUserEmail(checkFieldSting(jsonObjectData["data"].asJsonObject["email"]))
                            sessionManager.setUserPhone(checkFieldSting(jsonObjectData["data"].asJsonObject["mobile"]))

                            binding.etName.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["name"])
                            binding.etEmail.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["email"])


                            binding.etPhone.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["mobile"])
                            binding.etGender.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["gender_label"])
                            binding.etRole.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["role"])

                            binding.tvDate.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["dob"])
                            binding.etBio.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["bio"])
                            binding.etSkill.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["skills"])
                            binding.etTransport.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["transport_facility"])
                            binding.etPhysically.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["physically_fit"])
                            binding.etProfileType.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["profile_type"])
                            binding.etDisability.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["disability"])

                            binding.etAddress.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["address"])
                            binding.etState.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["state_name"])
                            binding.etCity.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["city_name"])
                            binding.etZip.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["zip"])

                            binding.etSubDistrict.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["sub_district"])

                            binding.etVillage.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["village"])

                            binding.etAge.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["age_range_label"])

                            binding.etDisabilityDetails.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["disability_details"])

                            binding.etMaritalStatus.text =
                                checkFieldSting(jsonObjectData["data"].asJsonObject["marrital_status_label"])

                            resumeUrl =
                                AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["resume"])

                            binding.btDownload.text = getString(R.string.View)
                            binding.btDownload.setTextColor(resources.getColor(R.color.blueTextColor))
                            binding.tvFileName.text =
                                resumeUrl.substring(resumeUrl.lastIndexOf('/') + 1)

                            Glide.with(requireActivity())
                                .load(AppConstant.MEDIA_BASE_URL + checkFieldSting(jsonObjectData["data"].asJsonObject["avatar"]))
                                .placeholder(R.drawable.demo_user).into(binding.tvProfile)

                            val dataList = Gson().fromJson(
                                checkFieldObject(jsonObjectData["data"].asJsonObject),
                                FamilyList::class.java
                            )

                            Log.e("Family Backgrounding", dataList.toString())

                            val workExperienceAdapter = WorkExperienceAdapter(
                                dataList.workExperience, requireContext(), 1
                            )
                            binding.workExprienceRecycleView.adapter = workExperienceAdapter
                            binding.workExprienceRecycleView.layoutManager =
                                LinearLayoutManager(requireContext())

                            workExperienceAdapter.setOnRequestAction(object :
                                WorkExperienceAdapter.OnRequestAction {

                                override fun editItem(item: WorkExperienceDataModel) {
                                    val bottomSheetFragment = BottomSheetWorkExperienceDialog(item)
                                    bottomSheetFragment.show(
                                        childFragmentManager, bottomSheetFragment.tag
                                    )
                                }

                                override fun deleteItem(item: WorkExperienceDataModel) {
                                    deleteItemAlertBox(1, item.id)
                                }

                            })


                            val familyAdapter = FamilyRelationAdapter(
                                dataList.family, requireContext(), 1
                            )
                            binding.familyBackgroundRecycleView.adapter = familyAdapter

                            binding.familyBackgroundRecycleView.layoutManager =
                                LinearLayoutManager(requireContext())

                            familyAdapter.setOnRequestAction(object :
                                FamilyRelationAdapter.OnRequestAction {
                                override fun editItem(item: FamilyDataModel) {
                                    val bottomSheetFragment = BottomSheetFamilyMemberDialog(
                                        item,
                                    )
                                    bottomSheetFragment.show(
                                        childFragmentManager, bottomSheetFragment.tag
                                    )
                                }

                                override fun deleteItem(item: FamilyDataModel) {
                                    deleteItemAlertBox(2, item.id)
                                }

                            })


                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }

        }
    }

    // This Function is used for delete family background from database
    private fun deleteFamilyBackground(id: Int) {
        lifecycleScope.launch {
            jobSeekerViewModel.deleteFamilyMember(
                sessionManager.getBearerToken(), id
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        Toast.makeText(
                            requireContext(),
                            "Family Background Deleted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        getUserProfile()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }

    }

    // This Function is used for delete work experience from database
    private fun deleteWorkExperience(id: Int) {
        lifecycleScope.launch {
            jobSeekerViewModel.deleteWorkExperience(
                sessionManager.getBearerToken(), id
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        Toast.makeText(
                            requireContext(),
                            "Work Experience Deleted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        getUserProfile()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPDF()
        } else {
            alertErrorDialog("Permission Denied")
        }
    }

    // this function is used for open the camera
    private fun cameraIntent() {
        ImagePicker.with(this).crop(150f, 150f).cameraOnly().compress(1024).maxResultSize(
            1080, 1080
        ).start()
    }

    // this function is used for open the gallery
    private fun galleryIntent() {
        ImagePicker.with(this).crop(150f, 150f).galleryOnly().compress(1024).maxResultSize(
            1080, 1080
        ).start()
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
            if (!isNetworkAvailable()) {
                alertErrorDialog(getString(R.string.no_internet))
            } else {
                updateProfilePicture(File(MediaUtility.getPath(requireContext(), data.data!!)))
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // this function is used for edit attorney profile
    private fun updateProfilePicture(image: File?) {

        val imageRequestBody = image?.asRequestBody("application/octet-stream".toMediaType())
        val imagePart = imageRequestBody?.let {
            MultipartBody.Part.createFormData("avatar", image.name, it)
        }

        lifecycleScope.launch {
            jobSeekerViewModel.updateProfilePicture(
                sessionManager.getBearerToken(), imagePart
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        Glide.with(requireActivity()).load(image).into(binding.tvProfile)
                        Toast.makeText(
                            requireContext(),
                            checkFieldSting(jsonObjectData["message"]),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }
    }

    // This Function is used for update Resume
    private fun updateResume(pdf: File?) {
        val pdfRequestBody = pdf?.asRequestBody("application/pdf".toMediaType())

        val pdfPart = pdfRequestBody?.let {
            MultipartBody.Part.createFormData("resume", pdf.name, it)
        }

        lifecycleScope.launch {
            jobSeekerViewModel.updateResume(
                sessionManager.getBearerToken(), pdfPart
            ).observe(viewLifecycleOwner) { jsonObject ->
                val jsonObjectData = checkResponse(jsonObject)
                Log.e("Edit Profile", "True")
                if (jsonObjectData != null) {
                    try {
                        Toast.makeText(
                            requireContext(),
                            checkFieldSting(jsonObjectData["message"]),
                            Toast.LENGTH_SHORT
                        ).show()
                        getUserProfile()
                    } catch (e: Exception) {
                        alertErrorDialog(e.toString())
                    }
                }

            }
        }

    }


    // This Function is used for show delete Item alert box
    @SuppressLint("SetTextI18n")
    private fun deleteItemAlertBox(work: Int, id: Int) {
        val deleteAccountDialog = Dialog(requireContext())
        deleteAccountDialog.setContentView(R.layout.delete_item_alert_dialog)
        deleteAccountDialog.setCancelable(true)

        val btYes: TextView = deleteAccountDialog.findViewById(R.id.yes)
        val btNo: TextView = deleteAccountDialog.findViewById(R.id.no)
        val title: TextView = deleteAccountDialog.findViewById(R.id.title)

        if (work == 1) {
            title.text = "Are you want to delete this work experience"
        } else {
            title.text = "Are you want to delete this family background"
        }

        btNo.setOnClickListener {
            deleteAccountDialog.dismiss()
        }

        btYes.setOnClickListener {
            deleteAccountDialog.dismiss()
            if (!isNetworkAvailable()) {
                alertErrorDialog(getString(R.string.no_internet))
            } else {
                if (work == 1) {
                    deleteWorkExperience(id)
                } else {
                    deleteFamilyBackground(id)
                }
            }

        }

        deleteAccountDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteAccountDialog.show()
    }


}

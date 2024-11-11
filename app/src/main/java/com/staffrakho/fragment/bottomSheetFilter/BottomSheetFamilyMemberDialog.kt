package com.staffrakho.fragment.bottomSheetFilter


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.staffrakho.R
import com.staffrakho.activity.IdentityActivity
import com.staffrakho.dataModel.AllFilterList
import com.staffrakho.dataModel.FamilyDataModel
import com.staffrakho.databinding.BottomSheetFamilyMemberBinding
import com.staffrakho.networkModel.BaseResponse
import com.staffrakho.networkModel.jobSeekerNetworkPanel.JobSeekerViewModel
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.SessionManager
import kotlinx.coroutines.launch

class BottomSheetFamilyMemberDialog(
    val item: FamilyDataModel?,
) : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var binding: BottomSheetFamilyMemberBinding
    lateinit var sessionManager: SessionManager
    private lateinit var jobSeekerViewModel: JobSeekerViewModel
    private var relationId: String? = null
    private var livingWithYouId: String = "Yes"
 
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.background = resources.getDrawable(R.drawable.bottom_white_layout, null)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isHideable = false
            behavior.isDraggable = false
        }
        dialog.setCancelable(false)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetFamilyMemberBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        sessionManager = SessionManager(requireContext())
        jobSeekerViewModel = ViewModelProvider(this)[JobSeekerViewModel::class.java]
    

        binding.btnApplyFilter.setOnClickListener(this)
        binding.btCloseFilter.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)


        val livingWithYou = ArrayList<String>()
        livingWithYou.add("Select")
        livingWithYou.add("Yes")
        livingWithYou.add("No")

        val livingWithYouAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            livingWithYou
        )
        livingWithYouAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLivingWithYou.adapter = livingWithYouAdapter
        binding.spinnerLivingWithYou.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    livingWithYouId = livingWithYou[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        }else{
            getAllFilterList()
        }

        if (item != null) {
            binding.etName.setText(item.name)

            val defaultRelation = item.living_with_you
            val defaultIndex = livingWithYou.indexOf(defaultRelation)
            if (defaultIndex != -1) {
                binding.spinnerLivingWithYou.setSelection(defaultIndex)
                livingWithYouId = livingWithYou[defaultIndex]
            }

            binding.btnApplyFilter.text = getString(R.string.update)

        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnApplyFilter -> {
                if (checkValidation()) {
                    if (!isNetworkAvailable()) {
                        alertErrorDialog(getString(R.string.no_internet))
                    }else{
                        if (item != null) {
                            updateFamilyMember()
                        } else {
                            addFamilyMember()
                        }
                    }

                }
            }

            R.id.btCloseFilter -> {
                dismiss()
            }

            R.id.btnClose -> {
                dismiss()
            }

        }

    }

    // This Function is used for check validation
    private fun checkValidation(): Boolean {
        if (binding.etName.text.toString().isEmpty()) {
            alertErrorDialog(getString(R.string.fill_the_family_member_name))
            return false
        } else if (relationId == null) {
            alertErrorDialog(getString(R.string.select_the_relation))
            return false
        } else {
            return true
        }

    }

    // This Function is used for  get filter list from database
    private fun getAllFilterList() {
        lifecycleScope.launch {

            jobSeekerViewModel.getAllFilterList(sessionManager.getBearerToken())
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData =  checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            val allFilterList =
                                Gson().fromJson(jsonObjectData, AllFilterList::class.java)


                            val relationsName = mutableListOf("Select")
                            relationsName.addAll(allFilterList.data.relations.map { it.name.toString() })

                            val relationsAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                relationsName
                            )

                            relationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerRelation.adapter = relationsAdapter
                            binding.spinnerRelation.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        relationId = if (position > 0) {
                                            allFilterList.data.relations[position - 1].id
                                        } else {
                                            null // or any other default value
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // Do nothing
                                    }
                                }

                            if (item != null) {
                                binding.etName.setText(item.name)

                                val defaultRelation = item.relation_name
                                val defaultIndex = relationsName.indexOf(defaultRelation)
                                if (defaultIndex != -1) {
                                    binding.spinnerRelation.setSelection(defaultIndex)
                                    relationId = if (defaultIndex > 0) {
                                        allFilterList.data.relations[defaultIndex - 1].id
                                    } else {
                                        null // or any other default value
                                    }
                                }

                            }


                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for  add family member data
    private fun addFamilyMember() {
        lifecycleScope.launch {
            jobSeekerViewModel.addFamilyMember(
                sessionManager.getBearerToken(),
                binding.etName.text.toString(),
                relationId!!,
                livingWithYouId
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData =  checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            dismiss()
                            Toast.makeText(requireContext(), jsonObjectData["message"].asString, Toast.LENGTH_SHORT).show()
                            val bundle = Bundle()
                            bundle.putString(AppConstant.SCREEN,AppConstant.MENU_SCREEN)
                            bundle.putInt(AppConstant.SCREEN,2)
                            findNavController().navigate(R.id.openProfileScreen,bundle)
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for  update family member data
    private fun updateFamilyMember() {
        lifecycleScope.launch {
            jobSeekerViewModel.updateFamilyMember(
                sessionManager.getBearerToken(),
                item!!.id,
                binding.etName.text.toString(),
                relationId!!,
                livingWithYouId
            )
                .observe(viewLifecycleOwner) { jsonObject ->
                    val jsonObjectData =  checkResponse(jsonObject)
                    if (jsonObjectData != null) {
                        try {
                            Toast.makeText(requireContext(), jsonObjectData["message"].asString, Toast.LENGTH_SHORT).show()
                            dismiss()
                            val bundle = Bundle()
                            bundle.putInt(AppConstant.SCREEN,2)
                            findNavController().navigate(R.id.openProfileScreen,bundle)
                        } catch (e: Exception) {
                            alertErrorDialog(e.toString())
                        }
                    }
                }
        }

    }

    // This Function is used for show error alert box
    fun  alertErrorDialog(msg:String?){
        val alertErrorDialog= Dialog(requireActivity())
        alertErrorDialog.setCancelable(false)
        alertErrorDialog.setContentView(R.layout.alertbox_error)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertErrorDialog.window!!.attributes)

        Log.e("Error Message",msg.toString())
        alertErrorDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        alertErrorDialog.window!!.attributes = layoutParams

        val tvTitle: TextView =alertErrorDialog.findViewById(R.id.tv_title)
        val btnOk: TextView =alertErrorDialog.findViewById(R.id.btn_ok)
        tvTitle.text=msg

        btnOk.setOnClickListener {
            alertErrorDialog.dismiss()
        }

        alertErrorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertErrorDialog.show()
    }

    // This Function is used for check api response
    fun checkResponse(jsonObject : BaseResponse<JsonObject>): JsonObject? {

        if (!jsonObject.isIsError) {
            if (jsonObject.response != null) {
                try {
                    val jsonObjectData: JsonObject = jsonObject.response!!
                    val status = jsonObjectData["success"].asBoolean

                    if (status){
                        return jsonObjectData
                    }else{
                        alertErrorDialog(jsonObjectData["message"].asString)
                    }

                }catch (e:Exception){
                    alertErrorDialog("Exception : $e")
                }

            }
        } else {
            Toast.makeText(requireContext(), jsonObject.message, Toast.LENGTH_SHORT).show()
            val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("staffAppTempStorage", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.clear()
            editor.apply()
            val intent = Intent(requireActivity() , IdentityActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return null
    }
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

}
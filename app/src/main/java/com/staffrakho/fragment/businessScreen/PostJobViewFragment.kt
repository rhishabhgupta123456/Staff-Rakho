package com.staffrakho.fragment.businessScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.staffrakho.R
import com.staffrakho.dataModel.MyJobs
import com.staffrakho.databinding.FragmentPostJobViewBinding
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.SessionManager
import kotlin.random.Random

class PostJobViewFragment : Fragment() {

    lateinit var binding: FragmentPostJobViewBinding
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostJobViewBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.openPostJobsScreen)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val arrayListJson = requireArguments().getString(AppConstant.JOB_DETAIL)
        val type = object : TypeToken<MyJobs>() {}.type
        val jobDetails: MyJobs = Gson().fromJson(arrayListJson, type)


        binding.btBack.setOnClickListener{
            findNavController().navigate(R.id.openPostJobsScreen)
        }

        setScreen(jobDetails)

    }

    // This Function is used for set post job screen
    private fun setScreen(dataItem: MyJobs) {
        binding.tvJobTitle.text = dataItem.title
        binding.tvCompanyName.text = dataItem.company_name
        binding.tvLocation.text = dataItem.address
        binding.tvSalary.text = dataItem.salary_label
        binding.tvPinCode.text = dataItem.pin_code
        binding.tvRequiredEducation.text = dataItem.required_education_label
        binding.tvPhone.text = dataItem.phone
        binding.tvEmail.text = dataItem.email
        binding.tvWhatsup.text = dataItem.whatsapp

        if (dataItem.company_logo == null || dataItem.company_logo == "") {
            binding.tvLogo.visibility = View.GONE
            binding.tvDefalutImage.visibility = View.VISIBLE

            if (dataItem.company_name != null) {
                if (dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                        .joinToString("") { it[0].uppercase() }.length >= 2) {
                    binding.tvDefalutImage.text =
                        dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                            .joinToString("") { it[0].uppercase() }.substring(0, 2)
                } else {
                    binding.tvDefalutImage.text =
                        dataItem.company_name.split(" ").filter { it.isNotEmpty() }
                            .joinToString("") { it[0].uppercase() }
                }
            } else {
                binding.tvDefalutImage.text = null
            }

            when (Random.nextInt(1, 11)) {
                1 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor1
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor1))
                }

                2 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor2
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor2))
                }

                3 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor3
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor3))
                }

                4 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor4
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor4))
                }

                5 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor5
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor5))
                }

                6 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor6
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor6))
                }

                7 -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor7
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor7))
                }

                else -> {
                    binding.tvDefalutImage.setBackgroundColor(
                        resources.getColor(
                            R.color.bgColor8
                        )
                    )
                    binding.tvDefalutImage.setTextColor(resources.getColor(R.color.textColor8))
                }
            }

        } else {
            binding.tvLogo.visibility = View.VISIBLE
            binding.tvDefalutImage.visibility = View.GONE

            Glide.with(requireActivity()).load(AppConstant.MEDIA_BASE_URL + dataItem.company_logo)
                .into(binding.tvLogo)
        }


        binding.tvApplicants.text = dataItem.applicant_count
        binding.tvLastDate.text = dataItem.last_date
        binding.tvBusinessType.text = dataItem.business_type_label
        binding.tvCompanyLook.text = dataItem.company_look_label
        binding.tvProfileType.text = dataItem.profile_type_label
        binding.tvSubCategory.text = dataItem.sub_category_name
        binding.tvRoles.text = dataItem.roles
        binding.tvJobDescription.text = dataItem.description
        binding.tvJobExprience.text = dataItem.experience_label
        binding.tvCategory.text = dataItem.category_name
        binding.tvTotalVacancy.text = dataItem.number_of_vacancy
        binding.tvPosition.text = dataItem.position
        binding.tvType.text = dataItem.type_label
        binding.tvGender.text = dataItem.gender
        binding.tvState.text = dataItem.state_name
        binding.tvCity.text = dataItem.city_name

    }



}
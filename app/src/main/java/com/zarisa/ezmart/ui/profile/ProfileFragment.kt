package com.zarisa.ezmart.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentProfileBinding
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferences
    private var customerId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupAppbar()
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.show()
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPref()
        initProfile()
        bindView()
        setDataInView()
        observeStatus()
    }

    private fun bindView() {
        binding.btnRegister.setOnClickListener {
            register()
        }
        binding.btnAddNewAddress.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addAddressFragment)
        }
    }

    private fun observeStatus() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            if (it != null)
                Toast.makeText(
                    requireContext(),
                    when (it) {
                        Status.LOADING -> "در حال بارگذاری..."
                        Status.SUCCESSFUL -> "مشخصات شما با موفقیت ثبت شد."
                        else -> viewModel.statusMessage
                    }, Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun initProfile() {
        if (customerId != 0) {
            binding.lBtnsRegister.visibility = View.GONE
            binding.imageViewAvatar.visibility = View.VISIBLE
            viewModel.customerLiveData.postValue(
                Customer(
                    customerId,
                    sharedPref.getString(USER_EMAIL, "")!!,
                    sharedPref.getString(USER_NAME, "")!!,
                    sharedPref.getString(USER_AVATAR, "")!!
                )
            )
        } else {
            binding.lBtnsRegister.visibility = View.VISIBLE
            binding.imageViewAvatar.visibility = View.GONE

        }

    }

    private fun register() {
        val editor = sharedPref.edit()
        if (validateData()) {
            lifecycleScope.launch {
                viewModel.createCustomer(
                    Customer(
                        0,
                        binding.editTextEmail.text.toString(),
                        binding.editTextName.text.toString(),
                    )
                ).let {
                    it?.let {
                        editor.putInt(CUSTOMER_ID, it.id)
                        editor.putString(USER_NAME, it.first_name)
                        editor.putString(USER_EMAIL, it.email)
                        editor.putString(USER_AVATAR, it.avatar_url)
                        editor.apply()
                        binding.lBtnsRegister.visibility = View.GONE
                        binding.imageViewAvatar.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "لطفا تمامی اطلاعات را تکمیل کنید.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun validateData(): Boolean {
        var isDataValid = true
        binding.editTextEmail.let {
            if (it.text.isNullOrBlank()) {
                isDataValid = false
                it.error = "ایمیل را وارد کنید."
            } else if (!Patterns.EMAIL_ADDRESS.matcher(it.text.toString()).matches()) {
                isDataValid = false
                it.error = "ایمیل را به درستی وارد کنید."
            }
        }
        binding.editTextName.let {
            if (it.text.isNullOrBlank()) {
                isDataValid = false
                it.error = "نام را وارد کنید."
            }
        }
        return isDataValid
    }

    private fun setDataInView() {
        viewModel.customerLiveData.observe(viewLifecycleOwner) { customer ->
            if (customer != null) {
                binding.editTextName.let {
                    it.isEnabled = false
                    it.setText(customer.first_name)
                }
                binding.editTextEmail.let {
                    it.isEnabled = false
                    it.setText(customer.email)
                }
                binding.imageViewAvatar.let {
                    Glide.with(it)
                        .load(customer.avatar_url)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_error_image)
                        .transform(CenterInside(), RoundedCorners(100))
                        .into(it)
                }
            }
        }
    }

    private fun initSharedPref() {
        sharedPref = requireActivity().getSharedPreferences(CUSTOMER, Context.MODE_PRIVATE)
        customerId = sharedPref.getInt(CUSTOMER_ID, 0)
    }
}
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentProfileBinding
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

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
        binding.btnSaveAddress.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_chooseAddressFragment)
        }
        binding.textFieldAddressOne.setEndIconOnClickListener {
            removeAddress(1)
        }
        binding.textFieldAddressTwo.setEndIconOnClickListener {
            removeAddress(2)
        }
    }

    private fun removeAddress(index: Int) {
        if (index == 1) {
            if (viewModel.addressTwo.value != "") {
                viewModel.addressOne.postValue(viewModel.addressTwo.value)
                viewModel.addressTwo.postValue("")
            } else
                viewModel.addressOne.postValue("")
        } else {
            viewModel.addressTwo.postValue("")
        }
    }

    private fun observeStatus() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            if (it != null)
                Toast.makeText(
                    requireContext(),
                    when (it) {
                        Status.LOADING -> "در حال بارگذاری..."
                        Status.SUCCESSFUL -> "عملیات با موفقیت انجام شد."
                        else -> viewModel.statusMessage
                    }, Toast.LENGTH_SHORT
                ).show()
        }
        viewModel.isRegistered.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnSaveAddress.visibility = View.GONE
                binding.lBtnsRegister.visibility = View.GONE
                binding.imageViewAvatar.visibility = View.VISIBLE
                binding.textFieldAddressOne.isEndIconVisible = false
                binding.textFieldAddressTwo.isEndIconVisible = false
            } else {
                binding.btnSaveAddress.visibility = View.VISIBLE
                binding.lBtnsRegister.visibility = View.VISIBLE
                binding.imageViewAvatar.visibility = View.GONE
                binding.textFieldAddressOne.isEndIconVisible = true
                binding.textFieldAddressTwo.isEndIconVisible = true
            }
        }
    }

    private fun initProfile() {
        if (customerId != 0) {
            viewModel.isRegistered.postValue(true)
            if (viewModel.customerLiveData.value == null)
                viewModel.automaticLogin(customerId)
        }
    }

    private fun register() {
        if (validateData()) {
            viewModel.createCustomer(
                Customer(
                    0,
                    binding.editTextEmail.text.toString(),
                    binding.editTextName.text.toString(),
                    shipping = Shipping(
                        viewModel.addressOne.value.toString(),
                        viewModel.addressTwo.value.toString()
                    )
                )
            )
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
                saveDataInSharedPref(customer)
                viewModel.isRegistered.postValue(true)
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
                viewModel.addressOne.value = customer.shipping.address_1
                viewModel.addressTwo.value = customer.shipping.address_2
            }
        }
        viewModel.addressOne.observe(viewLifecycleOwner) {
            if (it != "" && it != "null" && it != null) {
                binding.editTextAddressOne.setText(it.split(",")[0])
                binding.textFieldAddressOne.visibility = View.VISIBLE
            } else {
                binding.textFieldAddressOne.visibility = View.GONE
            }
        }
        viewModel.addressTwo.observe(viewLifecycleOwner) {
            if (it != "" && it != "null" && it != null) {
                binding.editTextAddressTwo.setText(it.split(",")[0])
                binding.textFieldAddressTwo.visibility = View.VISIBLE
                binding.btnSaveAddress.isEnabled = false
            } else {
                binding.textFieldAddressTwo.visibility = View.GONE
                binding.btnSaveAddress.isEnabled = true
            }
        }
    }

    private fun saveDataInSharedPref(customer: Customer) {

        val editor = sharedPref.edit()
        editor.putInt(CUSTOMER_ID, customer.id)
        editor.putString(USER_NAME, customer.first_name)
        editor.putString(USER_EMAIL, customer.email)
        editor.apply()
    }

    private fun initSharedPref() {
        sharedPref = requireActivity().getSharedPreferences(CUSTOMER, Context.MODE_PRIVATE)
        customerId = sharedPref.getInt(CUSTOMER_ID, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.statusLiveData.postValue(null)
    }
}
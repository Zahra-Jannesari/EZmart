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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zarisa.ezmart.databinding.FragmentProfileBinding
import com.zarisa.ezmart.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    val viewModel: ProfileViewModel by viewModels()
    private lateinit var sharedPref: SharedPreferences
    private var customerId = 0
    private var orderId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(false)
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPref()
        initProfile()
        setDataInView()
        observeStatus()
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
            viewModel.getCustomer(customerId)
        } else {
            binding.lBtnsRegister.visibility = View.VISIBLE
            binding.btnRegister.setOnClickListener {
                register()
            }
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
                        binding.editTextUserName.text.toString()
                    )
                ).let {
                    it?.let {
                        editor.putInt(CUSTOMER_ID, it).apply()
                        bindOrderToCustomer(it)
                        binding.lBtnsRegister.visibility = View.GONE
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

    private fun bindOrderToCustomer(customerId: Int) {
        if (orderId != 0) {
            viewModel.updateOrder(customerId, orderId)
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
        binding.editTextUserName.let {
            if (it.text.isNullOrBlank()) {
                isDataValid = false
                it.error = "نام کاربری را وارد کنید."
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
                binding.editTextUserName.let {
                    it.isEnabled = false
                    it.setText(customer.username)
                }
                binding.editTextEmail.let {
                    it.isEnabled = false
                    it.setText(customer.email)
                }
            }
        }
    }

    private fun initSharedPref() {
        sharedPref = requireActivity().getSharedPreferences(CUSTOMER, Context.MODE_PRIVATE)
        customerId = sharedPref.getInt(CUSTOMER_ID, 0)
        orderId = sharedPref.getInt(ORDER_ID, 0)
    }
}
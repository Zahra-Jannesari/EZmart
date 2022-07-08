package com.zarisa.ezmart.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.zarisa.ezmart.adapter.AddressListAdapter
import com.zarisa.ezmart.databinding.FragmentChooseAddressBinding
import com.zarisa.ezmart.model.ADDRESSES
import com.zarisa.ezmart.model.EZ_SHARED_PREF
import com.zarisa.ezmart.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseAddressFragment : Fragment() {
    lateinit var binding: FragmentChooseAddressBinding
    val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferences
    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.hide()
        setHasOptionsMenu(false)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupAppbar()
        binding = FragmentChooseAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(EZ_SHARED_PREF, Context.MODE_PRIVATE)
        bindView()
    }

    private fun bindView() {
        val addressList=sharedPref.getStringSet(ADDRESSES, emptySet())?.toList()
        if (addressList.isNullOrEmpty()){
            binding.imageViewNoAddress.visibility=View.VISIBLE
        }
        else{
            binding.imageViewNoAddress.visibility=View.GONE
            val adapter = AddressListAdapter { address -> onAddressClick(address) }
            binding.rvAddress.adapter = adapter
            adapter.submitList(addressList)
        }
    }

    private fun onAddressClick(address: String) {
        if (viewModel.addressOne.value == "")
            viewModel.addressOne.postValue(address)
        else if (viewModel.addressOne.value != address) viewModel.addressTwo.postValue(address)
        activity?.onBackPressed()
    }
}
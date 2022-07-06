package com.zarisa.ezmart.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.zarisa.ezmart.databinding.FragmentChooseAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseAddressFragment : Fragment() {
    lateinit var binding: FragmentChooseAddressBinding
    val viewModel: ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}
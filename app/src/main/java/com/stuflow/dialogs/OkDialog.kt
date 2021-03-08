package com.stuflow.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stuflow.R
import com.stuflow.activities.MainActivity
import com.stuflow.databinding.DialogOkBinding
import com.stuflow.viewmodels.MainViewModel

class OkDialog: BottomSheetDialogFragment() {
    private lateinit var binding: DialogOkBinding
    private lateinit var viewModel: MainViewModel
    private val args: OkDialogArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).let{ viewModel = it.viewModel }
        binding = DataBindingUtil
            .inflate(inflater, R.layout.dialog_ok, container, false)
        binding.text.text = args.text
        binding.pos.setOnClickListener {
            findNavController().apply{
                previousBackStackEntry?.savedStateHandle?.set("ok", true)
                navigate(R.id.action_okDialog_to_homeFragment)
            }
        }
        binding.neg.setOnClickListener {
            findNavController().apply {
                previousBackStackEntry?.savedStateHandle?.set("ok", false)
                navigate(R.id.action_okDialog_to_homeFragment)
            }
        }
        return binding.root
    }
}
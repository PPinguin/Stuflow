package com.stuflow.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stuflow.R
import com.stuflow.databinding.DialogUserBinding

class UserInfoDialog : BottomSheetDialogFragment(){

    private lateinit var binding: DialogUserBinding
    private val args: UserInfoDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.dialog_user, container, false)
        binding.user = args.user
        return binding.root
    }
}
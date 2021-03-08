package com.stuflow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stuflow.R
import com.stuflow.activities.StartActivity
import com.stuflow.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var dataBinding:FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_sign_in, container, false)
        dataBinding.goBtn.setOnClickListener {
            (requireActivity() as StartActivity).viewModel.signIn(
                dataBinding.email.text.toString(),
                dataBinding.password.text.toString()
            )
        }
        dataBinding.signInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        return dataBinding.root
    }
}
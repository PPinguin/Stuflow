package com.stuflow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.stuflow.R
import com.stuflow.activities.StartActivity
import com.stuflow.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var dataBinding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_sign_up, container, false)
        dataBinding.goBtn.setOnClickListener {
            (requireActivity() as StartActivity).viewModel.signUp(
                dataBinding.email.text.toString(),
                dataBinding.password.text.toString(),
                dataBinding.name.text.toString()
            )
        }
        return dataBinding.root
    }
}
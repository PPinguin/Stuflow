package com.stuflow.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.stuflow.R
import com.stuflow.activities.MainActivity
import com.stuflow.activities.StartActivity
import com.stuflow.databinding.FragmentSettingsBinding
import com.stuflow.repository.DatabaseConnection
import com.stuflow.viewmodels.MainViewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_settings, container, false)
        (requireActivity() as MainActivity).let{
            viewModel = it.viewModel
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.let{ ab -> ab.title = "" }
        }
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_house)
            setNavigationOnClickListener { submit() }
        }
        binding.exit.setOnClickListener {
            DatabaseConnection.auth.signOut()
            requireActivity().finish()
            startActivity(
                Intent(requireContext(), StartActivity::class.java)
            )
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){ submit() }
        binding.user = viewModel.user
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("ok")
            ?.observe(viewLifecycleOwner){
                if (it) {
                    viewModel.updateUser(
                        binding.name.text.toString(),
                        binding.contact.text.toString()
                    )
                    requireActivity().recreate()
                } else {
                    findNavController().popBackStack()
                }
            }
        return binding.root
    }

    private fun submit(){
        findNavController()
            .navigate(
                SettingsFragmentDirections
                    .actionSettingsFragmentToOkDialog(getString(R.string.save_changes))
            )
    }
}
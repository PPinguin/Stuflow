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
    private var isEdited = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_settings, container, false)
        (requireActivity() as MainActivity).let {
            viewModel = it.viewModel
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.let { ab -> ab.title = getString(R.string.settings) }
        }
        binding.apply {
            toolbar.apply {
                setNavigationIcon(R.drawable.ic_house)
                setNavigationOnClickListener {
                    if (isEdited) submit(getString(R.string.save_changes))
                    else findNavController().popBackStack()
                }
            }
            exit.setOnClickListener {
                DatabaseConnection.auth.signOut()
                requireActivity().finish()
                startActivity(
                    Intent(requireContext(), StartActivity::class.java)
                )
            }
            name.setOnFocusChangeListener { _, hasFocus ->
                isEdited = if (hasFocus) hasFocus else isEdited
            }
            contact.setOnFocusChangeListener { _, hasFocus ->
                isEdited = if (hasFocus) hasFocus else isEdited
            }
            user = viewModel.user
        }
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("ok")
            ?.observe(viewLifecycleOwner) {
                if (it) {
                    viewModel.updateUser(
                        binding.name.text.toString(),
                        binding.contact.text.toString()
                    )
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                } else {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (isEdited) submit(getString(R.string.save_changes))
            else findNavController().popBackStack()
        }
        return binding.root
    }

    private fun submit(text: String) {
        findNavController()
            .navigate(
                SettingsFragmentDirections
                    .actionSettingsFragmentToOkDialog(text)
            )
    }
}
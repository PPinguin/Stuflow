package com.stuflow.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stuflow.R
import com.stuflow.activities.MainActivity
import com.stuflow.databinding.FragmentEditBinding
import com.stuflow.models.Question
import com.stuflow.repository.DatabaseConnection
import com.stuflow.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditFragment:Fragment(R.layout.fragment_edit){

    private lateinit var binding: FragmentEditBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var date : Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = (requireActivity() as MainActivity).viewModel
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_edit, container, false)
        date = Calendar.getInstance()
        binding.date.text = SimpleDateFormat("dd.MM.yy",Locale.getDefault())
            .format(date.time)
        binding.theme.adapter = object:ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.themes)
        ){}
        binding.postFab.setOnClickListener {
            val question = Question(
                System.currentTimeMillis().toString(),
                binding.title.text.toString(),
                binding.theme.selectedItem as String,
                DatabaseConnection.currentUser!!.uid
            )
            viewModel.postQuestion(
                question,
                binding.content.text.toString(),
                SimpleDateFormat("dd.MM.yy",Locale.getDefault()).format(date.time)
            )
            viewModel.callback.observe(viewLifecycleOwner){
                if (it == getString(R.string.post_success)) findNavController().popBackStack()
            }
        }
        binding.date.setOnClickListener {
            DatePickerDialog(requireContext(), { _: DatePicker, y: Int, m: Int, d: Int ->
                date.set(y,m,d)
                binding.date.text = SimpleDateFormat("dd.MM.yy",Locale.getDefault())
                    .format(date.time)
            },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        return binding.root
    }
}
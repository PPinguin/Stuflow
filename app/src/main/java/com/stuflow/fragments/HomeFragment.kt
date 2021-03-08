package com.stuflow.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.stuflow.R
import com.stuflow.activities.MainActivity
import com.stuflow.adapters.QuestionDiffCallback
import com.stuflow.adapters.QuestionsListAdapter
import com.stuflow.databinding.FragmentHomeBinding
import com.stuflow.models.User
import com.stuflow.viewmodels.MainViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var listAdapter: QuestionsListAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_home, container, false)
        (requireActivity() as MainActivity).let{
            viewModel = it.viewModel
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.let{ ab -> ab.title = "" }
        }
        setHasOptionsMenu(true)
        binding.viewModel = viewModel
        listAdapter = QuestionsListAdapter(QuestionDiffCallback()){
            NavHostFragment.findNavController(this)
                .navigate(HomeFragmentDirections.actionHomeFragmentToQuestionFragment(it))
        }
        binding.list.adapter = listAdapter
            binding.create.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_editFragment)
            }
            viewModel.questionsList.observe(viewLifecycleOwner) {
                listAdapter.submitList(it)
            }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.user -> {
                findNavController()
                    .navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToUserInfo(viewModel.user ?: User())
                    )
                true
            }
            R.id.edit -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
package com.stuflow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.ktx.getValue
import com.stuflow.R
import com.stuflow.activities.MainActivity
import com.stuflow.adapters.CommentDiffCallback
import com.stuflow.adapters.CommentsListAdapter
import com.stuflow.databinding.FragmentQuestionBinding
import com.stuflow.models.User
import com.stuflow.repository.DatabaseConnection
import com.stuflow.viewmodels.QuestionViewModel

class QuestionFragment : Fragment() {

    private lateinit var binding: FragmentQuestionBinding
    private lateinit var  viewModel: QuestionViewModel
    private lateinit var listAdapter: CommentsListAdapter
    private val args: QuestionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_question, container, false)
        (requireActivity() as MainActivity).apply{
            setSupportActionBar(binding.toolbar)
            supportActionBar?.let{ it.title = "Вопрос" }
        }
        setHasOptionsMenu(true)
        binding.viewModel = viewModel
        viewModel.initQuestion(args.question)
        viewModel.callback.observe(viewLifecycleOwner){
            if (viewModel.author?.email == DatabaseConnection.currentUser!!.email)
                binding.toolbar.inflateMenu(R.menu.question_toolbar)
            if (it == "deleted")
                findNavController().popBackStack()
        }
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_house)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        binding.postComment.setOnClickListener {
            binding.comment.text.apply{
                viewModel.postComment(toString())
                clear()
            }
        }
        binding.author.setOnClickListener { userInfo() }
        listAdapter = CommentsListAdapter(CommentDiffCallback()){
            userInfo(it.uid)
        }
        binding.list.adapter = listAdapter
        viewModel.commentsList.observe(viewLifecycleOwner){
            it?.let{
                listAdapter.submitList(it)
            }
        }
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                viewModel.deleteQuestion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun userInfo(uid: String? = null){
        if (uid == null) {
            findNavController()
                .navigate(
                    QuestionFragmentDirections
                        .actionQuestionFragmentToUserInfo(viewModel.author ?: User())
                )
        } else {
            DatabaseConnection.questionsRef!!
                .child("${viewModel.question.get()!!.uid}/comments/$uid/author")
                .get()
                .addOnSuccessListener {
                    DatabaseConnection.usersRef!!.child(it.getValue<String>()!!).get()
                        .addOnSuccessListener { u ->
                            findNavController()
                                .navigate(
                                    QuestionFragmentDirections
                                        .actionQuestionFragmentToUserInfo(
                                            u.getValue<User>() ?: User()
                                        )
                                )
                        }
                }

        }
    }
}
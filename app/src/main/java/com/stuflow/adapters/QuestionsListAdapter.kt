package com.stuflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.getValue
import com.stuflow.R
import com.stuflow.models.Question
import com.stuflow.repository.DatabaseConnection

class QuestionsListAdapter(
        diffCallback: QuestionDiffCallback,
        val call: (q: Question) -> Unit
    ): ListAdapter<Question, QuestionsListAdapter.ArticleViewHolder>(diffCallback){

    inner class ArticleViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun bind(question: Question){
            view.apply {
                findViewById<TextView>(R.id.theme).text = question.theme
                findViewById<TextView>(R.id.title).text = question.title
                findViewById<TextView>(R.id.author).text = question.author
                setOnClickListener{ call(question) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class QuestionDiffCallback: DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }
}
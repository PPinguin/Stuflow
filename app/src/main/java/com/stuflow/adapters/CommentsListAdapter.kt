package com.stuflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stuflow.R
import com.stuflow.models.Comment

class CommentsListAdapter(
    diffCallback: CommentDiffCallback,
    val call: (c: Comment) -> Unit
):
    ListAdapter<Comment, CommentsListAdapter.CommentViewHolder>(diffCallback){

    inner class CommentViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun bind(comment: Comment){
            view.apply {
                findViewById<TextView>(R.id.author).apply {
                    text = comment.author
                    setOnClickListener { call(comment) }
                }
                findViewById<TextView>(R.id.content).text = comment.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CommentDiffCallback():DiffUtil.ItemCallback<Comment>(){
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}
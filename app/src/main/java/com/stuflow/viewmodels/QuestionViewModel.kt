package com.stuflow.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue
import com.stuflow.models.Comment
import com.stuflow.models.Question
import com.stuflow.models.User
import com.stuflow.repository.DatabaseConnection

class QuestionViewModel(app: Application): AndroidViewModel(app) {
    var question = ObservableField<Question>()
    var content = ObservableField<String>()
    var author: User? = null
    val commentsList = MutableLiveData<MutableList<Comment>?>(mutableListOf())
    val list = mutableListOf<Comment>()
    val callback = MutableLiveData("")
    private val listEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            DatabaseConnection.usersRef!!
                .child(snapshot.child("author").getValue<String>()!!)
                .child("name")
                .get()
                .addOnSuccessListener { name ->
                    list.add(snapshot.getValue<Comment>()!!.also {
                        it.uid = snapshot.key
                        it.author = name.getValue<String>()!!
                    })
                    commentsList.postValue(list)
                }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }

        override fun onChildRemoved(snapshot: DataSnapshot) { }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

        override fun onCancelled(error: DatabaseError) { }
    }

    fun initQuestion(q: Question){
        DatabaseConnection.questionsRef!!.child(q.uid).get()
            .addOnSuccessListener {
                question.set(q)
                content.set(it.child("content").getValue<String>())
                DatabaseConnection.usersRef!!.child(it.child("author").getValue<String>()!!)
                    .get().addOnSuccessListener { a ->
                        author = a.getValue(User::class.java)
                        callback.postValue("ok")
                    }
            }
        DatabaseConnection.questionsRef!!.child(q.uid).child("comments")
            .addChildEventListener(listEventListener)
    }

    fun postComment(comment: String) {
        val key = DatabaseConnection.questionsRef!!
            .child(question.get()!!.uid)
            .child("comments")
            .push()
            .key ?: return
        DatabaseConnection.questionsRef!!
            .child(question.get()!!.uid)
            .child("comments")
            .child(key)
            .updateChildren(
                mapOf(
                    "author" to DatabaseConnection.currentUser!!.uid,
                    "content" to comment
                )
        )
    }

    fun deleteQuestion() {
        DatabaseConnection.questionsRef!!.child(question.get()!!.uid).removeValue{_, _ ->
            callback.postValue("deleted")
        }
    }
}
package com.stuflow.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue
import com.stuflow.R
import com.stuflow.models.Question
import com.stuflow.models.User
import com.stuflow.repository.DatabaseConnection
import kotlinx.coroutines.launch

class MainViewModel(val app: Application) : AndroidViewModel(app) {
    val callback = MutableLiveData("ok")
    val questionsList = MutableLiveData<MutableList<Question>?>(mutableListOf())
    val list = mutableListOf<Question>()
    var user: User? = null
    private val listEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            DatabaseConnection.usersRef!!
                .child(snapshot.child("author").getValue<String>()!!)
                .child("name")
                .get()
                .addOnSuccessListener {
                    list.add(
                        Question(
                            snapshot.key!!,
                            snapshot.child("title").getValue<String>()!!,
                            snapshot.child("theme").getValue<String>()!!,
                            it.getValue<String>()!!
                        )
                    )
                    questionsList.postValue(list)
                }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            list[list.indexOfFirst { q -> q.uid == snapshot.key }].apply {
                title = snapshot.child("title").getValue<String>()!!
                theme = snapshot.child("theme").getValue<String>()!!
            }
            questionsList.postValue(list)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            list.removeAt(list.indexOfFirst { q -> q.uid == snapshot.key })
            questionsList.postValue(list)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {
            callback.value = app.getString(R.string.update_failed)
        }
    }

    init {
        DatabaseConnection.questionsRef!!.limitToLast(10)
            .addChildEventListener(listEventListener)
        viewModelScope.launch {
            DatabaseConnection.usersRef!!.child(DatabaseConnection.currentUser!!.uid).get()
                .addOnSuccessListener {
                    user = it.getValue(User::class.java)
                }
        }
    }

    fun postQuestion(question: Question, content: String, date: String) {
        val key = DatabaseConnection.questionsRef!!.push().key ?: return
        DatabaseConnection.questionsRef!!.child(key).updateChildren(
            mapOf(
                "title" to question.title,
                "theme" to question.theme,
                "author" to question.author,
                "date" to date,
                "content" to content,
                "comments" to null
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) callback.value = app.getString(R.string.post_success)
        }
    }

    fun updateUser(name: String, contacts: String) {
        user?.let { it.contacts = contacts }
        DatabaseConnection.usersRef!!.child(DatabaseConnection.currentUser!!.uid).updateChildren(
            mapOf(
                "name" to name,
                "contacts" to contacts
            )
        )
    }
}
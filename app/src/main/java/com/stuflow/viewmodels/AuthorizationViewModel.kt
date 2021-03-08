package com.stuflow.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.stuflow.R
import com.stuflow.models.User
import com.stuflow.repository.DatabaseConnection

class AuthorizationViewModel(val app: Application) : AndroidViewModel(app) {
    val message = MutableLiveData("")

    init {
        if (DatabaseConnection.initDatabase()){
            if (DatabaseConnection.initAuth())
                message.value = "ok"
        } else message.value = app.getString(R.string.no_connection)
    }

    fun signIn(email: String, password: String) {
        DatabaseConnection.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    DatabaseConnection.currentUser = DatabaseConnection.auth.currentUser
                    message.value = "ok"
                }
                else message.value = app.getString(R.string.sign_in_failed)
            }
    }

    fun signUp(email: String, password: String, name: String) {
        DatabaseConnection.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    DatabaseConnection.apply {
                        currentUser = auth.currentUser
                        usersRef!!
                            .updateChildren(
                                mapOf(
                                    currentUser!!.uid to User(email, name)
                                )
                            )
                    }
                    message.value = "ok"
                }
                else message.value = app.getString(R.string.sign_up_failed)
            }
    }
}
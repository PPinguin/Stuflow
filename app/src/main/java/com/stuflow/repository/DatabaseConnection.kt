package com.stuflow.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object DatabaseConnection {
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    var usersRef: DatabaseReference? = null
    var questionsRef: DatabaseReference? = null
    var currentUser: FirebaseUser? = null

    fun initAuth():Boolean{
        auth = Firebase.auth
        auth.setLanguageCode("ru")
        currentUser = auth.currentUser
        return currentUser != null
    }

    fun initDatabase():Boolean{
        database = Firebase.database
        usersRef = database.getReference("users")
        questionsRef = Firebase.database.getReference("questions")
        return usersRef != null && questionsRef != null
    }
}
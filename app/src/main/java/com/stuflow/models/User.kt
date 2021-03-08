package com.stuflow.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User (
    val email:String? = null,
    var name:String? = null,
    var contacts: String? = null
): Serializable{
    init {
        if (contacts == null) contacts = email
    }
}
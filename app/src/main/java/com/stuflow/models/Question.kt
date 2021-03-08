package com.stuflow.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Question(
    val uid:   String,
    var title: String,
    var theme: String,
    var author: String
):Serializable

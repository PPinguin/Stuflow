package com.stuflow.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment (
    var uid: String? = null,
    var author: String? = null,
    val content: String? = null
){}
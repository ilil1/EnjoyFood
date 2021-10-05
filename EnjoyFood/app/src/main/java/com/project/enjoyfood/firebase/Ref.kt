package com.project.enjoyfood.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class Ref {
    companion object {
        private val database = Firebase.database
        private val storage = Firebase.storage
        val storageRef = storage.reference

        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
    }
}
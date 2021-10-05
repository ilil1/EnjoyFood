package com.project.enjoyfood.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Ref {
    companion object {

        private val database = Firebase.database
        const val DB_ARTICLES = "board"
        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
    }
}
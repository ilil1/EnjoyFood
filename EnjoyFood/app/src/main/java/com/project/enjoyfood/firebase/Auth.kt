package com.project.enjoyfood.firebase

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class Auth {
    companion object {
        private lateinit var auth: FirebaseAuth

        fun getUid() : String {

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }

        fun getTime() : String {

            val long_now = System.currentTimeMillis()
            val t_date = Date(long_now)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("ko","KR"))
            val str_date = dateFormat.format(t_date)

            return str_date
        }
    }
}
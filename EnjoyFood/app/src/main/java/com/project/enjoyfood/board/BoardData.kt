package com.project.enjoyfood.board

import android.net.Uri

data class BoardData(
    val title: String = "",
    val content: String = "",
    val uid: String = "",
    val time: String = "",
    val key: String = "",
    val imageUrl: String = ""
) {
    constructor(): this("","","","","","")
}
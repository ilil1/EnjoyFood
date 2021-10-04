package com.project.enjoyfood.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.project.enjoyfood.R

class CommentListAdapter(val commentList : MutableList<CommentData>) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view =convertView

        if(view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list, parent, false)
        }

        val title = view?.findViewById<TextView>(R.id.commentText)
        val time = view?.findViewById<TextView>(R.id.timeText)

        title!!.text = commentList[position].comment
        time!!.text = commentList[position].time

        return view!!
    }
}
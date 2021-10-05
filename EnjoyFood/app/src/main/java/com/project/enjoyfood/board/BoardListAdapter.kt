package com.project.enjoyfood.board

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator
import android.media.CamcorderProfile.get
import android.net.sip.SipErrorCode.toString
import android.nfc.tech.NfcB.get
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.project.enjoyfood.databinding.BoardListBinding
import java.lang.reflect.Array.get
import java.nio.file.Paths.get
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat.startActivity

import com.project.enjoyfood.MainActivity
import com.project.enjoyfood.firebase.Ref

class BoardListAdapter(val activity : Context): ListAdapter<BoardData, BoardListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: BoardListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoList: BoardData) {

            binding.titleText.text = todoList.title
            binding.contentText.text = todoList.content
            binding.timeText.text = todoList.time

//            if(BoardData.imageUrl.isNotEmpty()) {
//                Glide.with(binding.thumbnailImageView)
//                    .load(BoardData.imageUrl)
//                    .into(binding.thumbnailImageView)
//            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BoardListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, BoardInActivity::class.java)
            intent.putExtra("key", currentList[position].key)
            Log.d("tg", "${position} 선택")
            activity.startActivity(intent)
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BoardData>() {
            override fun areItemsTheSame(oldItem: BoardData, newItem: BoardData): Boolean {
                return oldItem.time == newItem.time
            }
            override fun areContentsTheSame(oldItem: BoardData, newItem: BoardData): Boolean {
                return oldItem == newItem
            }
        }
    }
}

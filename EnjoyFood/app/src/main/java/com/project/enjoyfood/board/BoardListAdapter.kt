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
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

import com.project.enjoyfood.MainActivity
import com.project.enjoyfood.firebase.Ref

class BoardListAdapter(val activity : Context): RecyclerView.Adapter<BoardListAdapter.ViewHolder>() {

    private var boardList : MutableList<BoardData> = mutableListOf()

    inner class ViewHolder(private val binding: BoardListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(boardData: BoardData) {

            if (boardData.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(boardData.imageUrl)
                    .into(binding.thumbnailImageView)
            }
            binding.titleText.text = boardData.title
            binding.contentText.text = boardData.content
            binding.timeText.text = boardData.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BoardListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(boardList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, BoardInActivity::class.java)
            intent.putExtra("key", boardList[position].key)
            Log.d("tg", "${position} ??????")
            activity.startActivity(intent)
        }
    }
    fun add(item : BoardData) {
        boardList.add(item)
    }
    fun clear() = boardList.clear()

    override fun getItemCount(): Int {
        return boardList.size
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

package com.project.enjoyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.enjoyfood.R
import com.project.enjoyfood.board.BoardData
import com.project.enjoyfood.board.BoardInActivity
import com.project.enjoyfood.board.BoardListAdapter
import com.project.enjoyfood.board.BoardWriteActivity
import com.project.enjoyfood.databinding.FragmentTalkBinding
import com.project.enjoyfood.firebase.Auth
import com.project.enjoyfood.firebase.Ref


class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding
    private lateinit var boardListAdapter : BoardListAdapter

    private val TAG = TalkFragment::class.java.simpleName

    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

        boardListAdapter = BoardListAdapter(requireContext())

        recyclerView = binding.boardListArr

        binding.writBtn.setOnClickListener {
            val intent = Intent(requireContext(), BoardWriteActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.boardListArr.layoutManager = layoutManager
        (binding.boardListArr.layoutManager as LinearLayoutManager).reverseLayout = true
        (binding.boardListArr.layoutManager as LinearLayoutManager).stackFromEnd = true
        binding.boardListArr.adapter = boardListAdapter

        getFBBoardData()

        return binding.root
    }
    private fun getFBBoardData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardListAdapter.clear()
                for (dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.toString())

                    val item = dataModel.getValue(BoardData::class.java) as BoardData
                    boardListAdapter.add(item)
                }
                boardListAdapter.notifyDataSetChanged()
                Log.d("wwww", "adapter insertion complete")
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        Ref.boardRef.addValueEventListener(postListener)
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: ")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        boardListAdapter.clear()
        //Ref.boardRef.removeEventListener(listener)
    }
}
package com.project.enjoyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.enjoyfood.R
import com.project.enjoyfood.board.BoardData
import com.project.enjoyfood.board.BoardInActivity
import com.project.enjoyfood.board.BoardListAdapter
import com.project.enjoyfood.board.BoardWriteActivity
import com.project.enjoyfood.databinding.FragmentTalkBinding
import com.project.enjoyfood.firebase.Ref


class TalkFragment : Fragment(R.layout.fragment_talk) {

    private var binding : FragmentTalkBinding? = null
    private lateinit var boardListAdapter : BoardListAdapter

    private val boardList = mutableListOf<BoardData>()

    private val TAG = TalkFragment::class.java.simpleName

    private var listener = object: ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            val BoardData = dataSnapshot.getValue(BoardData::class.java)
            BoardData ?: return
            boardList.add(BoardData)
            boardListAdapter.notifyDataSetChanged()

            boardListAdapter.submitList(boardList)
        }
        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
        }
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentTalkBinding.bind(view)
        binding = fragmentHomeBinding

        boardListAdapter = BoardListAdapter(activity = requireContext())

        fragmentHomeBinding.boardListArr.layoutManager = LinearLayoutManager(context)
        (fragmentHomeBinding.boardListArr.layoutManager as LinearLayoutManager).reverseLayout = true
        (fragmentHomeBinding.boardListArr.layoutManager as LinearLayoutManager).stackFromEnd = true
        fragmentHomeBinding.boardListArr.adapter = boardListAdapter

        fragmentHomeBinding.writBtn.setOnClickListener {
            val intent = Intent(requireContext(), BoardWriteActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        boardList.clear()
        Ref.boardRef.addChildEventListener(listener)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Ref.boardRef.removeEventListener(listener)
    }
}
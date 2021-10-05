package com.project.enjoyfood.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.project.enjoyfood.R
import com.project.enjoyfood.comment.CommentData
import com.project.enjoyfood.comment.CommentListAdapter
import com.project.enjoyfood.databinding.ActivityBoardInBinding
import com.project.enjoyfood.firebase.Auth
import com.project.enjoyfood.firebase.Ref

class BoardInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInBinding
    private lateinit var key: String
    private val commentList = mutableListOf<CommentData>()
    private lateinit var commentAdapter : CommentListAdapter

    private val TAG = BoardInActivity::class.java.simpleName
    private lateinit var writerUid : String
    private val boardKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_in)

        binding.boardInmenu.setOnClickListener {
            showDialog()
        }

        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {
            insertComment()
        }

        getCommentData(key)

        commentAdapter = CommentListAdapter(commentList)
        binding.commentListView.adapter = commentAdapter

    }
    fun getCommentData(key : String){
        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()

                for(dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentData::class.java)
                    commentList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }

                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

                Log.d(TAG,"ladPost:onCancelled",error.toException())

            }
        }
        Ref.commentRef.child(key).addValueEventListener(postListener)
    }
    fun insertComment() {
        //comment
        // -BoardKey
        //   -CommentKey
        //      -CommentData
        Ref.commentRef
            .child(key)
            .push()
            .setValue(CommentData(
                binding.commentText.text.toString(),
                Auth.getTime()
            ))
        Toast.makeText(this,"댓글 입력 완료",Toast.LENGTH_LONG).show()
        binding.commentText.setText("")
    }
    private fun showDialog() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog,null)
        val builder = AlertDialog.Builder(this).setView(dialogView).setTitle("게시글 수정/삭제")
        val alertDialog = builder.show()

        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this,"게시글을 수정합니다.",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
            finish()
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            Ref.boardRef.child(key).removeValue()
            Ref.storageRef.child(key + ".png").delete()
            Toast.makeText(this,"게시글이 삭제되었습니다.",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun getImageData(key : String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageView = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this /* context */)
                    .load(task.result)
                    .into(imageView)
            } else {
                binding.imageArea.isVisible = false
            }
        })
    }
    private fun getBoardData(key : String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {

                    val dataModel = dataSnapshot.getValue(BoardData::class.java)

                    binding.titleText.text = dataModel!!.title
                    binding.contentText.text = dataModel!!.content
                    binding.timeText.text = dataModel!!.time
                    writerUid = dataModel!!.uid

                    val myUid = Auth.getUid()

                    if (myUid.equals(writerUid)) {
                        binding.boardInmenu.isVisible = true
                    }

                } catch(e : Exception) {
                    Log.d(TAG,"불러올 Data가 존재하지 않습니다.")
                }
            }
            override fun onCancelled(error: DatabaseError) {

                Log.d(TAG,"ladPost:onCancelled",error.toException())

            }
        }
        Ref.boardRef.child(key).addValueEventListener(postListener)
    }
}
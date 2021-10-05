package com.project.enjoyfood.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.project.enjoyfood.R
import com.project.enjoyfood.databinding.ActivityBoardEditBinding
import com.project.enjoyfood.databinding.ActivityBoardInBinding
import com.project.enjoyfood.firebase.Auth
import com.project.enjoyfood.firebase.Ref

class BoardEditActivity : AppCompatActivity() {

    private lateinit var  key : String
    private lateinit var binding : ActivityBoardEditBinding
    private val TAG = BoardEditActivity::class.java.simpleName
    private lateinit var writerUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)

        binding.editBtn.setOnClickListener {

            val title = binding.titleText.text.toString()
            val content = binding.contentText.text.toString()
            val uid = Auth.getUid()
            val time = Auth.getTime()

            Ref.boardRef
                .child(key).setValue(BoardData(title, content, uid, time, key))

            Toast.makeText(this,"수정완료",Toast.LENGTH_LONG).show()
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

            }
        })
    }
    private fun getBoardData(key : String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d(TAG, dataSnapshot.toString())
                try {
                    val dataModel = dataSnapshot.getValue(BoardData::class.java)

                    binding.titleText.setText(dataModel?.title)
                    binding.contentText.setText(dataModel?.content)
                    writerUid = dataModel!!.uid
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
package com.project.enjoyfood.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.project.enjoyfood.R
import com.project.enjoyfood.databinding.ActivityBoardWriteBinding
import com.project.enjoyfood.firebase.Auth
import com.project.enjoyfood.firebase.Ref
import com.project.enjoyfood.firebase.Ref.Companion.boardRef
import com.project.enjoyfood.firebase.Ref.Companion.storageRef
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private var selectedUri: Uri? = null
    private lateinit var binding : ActivityBoardWriteBinding
    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {

            val title = binding.titleText.text.toString()
            val content = binding.contentText.text.toString()
            val uid = Auth.getUid()
            val time = Auth.getTime()
            val key = boardRef.push().key.toString()

            var imageurl = "0"

            val ref = storageRef.child(key + ".png")
            val uploadTask = selectedUri?.let {
                    it1 -> ref.putFile(it1)
            }

            Log.d("tg", "${uploadTask} uploadTask가 뭐냐?")

            if(isImageUpload == true) {

                uploadTask?.continueWithTask {
                    ref.downloadUrl
                }?.addOnCompleteListener { task ->
                    Log.e("tttttt", "onCreate 3번????", )
                    Ref.boardRef
                        .child(key)
                        .setValue(BoardData(title, content, uid, time, key, task.result.toString()))
                }
                finish()
            }
            else {
                Ref.boardRef.child(key).setValue(BoardData(title, content, uid, time, key, imageurl))
                finish()
            }
        }
        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
            selectedUri = data?.data
            Log.d("tg", "${data?.data} data가 뭐냐?")
            Toast.makeText(this,"이미지가 업로드 되었습니다.",Toast.LENGTH_LONG).show()
        }
    }
}
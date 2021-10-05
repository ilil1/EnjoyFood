package com.project.enjoyfood.board

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
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
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

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

            Ref.boardRef
                .child(key).setValue(BoardData(title, content, uid, time, key))

            Toast.makeText(this, "게시글 입력완료", Toast.LENGTH_LONG).show()

            if(isImageUpload == true) {
                imageUpload(key)
            }

            finish()
        }
        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }
    private fun imageUpload(key : String) {
//        val storage = Firebase.storage
//        val storageRef = storage.reference
        val mountainsRef = Ref.storageRef.child(key + ".png")

        val imageView = binding.imageArea
        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode,data)

            if(resultCode == RESULT_OK && requestCode == 100) {
                binding.imageArea.setImageURI(data?.data)
                Toast.makeText(this,"이미지가 업로드 되었습니다.",Toast.LENGTH_LONG).show()
            }

    }
}
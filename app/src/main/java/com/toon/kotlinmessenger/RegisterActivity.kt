package com.toon.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.toon.lbtaapplication.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : BaseActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener(this)

        storageRef = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()

        already_have_account_textView_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            Log.d(TAG, "Intent to Login")
        }

        select_photo_button_register.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            Log.d(TAG, "Select Photo Button Clicked")

        }

    }
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            select_photo_imageView_register.setImageBitmap(bitmap)
            select_photo_button_register.alpha = 0f

        }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    signIn(email_editText_register.text.toString(), password_editText_register.text.toString())
                    uploadImageToFirebaseStorage()

                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                hideProgressDialog()
            }

    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                if (!task.isSuccessful) {
                    Log.d(TAG, "Auth Failed")
                }
                hideProgressDialog()
            }

    }

    private fun uploadImageToFirebaseStorage() {

        if (selectedPhotoUri == null) return

        val file = UUID.randomUUID().toString()
        val riversRef = storageRef.child("images/$file")

        riversRef.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {

                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                riversRef.downloadUrl.addOnSuccessListener {

                    Log.d(TAG, "File Location: $it")

                    saveUserToFirebaseStorage(it.toString())

                }

            }
            .addOnFailureListener{

                Log.d(TAG, "Failed to upload the image")

            }

    }

    private fun saveUserToFirebaseStorage(profileImageUrl: String) {

        val uid = auth.uid ?: ""

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("/users/$uid")

        val user = User(uid, username_editText_register.text.toString(), profileImageUrl)

        myRef.setValue(user)
            .addOnSuccessListener {

                Log.d(TAG, "FINAL: Saved User To Database")

            }

    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = email_editText_register.text.toString()
        if (TextUtils.isEmpty(email)) {
            email_editText_register.error = "Required."
            valid = false
        } else {
            email_editText_register.error = null
        }

        val password = password_editText_register.text.toString()
        if (TextUtils.isEmpty(password)) {
            password_editText_register.error = "Required."
            valid = false
        } else {
            password_editText_register.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        } else {



        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.register_button_register -> createAccount(email_editText_register.text.toString(), password_editText_register.text.toString())
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }

}

class User(val uid: String, val username: String, val profileImageUrl: String)

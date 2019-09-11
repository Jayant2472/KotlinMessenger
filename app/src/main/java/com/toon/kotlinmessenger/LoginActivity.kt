package com.toon.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.toon.lbtaapplication.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

        create_account_textView_login.setOnClickListener {

            finish()

            Log.d(TAG, "Intent to Register")

        }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
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

    private fun validateForm(): Boolean {
        var valid = true

        val email = email_editText_login.text.toString()
        if (TextUtils.isEmpty(email)) {
            email_editText_login.error = "Required."
            valid = false
        } else {
            email_editText_login.error = null
        }

        val password = password_editText_login.text.toString()
        if (TextUtils.isEmpty(password)) {
            password_editText_login.error = "Required."
            valid = false
        } else {
            password_editText_login.error = null
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
            R.id.login_button_login -> signIn(email_editText_login.text.toString(), password_editText_login.text.toString())
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

}

package com.toon.kotlinmessenger

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {

            val email = email_editText_login.text.toString()
            val password = password_editText_login.text.toString()

            Log.d(TAG, "E-mail is: $email")
            Log.d(TAG, "Password is: $password")

        }

        create_account_textView_login.setOnClickListener {

            finish()

            Log.d(TAG, "Intent to Register")

        }

    }
}

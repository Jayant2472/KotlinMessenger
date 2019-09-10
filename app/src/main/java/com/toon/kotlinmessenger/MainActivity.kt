package com.toon.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "Register"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {

            val email = email_editText_register.text.toString()
            val password = password_editText_register.text.toString()

            Log.d(TAG, "E-mail is: $email")
            Log.d(TAG, "Password is: $password")

        }

        already_have_account_textView_register.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            Log.d(TAG, "Intent to Login")

        }

    }
}

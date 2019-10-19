package com.toon.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.toon.lbtaapplication.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class LatestMessageActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "Latest Messages"

        auth = FirebaseAuth.getInstance()

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.nav_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId) {

            R.id.menu_new_message -> {

                Log.d(TAG, "Intent to new message activity")

                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)

            }

            R.id.menu_sign_out -> {

                Log.d(TAG, "Sign Out")

                signOut()

            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {

            Log.d(TAG, "User: $user")


        } else {

            Log.d(TAG, "User: null")

            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }

    companion object {
        private const val TAG = "LatestMessageActivity"
    }

}

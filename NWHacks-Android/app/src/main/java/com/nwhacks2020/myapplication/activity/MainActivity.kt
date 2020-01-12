package com.nwhacks2020.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nwhacks2020.myapplication.R
import com.nwhacks2020.myapplication.services.AppService


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // See if there's a sign in
        val user = AppService.getService().getSignedInUser(this)
        // Conditionally route depending on logged in user status
        val intent: Intent = if (user == null) {
            Intent(applicationContext, AppLoginActivity::class.java)
        } else {
            Intent(applicationContext, HomeActivity::class.java)
        }
        startActivity(intent)
    }
}

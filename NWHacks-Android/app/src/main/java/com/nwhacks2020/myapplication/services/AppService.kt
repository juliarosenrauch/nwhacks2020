package com.nwhacks2020.myapplication.services

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.nwhacks2020.myapplication.models.User

// For all services used in the app
class AppService {

    init {
        // Initialize any dependencies
    }

    fun getSignedInUser(context: Context): User? {
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context)
        account?.let {
            // Create user from the signed in account
            return User(it.id ?: "",
                it.displayName ?: "",
                it.displayName ?: "",
                it.email ?: ""
            )
        }
        return null
    }

    companion object {
        private var appService: AppService? = null

        fun getService(): AppService {
            appService?.let { return it }
            val newService = AppService()
            appService = newService
            return newService
        }
    }
}
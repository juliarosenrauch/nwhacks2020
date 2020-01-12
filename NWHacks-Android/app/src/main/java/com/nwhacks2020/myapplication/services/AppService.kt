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

    fun initializeNewUser(googleAccount: GoogleSignInAccount) {
        // Create a user
        initializeUser(googleAccount)
        // TODO: Save to firebase
    }

    fun getSignedInUser(context: Context): User? {
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context)
        account?.let {
            initializeUser(it)
        }
        return null
    }

    private fun initializeUser(googleAccount: GoogleSignInAccount): User {
        // Create user from the signed in account
        val user =  User(
            googleAccount.id ?: "",
            googleAccount.displayName ?: "",
            googleAccount.displayName ?: "",
            googleAccount.email ?: ""
        )
        // Initialize singleton
        AppService.user = user
        return user
    }

    companion object {
        private var appService: AppService? = null
        var user: User? = null // We'll force unwrap this just to be hacky, need to make sure we set this

        fun getService(): AppService {
            appService?.let { return it }
            val newService = AppService()
            appService = newService
            return newService
        }
    }
}
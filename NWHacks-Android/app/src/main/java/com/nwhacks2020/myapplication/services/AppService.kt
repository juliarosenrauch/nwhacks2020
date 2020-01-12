package com.nwhacks2020.myapplication.services

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.FirebaseFirestore
import com.nwhacks2020.myapplication.models.Offer
import com.nwhacks2020.myapplication.models.User

// For all services used in the app
class AppService {

    val db = FirebaseFirestore.getInstance()

    init {
        // Initialize any dependencies
    }

    fun saveOfferToFirebase(offer: Offer) {

        val offerdata = hashMapOf(
            "text" to offer.text,
            "type" to offer.type,
            "longitude" to offer.longitude,
            "latitude" to offer.latitude
        )

        db.collection("offers").document()
            .set(offerdata)
            .addOnSuccessListener { documentReference ->
                Log.d("saveOfferToFirebase method", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("saveOfferToFirebase method", "Error writing document", e)
            }
    }

    fun getOffers(){
        db.collection("offers")
            .get()
            .addOnSuccessListener { result ->
            for (document in result) {
                Log.d("getOffersFromFirebase method", "${document.id} => ${document.data}")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("getOffersFromFirebase method", "Error getting documents: ", exception)
            }
    }

    fun initializeNewUser(googleAccount: GoogleSignInAccount) {
        // Create a user
        val user = initializeUser(googleAccount)

        val userdata = hashMapOf(
            "userId" to user.userId,
            "userName" to user.userName,
            "fullName" to user.fullName,
            "email" to user.email
        )
        
        db.collection("users").document(user.userId)
            .set(userdata)
            .addOnSuccessListener { documentReference ->
                Log.d("initializeNewUser method", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("initializeNewUser method", "Error writing document", e)
            }
    }

    fun getSignedInUser(context: Context): User? {
        // TODO: Doesn't currently work
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
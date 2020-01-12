package com.nwhacks2020.myapplication.services

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.nwhacks2020.myapplication.models.Offer
import com.nwhacks2020.myapplication.models.User

// For all services used in the app
class AppService {

    val db = FirebaseFirestore.getInstance()

    fun getLocation(context: Context, onSuccess: (Location) -> Unit) {
        val location: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val currLocationTask = location.lastLocation
        currLocationTask.addOnSuccessListener { location ->
            if (location != null) {
                onSuccess(location)
            }
        }
    }

    fun saveOfferToFirebase(offer: Offer) {
        Log.e("Service save offer to firebase", "saving offer")
        val offerData = hashMapOf(
            "text" to offer.text,
            "type" to offer.type,
            "longitude" to offer.longitude,
            "latitude" to offer.latitude
        )

        db.collection("offers")
            .add(offerData)
            .addOnSuccessListener { documentReference ->
                Log.e("saveOfferToFirebase method", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.e("saveOfferToFirebase method", "Error writing document", e)
            }
    }

    fun getOffers(onSuccess: (ArrayList<Offer>) -> Unit) {
        db.collection("offers")
            .get()
            .addOnSuccessListener { result ->
                result.documents.map {
                    val data = it.data!!
                    Offer(
                        data["text"] as String,
                        data["type"] as String,
                        data["latitude"] as Double,
                        data["longitude"] as Double
                    )
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
        val user = User(
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
        var user: User? =
            null // We'll force unwrap this just to be hacky, need to make sure we set this

        fun getService(): AppService {
            appService?.let { return it }
            val newService = AppService()
            appService = newService
            return newService
        }
    }
}
package com.example.graduateproject.authentication.firebase

import com.example.graduateproject.shared_preferences.SharedPreferences
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class Firebase @Inject constructor() {
    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registerUser(email: String, password: String): Task<AuthResult> =
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

    fun loginUser(email: String, password: String): Task<AuthResult> =
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)

    fun restoreAccount(email: String): Task<Void> =
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)

    fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun logoutUser(): Unit = FirebaseAuth.getInstance().signOut()

    fun deleteAccount(): Task<Void>? = FirebaseAuth.getInstance().currentUser?.delete()

    fun reAuthenticate() {
        var email = ""
        var password = ""
        getCurrentUser()?.email?.let { email = it }
        SharedPreferences.savedPassword?.let { password = it }

        FirebaseAuth.getInstance().currentUser?.reauthenticate(
            EmailAuthProvider.getCredential(
                email, password
            )
        )
    }

    fun changePassword(password: String) = getCurrentUser()?.updatePassword(password)

    fun saveFeedback(appRating: Float, review: String): Task<DocumentReference> {
        val feedBack: HashMap<String, Any> = HashMap()
        feedBack["rating"] = appRating
        feedBack["review"] = review
        feedBack["user"] = getCurrentUser()?.email!!

        return sendToFirestore(feedBack)
    }

    private fun sendToFirestore(feedBack: HashMap<String, Any>): Task<DocumentReference> =
        database.collection("Feedback").add(feedBack)
}

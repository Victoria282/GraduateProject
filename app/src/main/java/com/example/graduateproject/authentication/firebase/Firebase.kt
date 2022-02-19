package com.example.graduateproject.authentication.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class Firebase @Inject constructor() {
    fun registerUser(email: String, password: String): Task<AuthResult> =
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

    fun loginUser(email: String, password: String): Task<AuthResult> =
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)

    fun restoreAccount(email: String): Task<Void> =
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)

    fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun logoutUser(): Unit = FirebaseAuth.getInstance().signOut()
}
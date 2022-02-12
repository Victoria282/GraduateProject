package com.example.graduateproject.authentication.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Firebase {

    private var user: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String): Task<AuthResult> =
        user.createUserWithEmailAndPassword(email, password)

    fun loginUser(email: String, password: String): Task<AuthResult> =
        user.signInWithEmailAndPassword(email, password)

    fun restoreAccount(email: String): Task<Void> =
        user.sendPasswordResetEmail(email)

    fun getCurrentUser(): FirebaseUser? = user.currentUser

    fun logoutUser(): Unit = user.signOut()
}
package com.mertg.gelirgidertakip.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var authState = mutableStateOf<AuthState>(AuthState.Idle)

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            authState.value = AuthState.Success
        } else {
            authState.value = AuthState.Idle
        }
    }

    fun login(email: String, password: String) {
        authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authState.value = AuthState.Success
                } else {
                    authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun register(email: String, password: String) {
        authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    user?.let {
                        saveUserToFirestore(it.uid, email, password)
                    }
                    authState.value = AuthState.Success
                } else {
                    authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    private fun saveUserToFirestore(uid: String, email: String, password: String) {
        val user = hashMapOf(
            "uid" to uid,
            "email" to email,
            "password" to password // Düz metin olarak şifre saklanması GÜVENLİ DEĞİLDİR!
        )
        firestore.collection("users").document(uid).set(user)
    }

    fun logout() {
        auth.signOut()
        authState.value = AuthState.Idle
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }
}

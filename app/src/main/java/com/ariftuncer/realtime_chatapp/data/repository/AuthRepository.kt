package com.ariftuncer.realtime_chatapp.data.repository

import com.ariftuncer.realtime_chatapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    fun registerWithEmailAndPass(name : String, email : String, password: String, onResult: (Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val uid = auth.currentUser!!.uid
                    val user = User(uid,name,email)
                    fireStore.collection("users").document(uid)
                        .set(user)
                        .addOnSuccessListener { onResult(true,null) }
                        .addOnFailureListener { onResult(false,it.message) }

                }
                else{
                    onResult(false,task.exception?.message)
                }
            }
    }
    fun loginWithEmailAndPass(email : String, password: String, onResult: (Boolean, String?) -> Unit){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    onResult(true,null)
                }
                else{
                    onResult(false,task.exception?.message)
                }
            }
    }

    fun registerWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    val userModel = User(user.uid, user.displayName ?: "", user.email ?: "")
                    fireStore.collection("users").document(user.uid)
                        .set(userModel)
                        .addOnSuccessListener { onResult(true, null) }
                        .addOnFailureListener { onResult(false, it.message) }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun loginWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser!!.uid
                    fireStore.collection("users").document(uid).get()
                        .addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                onResult(true, null)
                            } else {
                                auth.signOut()
                                onResult(false, "Bu Google hesabı kayıtlı değil.")
                            }
                        }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
    fun getCurrentUser() = auth.currentUser
    fun logout() = auth.signOut()



}
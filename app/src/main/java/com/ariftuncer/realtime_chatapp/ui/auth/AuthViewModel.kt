package com.ariftuncer.realtime_chatapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariftuncer.realtime_chatapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()

    private val _authResult = MutableLiveData<Pair<Boolean, String?>>()
    val authResult : LiveData<Pair<Boolean, String?>> = _authResult

    fun register(name : String, email : String, password : String){

        repo.registerWithEmailAndPass(name,email,password) { success,message ->
            _authResult.value = Pair(success,message)
        }

    }
    fun login(email : String, password : String){

        repo.loginWithEmailAndPass(email,password) { success,message ->
            _authResult.value = Pair(success,message)
        }

    }

    fun registerGoogle(idToken : String){
        repo.registerWithGoogle(idToken) { success, message ->
            _authResult.value = Pair(success, message)
        }
    }
    fun loginGoogle(idToken : String){
        repo.loginWithGoogle (idToken) { success, message ->
            _authResult.value = Pair(success, message)
        }
    }
    fun logout(){
        repo.logout()
    }
    fun getCurrentUser() : FirebaseUser? {
        return repo.getCurrentUser()
    }

}
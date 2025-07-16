package com.ariftuncer.realtime_chatapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ariftuncer.realtime_chatapp.R
import com.ariftuncer.realtime_chatapp.databinding.ActivityLoginBinding
import com.ariftuncer.realtime_chatapp.ui.main.MainActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val authViewModel : AuthViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

         googleSignInClient = GoogleSignIn.getClient(this, gso)

        authViewModel.authResult.observe(this) { result ->
            val success = result.first
            val message = result.second
            if (success){
                val intent = Intent(this, MainActivity :: class.java)
                startActivity(intent)

            }
            else{
                Snackbar.make(view,"Başarısız giriş: $message", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.notRegisteredTxt.setOnClickListener { l ->
            val intent = Intent(this, RegisterActivity :: class.java)
            startActivity(intent)
        }
    }
    private val RC_GOOGLE_SIGN_IN = 100

    fun login(view: View) {
        if (view.id == binding.loginBtn.id) {
            val email = binding.emailEditTxt.text.toString()
            val password = binding.passEditTxt.text.toString()
            authViewModel.login(email, password)
        } else if (view.id == binding.LoginWithGoogleBtn.id) {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    authViewModel.loginGoogle(idToken)
                } else {
                    Snackbar.make(binding.root, "Google ID Token alınamadı", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Snackbar.make(binding.root, "Google Giriş Hatası: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
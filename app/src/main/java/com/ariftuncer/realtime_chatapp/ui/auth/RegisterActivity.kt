package com.ariftuncer.realtime_chatapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ariftuncer.realtime_chatapp.databinding.ActivityRegisterBinding
import com.ariftuncer.realtime_chatapp.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val authViewModel: AuthViewModel by viewModels()
    private val RC_GOOGLE_SIGN_IN = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.ariftuncer.realtime_chatapp.R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        authViewModel.authResult.observe(this) { (success, message) ->
            if (success) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Snackbar.make(binding.root, "Kayıt başarısız: $message", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun register(view: View) {
        if (view.id == binding.registerBtn.id) {
            val name = "${binding.nameEditTxt.text.toString()} ${binding.surnameEditTxt.text.toString()}"
            val email = binding.emailEditTxt.text.toString().trim()
            val password = binding.passEditTxt.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.length >= 6) {
                authViewModel.register(name, email, password)
            } else {
                Snackbar.make(binding.root, "Lütfen tüm alanları doldurun (şifre min 6 karakter)", Snackbar.LENGTH_SHORT).show()
            }
        } else if (view.id == binding.registerWithGoogleBtn.id) {
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
                    authViewModel.registerGoogle(idToken)
                } else {
                    Snackbar.make(binding.root, "Google ID Token alınamadı", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Snackbar.make(binding.root, "Google Kayıt Hatası: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}

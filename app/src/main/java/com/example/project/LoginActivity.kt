package com.example.project

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import com.example.project.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var facebookCallbackManager:CallbackManager
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                var task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    startMain()
                }catch(e:ApiException) {
                    Log.d("result","ApiException: $e")
                }
            }
        }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null){
            Log.d("guser","displayname: ${account.displayName}")
            Log.d("guser","givename: ${account.givenName}")
            Log.d("guser","email: ${account.email}")
            Log.d("guser","id: ${account.id}")
            Log.d("guser","photouri: ${account.photoUrl}")
            mGoogleSignInClient.signOut()
            startMain()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val googleSigninOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSigninOptions)
        facebookCallbackManager = CallbackManager.Factory.create()
        setContentView(binding.root)
        binding.signin.setOnClickListener { onSignin() }
        binding.googleSignin.setOnClickListener {onGoogleSignIn()}
        binding.facebookSignin.registerCallback(facebookCallbackManager, object: FacebookCallback<LoginResult>{
            override fun onCancel() {
                TODO("Not yet implemented")
                Log.d("result", "facebook response onCancel")
            }

            override fun onError(error: FacebookException) {
                TODO("Not yet implemented")
                Log.d("result", "facebook response onError")

            }

            override fun onSuccess(result: LoginResult) {
                Log.d("result", "facebook response on sucessLogin")
                startMain()
            }

        })
    }

    private fun onSignin() {
        val email = binding.email.text
        if (!email.isBlank() && Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()) {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            this.finish()
        } else {
            binding.email.setError("Enter valid email")
        }
    }

    private fun onGoogleSignIn() {
        val intent = mGoogleSignInClient.signInIntent
        getResult.launch(intent)
    }

    private fun startMain(){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }
}
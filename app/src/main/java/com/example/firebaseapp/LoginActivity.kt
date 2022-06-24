package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.databinding.ActivityLoginBinding
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

open class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //variaveis globais
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val mSignIn = 123
    private var mAuth: FirebaseAuth? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createRequest()

        setSupportActionBar(binding.toolbarLogin)

        auth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance()

        callbackManager = CallbackManager.Factory.create()
        loginButton = findViewById<LoginButton>(R.id.login_button)

        binding.buttonSignInGoogle.setOnClickListener {
            sigInWithGoogle()
            val intent = Intent(applicationContext, Profile::class.java)
            startActivity(intent)
        }

        binding.buttonSignIn.setOnClickListener {
            singIn()
        }

        binding.loginButton.setOnClickListener {
            facebookInit()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    //Login com o facebook
    private fun facebookInit() {
        FacebookSdk.sdkInitialize(this)
        loginButton.setReadPermissions("email", "public_profile", "user_friends")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }

    //Login simples
    private fun singIn() {
        if (validateFieldsLogin()) {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, "Digite email e senha", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun validateFieldsLogin(): Boolean {
        return (binding.editTextEmailLogin.text.isNotEmpty() && binding.editTextPasswordLogin.text.isNotEmpty())
    }

    //Login com a google
    private fun createRequest() {
        //configurando o google signIn
        val googleSignIn = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignIn)
    }

    private fun sigInWithGoogle() {
        val signIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signIntent, mSignIn)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                } else {
                    Toast.makeText(this, "Sorry, auth failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val TAG = "FacebookLogin"
    }
}
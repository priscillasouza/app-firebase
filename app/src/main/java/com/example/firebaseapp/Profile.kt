package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.databinding.ActivityProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarProfile)

        getLoginGoogle()
        getRegisterUser()
    }

    private fun getLoginGoogle() {
        val name = binding.textViewName
        val email = binding.textViewEmail

        val acct = GoogleSignIn.getLastSignedInAccount(this)

        if(acct != null) {
            name.text = acct.displayName
            email.text = acct.email

            binding.buttonLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getRegisterUser() {
        val name = intent.getStringExtra("EXTRA_NAME")
        val city = intent.getStringExtra("EXTRA_CITY")
        val country = intent.getStringExtra("EXTRA_COUNTRY")

        val userDescription =
            "$name, mora em $city, que fica no(a) $country"
        binding.textViewDescriptionUser.text = userDescription

        binding.buttonLogout.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
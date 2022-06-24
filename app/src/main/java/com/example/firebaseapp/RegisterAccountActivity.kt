package com.example.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.firebaseapp.databinding.ActivityRegisterAccountBinding

class RegisterAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSignUp)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.buttonSignUp.setOnClickListener {
            singUp()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun singUp() {
        if (validateFieldsSignUp()) {
            val name = binding.editTextNameSignUp.text.toString()
            val city = binding.editTextCitySignUp.text.toString()
            val country = binding.editTextCountrySignUp.text.toString()

            val intent = Intent(this, Profile::class.java)
            intent.putExtra("EXTRA_NAME", name)
            intent.putExtra("EXTRA_CITY", city)
            intent.putExtra("EXTRA_COUNTRY", country)
            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, "Digite todos os campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateFieldsSignUp(): Boolean {
        return (binding.editTextNameSignUp.text.isNotEmpty() && binding.editTextEmailSignUp.text.isNotEmpty()
                && binding.editTextPasswordSignUp.text.isNotEmpty() && binding.editTextCitySignUp.text.isNotEmpty()
                && binding.editTextCountrySignUp.text.isNotEmpty())
    }
}
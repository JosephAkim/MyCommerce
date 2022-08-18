package com.kim.mycommerce.activity

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.kim.mycommerce.MainActivity
import com.kim.mycommerce.R
import com.kim.mycommerce.databinding.ActivityOtpactivityBinding

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)

        binding.button3.setOnClickListener {
            if (binding.userOTP.text!!.isEmpty())
                Toast.makeText(this, "Please provide OTP", Toast.LENGTH_SHORT).show()
            else
                verifyUser(binding.userOTP.text.toString())
        }

        setContentView(binding.root)
    }

    private fun verifyUser(otp: String) {
        val credential = PhoneAuthProvider.getCredential(
            intent.getStringExtra("verification Id")!!,otp)

        signInWIthPhoneAuthCredential(credential)
    }

    private fun signInWIthPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){

                    val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preferences.edit()

                    editor.putString("number", intent.getStringExtra("number")!!)
                    editor.apply()

                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else {
                    Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()

                    }
                }
            }
    }
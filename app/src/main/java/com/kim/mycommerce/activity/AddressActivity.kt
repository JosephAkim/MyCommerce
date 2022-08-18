package com.kim.mycommerce.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kim.mycommerce.databinding.ActivityAddressBinding

class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var totalCost: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        preferences = this.getSharedPreferences("user", MODE_PRIVATE)

        totalCost =  intent.getStringExtra("totalCost")!!
        loadUserInfo()

        binding.proceed.setOnClickListener {
            validateData(
                binding.userName.text.toString(),
                binding.userNumber.text.toString(),
                binding.userState.text.toString(),
                binding.village.text.toString(),
                binding.userCity.text.toString(),
                binding.userPin.text.toString(),

            )
        }

        setContentView(binding.root)
    }

    private fun validateData(
        name: String,
        number: String,
        state: String,
        village: String,
        city: String,
        pinCode: String
    ) {
        if (number.isEmpty() || name.isEmpty() || state.isEmpty())
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        else
            storeData(pinCode, city, state, village)
    }

    private fun storeData(pinCode: String, city: String, state: String, village: String) {
        val map = hashMapOf<String, Any>()
        map["village"] = village
        map["city"] = city
        map["state"] = state
        map["pinCode"] = pinCode

        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .update(map).addOnSuccessListener {
                val b = Bundle()
                b.putStringArrayList("productIds", intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost", totalCost)
                val intent = Intent(this, CheckOutActivity::class.java)

                intent.putExtras(b)

                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {


        Firebase.firestore.collection("user")
            .document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userPhoneNumber"))
                binding.village.setText(it.getString("village"))
                binding.userCity.setText(it.getString("city"))
                binding.userState.setText(it.getString("state"))
                binding.userPin.setText(it.getString("pinCode"))
            }
            .addOnFailureListener {

            }
    }
}
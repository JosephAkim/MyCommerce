package com.kim.mycommerce.activity

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kim.mycommerce.MainActivity
import com.kim.mycommerce.R
import com.kim.mycommerce.roomdb.AppDatabase
import com.kim.mycommerce.roomdb.ProductModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckOutActivity : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        val checkout = Checkout()
        checkout.setKeyID("<YOUR_KEY_ID>")

        val price = intent.getStringExtra("totalCost")
        try {
            val options = JSONObject()
            options.put("name", "PKart")
            options.put("description", "Ecommerce app")
            options.put("image", "Merchant Name")
            options.put("theme.color", "#FFBB86FC")
            options.put("currency", "INR")
            options.put("amount", (price!!.toInt()*100))
            options.put("prefill.email", "example@email.com")
            options.put("prefill.contact", "1234567890")
            checkout.open(this, options)
        }catch (e: Exception){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(p0 : String?){
        Toast.makeText(this,"Payment success", Toast.LENGTH_SHORT).show()

        uploadData()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
    }

    private fun uploadData() {
        val id =  intent.getStringArrayListExtra("productIds")
        for (currentId in id!!){
            fetchData(currentId)
        }
    }

    private fun saveData(name: String?, price: String?, productId: String) {
        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()
        data["name"] = name!!
        data["price"] = price!!
        data["productId"] = productId
        data["status"] = "Ordered"
        data["userId"] = preferences.getString("number", "")!!

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.document(key).set(data).addOnSuccessListener {

        }.addOnFailureListener {
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            Toast.makeText(this, "Order placed", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun fetchData(productId: String?) {

        val dao = AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener {

                lifecycleScope.launch(Dispatchers.IO){
                    dao.deleteProduct(ProductModel(productId))
                }
                saveData(it.getString("productName"),
                    it.getString("productSp"),
                    productId)
            }
    }
}
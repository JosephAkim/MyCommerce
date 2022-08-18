package com.kim.mycommerce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kim.mycommerce.activity.ProductDetailsActivity
import com.kim.mycommerce.databinding.LayoutCartItemBinding
import com.kim.mycommerce.roomdb.AppDatabase
import com.kim.mycommerce.roomdb.ProductModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CartAdapter(val context: Context,val list: List<ProductModel>) :
RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    inner class CartViewHolder(val binding: LayoutCartItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = LayoutCartItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Glide.with(context).load(list[position].productImages).into(holder.binding.imageView4)

        holder.binding.textView7.text = list[position].productName
        holder.binding.textView12.text = list[position].productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }

        val dao = AppDatabase.getInstance(context).productDao()
        holder.binding.imageView5.setOnClickListener {
            GlobalScope.launch {
                dao.deleteProduct(
                    ProductModel(
                        list[position].productId,
                        list[position].productName),
                        list[position].productImages,
                        list[position].productSp )
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}
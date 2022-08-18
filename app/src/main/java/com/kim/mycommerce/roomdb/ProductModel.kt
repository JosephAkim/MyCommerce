package com.kim.mycommerce.roomdb

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Products")
data class ProductModel(
    @PrimaryKey
    @NonNull
    val productId : String,
    @ColumnInfo(name = "productName")
    val productName : String? = "",
    @ColumnInfo(name = "productImage")
    val productImages : String? = "",
    @ColumnInfo(name = "productSp")
    val productSp : String? = "",
)

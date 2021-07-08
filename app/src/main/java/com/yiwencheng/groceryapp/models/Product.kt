package com.yiwencheng.groceryapp.models

import java.io.Serializable

data class Product(
    val `data`: ProductData,
    val error: Boolean
)

data class ProductData(
//    val __v: Int,
    val _id: String,
//    val catId: Int,
//    val created: String,
//    val description: String,
    val image: String,
    val mrp: Float,
//    val position: Int,
    val price: Float,
    val productName: String,
//    val quantity: Int,
//    val status: Boolean,
//    val subId: Int,
    val unit: String,
    var ordered_quantity: Int
):Serializable{
    companion object{
        const val KEY_PRODUCT = "productData"
    }
}
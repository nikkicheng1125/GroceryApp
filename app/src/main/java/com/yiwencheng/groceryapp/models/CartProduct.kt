package com.yiwencheng.groceryapp.models

data class CartProduct(
    var _id:String,
    var image: String,
    var mrp: Float,
    var price: Float,
    var productName: String,
    var quantity: Int,
)

package com.yiwencheng.groceryapp.models

data class History(
    val date: String,
    val orderSummary: OrderSummary,
    val payment: Payment,
    val products: List<OrderedProduct>,
    val shippingAddress: ShippingAddress,
    val user: User
)

data class OrderSummary(
    val _id: String,
    val deliveryCharges: Int,
    val discount: Int,
    val orderAmount: Int,
    val ourPrice: Int,
    val totalAmount: Int
)

data class Payment(
    val _id: String,
    val paymentMode: String,
    val paymentStatus: String
)

data class OrderedProduct(
    val _id: String,
    val image: String,
    val mrp: Int,
    val price: Int,
    val quantity: Int
)

data class ShippingAddress(
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String
)

data class User(
    val _id: String,
    val email: String,
    val mobile: String,
    val name: String
)
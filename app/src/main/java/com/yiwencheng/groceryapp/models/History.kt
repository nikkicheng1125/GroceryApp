package com.yiwencheng.groceryapp.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class History(
    val date: String,
    val orderSummary: OrderSummary,
    val payment: Payment,
    val products: ArrayList<OrderedProduct>,
    val shippingAddress: ShippingAddress,
    val user: User
) : Serializable{
    companion object{
        const val KEY_HISTORY = "history"
    }
}


data class OrderSummary(
    val _id: String,
    val deliveryCharges: Int,
    val discount: Int,
    val orderAmount: Int,
    val ourPrice: Int,
    val totalAmount: Int
) : Serializable {
    companion object {
        const val KEY_ORDER_SUMMARY = "orderSummary"
    }
}

data class Payment(
    val _id: String,
    val paymentMode: String,
    val paymentStatus: String
) : Serializable {
    companion object {
        const val KEY_PAYMENT = "payment"
    }
}

data class OrderedProduct(
    val _id: String,
    val image: String,
    val mrp: Float,
    val price: Float,
    val quantity: Int
) : Serializable {
    companion object {
        const val KEY_ORDERED_PRODUCT = "orderedProduct"
    }
}

data class ShippingAddress(
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String
) : Serializable {
    companion object {
        const val KEY_SHIPPING_ADDRESS = "shippingAddress"
    }
}

data class User(
    val _id: String,
    val email: String,
    val mobile: String,
    val name: String
) : Serializable {
    companion object {
        const val KEY_USER = "user"
    }
}
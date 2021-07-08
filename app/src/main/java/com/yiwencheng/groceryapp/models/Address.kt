package com.yiwencheng.groceryapp.models

import java.io.Serializable

data class AddressResponse(
    val count: Int,
//    val address: List<Address>,
    val address: Address,
    val error: Boolean
)

data class Address(
//    val __v: Int,
    var _id: String,
    var city: String,
    var houseNo: String,
    var pincode: Int,
    var streetName: String,
    var type: String,
    var userId: String,
    var name: String,
    var email:String
):Serializable{
    companion object{
        const val KEY_ADDRESS = "address"
    }
}
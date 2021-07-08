package com.yiwencheng.groceryapp.models

data class SubCategory(
    val count: Int,
    val `data`: List<SubCatData>,
    val error: Boolean
)

data class SubCatData(
//    val __v: Int,
//    val _id: String,
    val catId: Int,
//    val position: Int,
//    val status: Boolean,
//    val subDescription: String,
    val subId: Int,
//    val subImage: String,
    val subName: String
)
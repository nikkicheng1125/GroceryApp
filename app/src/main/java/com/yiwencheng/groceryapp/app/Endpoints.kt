package com.yiwencheng.groceryapp.app

class Endpoints {
    companion object {
        const val URL_REGISTER = "auth/register"
        const val URL_LOGIN = "auth/login"
        const val URL_CATEGORY = "category"
        const val URL_SUB_CATEGORY = "subcategory"
        const val URL_PRODUCT = "products"
        const val URL_ADDRESS = "address"
        const val URL_ORDER = "orders"
        const val URL_USER = "users"


        fun getRegisterUrl(): String {
            return "${Config.BASE_URL + URL_REGISTER}"
        }

        fun getLoginUrl(): String {
            return "${Config.BASE_URL + URL_LOGIN}"
        }

        fun getCategoryUrl(): String {
            return "${Config.BASE_URL + URL_CATEGORY}"
        }

        fun getSubCategoryByCatId(catId: Int): String {
            return Config.BASE_URL + URL_SUB_CATEGORY + "/" + catId.toString()
        }

        fun getProductUrlBySubId(subId: Int): String {
            return Config.BASE_URL + URL_PRODUCT + "/sub/" + subId.toString()
        }

        fun getAddressUrlByUserId(userId: String): String {
            return Config.BASE_URL + URL_ADDRESS + "/" + userId
        }

        fun getAddressUrl(): String {
            return Config.BASE_URL + URL_ADDRESS
        }

        fun getAddressUrlById(_id: String): String {
            return Config.BASE_URL + URL_ADDRESS + "/" + _id
        }

        fun getOrderUrl(): String {
            return Config.BASE_URL + URL_ORDER
        }

        fun getUserUrlById(_id: String): String {
            return Config.BASE_URL + URL_USER + "/" + _id
        }

        fun getOrderUrlById(_id: String): String {
            return Config.BASE_URL + URL_ORDER + "/" + _id
        }
    }
}


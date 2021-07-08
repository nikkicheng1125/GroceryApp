package com.yiwencheng.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.DBHelper
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.Address
import com.yiwencheng.groceryapp.models.Address.Companion.KEY_ADDRESS
import com.yiwencheng.groceryapp.models.CartProduct
import kotlinx.android.synthetic.main.activity_order_confirm.*
import org.json.JSONArray
import org.json.JSONObject

class OrderConfirmActivity : AppCompatActivity() {

    private var mycartList: ArrayList<CartProduct> = ArrayList()

    var dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)

        button_home.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        init()
    }

    private fun init() {
        var sessionManager = SessionManager(this)
        var userId = sessionManager.getUserId()

        var jsonObject_summary = getSummary()
        var jsonObject_user = getUser()
        var jsonObject_address = getAddress()
        var jsonObject_payment = getPayment()
        var jsonArray_products = getProducts()

        var jsonObject_info = JSONObject()
        jsonObject_info.put("userId",userId)
        jsonObject_info.put("orderSummary",jsonObject_summary)
        jsonObject_info.put("user",jsonObject_user)
        jsonObject_info.put("shippingAddress",jsonObject_address)
        jsonObject_info.put("payment",jsonObject_payment)
        jsonObject_info.put("products",jsonArray_products)

        var requestQueue = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.getOrderUrl(),
            jsonObject_info,
            Response.Listener {
                Log.d("confirm",it.toString())
            },
            Response.ErrorListener {
                Log.e("abc", it.message.toString())
            }
        )
        requestQueue.add(request)

    }

    private fun getSummary():JSONObject {
        var jsonObject_summary = JSONObject()

        mycartList = dbHelper.readProduct()
        var totalmrp: Float = 0F
        var totalprice: Float = 0F
        var deliverycharge: Float = 0F

        for (i in 0 until mycartList.size) {
            var each_quantity = mycartList[i].quantity
            var each_mrp = mycartList[i].mrp
            var each_price = mycartList[i].price
            totalmrp += each_quantity * each_mrp
            totalprice += each_quantity * each_price
        }

        deliverycharge = if (totalprice<100){
            10F
        }else{
            0F
        }

        jsonObject_summary.put("totalAmount",totalmrp)
        jsonObject_summary.put("ourPrice",totalprice)
        jsonObject_summary.put("discount",totalmrp-totalprice)
        jsonObject_summary.put("deliveryCharges",deliverycharge)
        jsonObject_summary.put("orderAmount",totalmrp)

        return jsonObject_summary
    }

    private fun getUser():JSONObject{
        var sessionManager = SessionManager(this)
        var jsonObject_user = JSONObject()

        var name = sessionManager.getName()
        var email = sessionManager.getEmail()
        var mobile = sessionManager.getMobile()

        jsonObject_user.put("email",email)
        jsonObject_user.put("mobile",mobile)
        jsonObject_user.put("name",name)

        return jsonObject_user
    }

    private fun getAddress():JSONObject{
        var jsonObject_address = JSONObject()
        var address = intent.getSerializableExtra(KEY_ADDRESS) as Address

        jsonObject_address.put("pincode",address.pincode)
        jsonObject_address.put("houseNo",address.houseNo)
        jsonObject_address.put("streetName",address.streetName)
        jsonObject_address.put("city",address.city)

        return jsonObject_address
    }

    private fun getPayment():JSONObject{
        var jsonObject_payment = JSONObject()
        var payment_mode = intent.getStringExtra("PAYMENT_MODE")
        var payment_status = intent.getStringExtra("PAYMENT_STATUS")

        jsonObject_payment.put("paymentMode",payment_mode)
        jsonObject_payment.put("paymentStatus",payment_status)
        return jsonObject_payment
    }

    private fun getProducts():JSONArray{
        var jsonArray_products = JSONArray()
        var myCartList= dbHelper.readProduct()
        for (item in myCartList){
            var jsonObject = JSONObject()

            jsonObject.put("_id",item._id)
            jsonObject.put("mrp",item.mrp)
            jsonObject.put("price",item.price)
            jsonObject.put("quantity",item.quantity)
            jsonObject.put("image",item.image)

            jsonArray_products.put(jsonObject)
        }
        return jsonArray_products
    }


}
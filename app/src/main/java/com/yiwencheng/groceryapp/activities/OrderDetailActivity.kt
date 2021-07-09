package com.yiwencheng.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.adapters.OrderAdapter
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.*
import com.yiwencheng.groceryapp.models.History.Companion.KEY_HISTORY
import com.yiwencheng.groceryapp.models.OrderSummary.Companion.KEY_ORDER_SUMMARY
import com.yiwencheng.groceryapp.models.OrderedProduct.Companion.KEY_ORDERED_PRODUCT
import com.yiwencheng.groceryapp.models.Payment.Companion.KEY_PAYMENT

import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.app_bar.*

class OrderDetailActivity : AppCompatActivity() {

    var productList: ArrayList<CartProduct> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        var mtoolbar = my_toolbar
        mtoolbar.title = "Order Detail"

        setSupportActionBar(mtoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
    }

    private fun init() {
        getProductsInfo()
        var history = intent.getSerializableExtra(KEY_HISTORY) as History
        text_view_date.text = "Order Date: ${history.date.subSequence(0,10)}"
        text_view_order_number.text = "Order Number: ${history.orderSummary._id}"
        text_view_status.text = "Order Status: ${history.payment.paymentStatus}"
        text_view_pay_method.text = "Payment Method: ${history.payment.paymentMode}"
        text_view_name.text = "${history.user.name}"
        text_view_email.text = "${history.user.email}"
        text_view_street1.text = "${history.shippingAddress.streetName}"
        text_view_street2.text = "${history.shippingAddress.houseNo}"
        text_view_city.text = "${history.shippingAddress.city}"
        if(history.shippingAddress.pincode.toString().length==4){
            text_view_zipcode.text = "0${history.shippingAddress.pincode}"
        }else {
            text_view_zipcode.text = "${history.shippingAddress.pincode}"
        }
        text_view_subtotal.text = "Subtotal: $${history.orderSummary.totalAmount}"
        text_view_discount.text = "Discount: -$${history.orderSummary.discount}"
        text_view_shipping.text = "Shipping: $${history.orderSummary.deliveryCharges}"
        text_view_payment.text = "Total Amount: $${history.orderSummary.ourPrice}"
    }

    private fun getProductsInfo() {
        var history = intent.getSerializableExtra(KEY_HISTORY) as History
        var orderProduct = history.products

        for (item in orderProduct) {
            var requestQueue = Volley.newRequestQueue(this)
            var request = JsonObjectRequest(
                Request.Method.GET,
                Endpoints.getProductUrlById(item._id),
                null,
                Response.Listener {
                    var jsonObject = it.getJSONObject("data")
                    var productName = jsonObject.getString("productName")
                    var image = item.image
                    var mrp = item.mrp
                    var price = item.price
                    var quantity = item.quantity
                    var _id = item._id
                    var cartProduct = CartProduct(_id,image,mrp,price,productName,quantity)
                    productList.add(cartProduct)
                    var adapterDetail = OrderAdapter(this, productList)
                    recycler_view_order.adapter = adapterDetail
                    recycler_view_order.layoutManager = LinearLayoutManager(this)

                },
                Response.ErrorListener {
                    Log.e("abc", it.message.toString())
                }
            )
            requestQueue.add(request)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return true
    }
}
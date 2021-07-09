package com.yiwencheng.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.adapters.OrderAdapter
import com.yiwencheng.groceryapp.helpers.DBHelper
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.Address
import com.yiwencheng.groceryapp.models.Address.Companion.KEY_ADDRESS
import com.yiwencheng.groceryapp.models.CartProduct
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.app_bar.*

class PaymentActivity : AppCompatActivity() {

    private var orderedProducts: ArrayList<CartProduct> = ArrayList()
    var dbHelper = DBHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        init()

        var toolbar = my_toolbar
        toolbar.title = "Payment"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun init() {
        orderedProducts = dbHelper.readProduct()
        var totalmrp: Float = 0F
        var totalprice: Float = 0F
        var shipping: Float = 0F

        var adapterCart = OrderAdapter(this, orderedProducts)
        recycler_view_order.adapter = adapterCart
        recycler_view_order.layoutManager = LinearLayoutManager(this)

        var address = intent.getSerializableExtra(KEY_ADDRESS) as Address

        for (i in 0 until  orderedProducts .size) {
            var each_quantity =  orderedProducts [i].quantity
            var each_mrp =  orderedProducts [i].mrp
            var each_price =  orderedProducts [i].price
            totalmrp += each_quantity * each_mrp
            totalprice += each_quantity * each_price
        }

        if(totalprice>=100){
            shipping=0F
        }else{
            shipping = 10F
        }

        text_view_name.text = address.name
        text_view_street1.text = address.streetName
        text_view_street2.text = address.houseNo
        text_view_city.text = address.city
        if(address.pincode.toString().length ==4){
            text_view_zipcode.text = "0"+address.pincode.toString()
        }else{
            text_view_zipcode.text = address.pincode.toString()
        }

        text_view_subtotal.text = "Subtotal: $${totalmrp}"
        text_view_discount.text = "Discount: -$${totalmrp - totalprice}"
        text_view_shipping.text = "Shipping: $${shipping}"
        text_view_payment_due.text = "Total Payment Due: $${totalprice+shipping}"

        button_cash.setOnClickListener {
            var intent = Intent(this, OrderConfirmActivity::class.java)
            intent.putExtra("PAYMENT_MODE", "cash")
            intent.putExtra("PAYMENT_STATUS", "completed")
            intent.putExtra(KEY_ADDRESS, address)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_cart_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_home -> startActivity(Intent(this,CategoryActivity::class.java))
            R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
        }
        return true
    }


}
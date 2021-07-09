package com.yiwencheng.groceryapp.activities

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.adapters.CartAdapter
import com.yiwencheng.groceryapp.helpers.DBHelper
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.CartProduct
import com.yiwencheng.groceryapp.models.Data
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.row_adapter_cart.*
import kotlinx.android.synthetic.main.row_adapter_cart.view.*

class CartActivity : AppCompatActivity(), CartAdapter.OnCartAdapterListener {

    private var mycartList: ArrayList<CartProduct> = ArrayList()
    var dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        var toolbar = my_toolbar
        toolbar.title = "Shopping Cart"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()

        button_shopping.setOnClickListener {
           startActivity(Intent(this,CategoryActivity::class.java))
        }
    }

    private fun init() {
        var totalmrp: Float = 0F
        var totalprice: Float = 0F

        mycartList = dbHelper.readProduct()

        var adapterCart = CartAdapter(this, mycartList)
        adapterCart.setOnCartAdapterListener(this)
        recycler_view_cart.adapter = adapterCart
        recycler_view_cart.layoutManager = LinearLayoutManager(this)

        for (i in 0 until mycartList.size) {
            var each_quantity = mycartList[i].quantity
            var each_mrp = mycartList[i].mrp
            var each_price = mycartList[i].price
            totalmrp += each_quantity * each_mrp
            totalprice += each_quantity * each_price
        }

        if (totalmrp == 0F){
            text_view_total.text = null
            text_view_total_mrp.text = null
            text_view_discount.text = null
            text_view_discount_price.text = null
            text_view_total_pay.text = null
            text_view_pay.text = null
            button_checkout.visibility = View.GONE
            text_view_no_item.visibility = View.VISIBLE
        }else{
            text_view_total.text = "Subtotal:"
            text_view_discount.text = "Discount:"
            text_view_pay.text = "Total:"
            text_view_total_mrp.text = "S"+totalmrp.toString()
            text_view_total_pay.text = "S"+totalprice.toString()
            text_view_discount_price.text = "-$"+(totalmrp - totalprice).toString()
            button_checkout.visibility = View.VISIBLE
            text_view_no_item.visibility = View.GONE
        }

        button_checkout.setOnClickListener {
            startActivity(Intent(this,AddressActivity::class.java))
        }
    }

    override fun onClickDelete(cartProduct: CartProduct) {
        dbHelper.deleteProduct(cartProduct._id)
        init()
    }

    override fun onClickMinus(cartProduct: CartProduct) {
        if (cartProduct.quantity > 1) {
            cartProduct.quantity -= 1
            text_view_quantity.text = cartProduct.quantity.toString()
            var newCartItem = CartProduct(
                cartProduct._id,
                cartProduct.image,
                cartProduct.mrp,
                cartProduct.price,
                cartProduct.productName,
                cartProduct.quantity
            )
            dbHelper.updateCart(newCartItem)
            init()
        } else {
            dbHelper.deleteProduct(cartProduct._id)
            init()
        }

    }

    override fun onClickPlus(cartProduct: CartProduct) {
        cartProduct.quantity += 1
        var newCartItem = CartProduct(
            cartProduct._id,
            cartProduct.image,
            cartProduct.mrp,
            cartProduct.price,
            cartProduct.productName,
            cartProduct.quantity
        )
        dbHelper.updateCart(newCartItem)
        text_view_quantity.text = newCartItem.quantity.toString()

        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.no_cart_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
            R.id.menu_home -> startActivity(Intent(this,CategoryActivity::class.java))
        }
        return true
    }
}
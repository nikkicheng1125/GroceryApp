package com.yiwencheng.groceryapp.activities

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.helpers.DBHelper
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.CartProduct
import com.yiwencheng.groceryapp.models.ProductData
import com.yiwencheng.groceryapp.models.ProductData.Companion.KEY_PRODUCT
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.row_adapter_product.view.*

class ProductDetailActivity : AppCompatActivity() {

    var dbHelper = DBHelper(this)
    private var cartItems: ArrayList<CartProduct> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        var toolbar = my_toolbar
        toolbar.title = "Detail"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
    }

    private fun init(){
        var product = intent.getSerializableExtra(KEY_PRODUCT) as ProductData
        var url ="https://rjtmobile.com/grocery/images/"+ product.image
        Picasso.get().load(url).placeholder(R.drawable.noimage).error(R.drawable.noimage).into(image_view_detail)
        text_view_product_name.text = product.productName
        text_view_mrp.text = "$"+product.mrp.toString()
        text_view_mrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        text_view_price.text ="$"+ product.price.toString()
        text_view_unit.text = "Quantity: "+ product.unit
        if(product.ordered_quantity==0){
            text_view_quantity.text = ""
        }else {
            text_view_quantity.text = product.ordered_quantity.toString()
        }
        cartItems = dbHelper.readProduct()
        var idlist: ArrayList<String> = ArrayList()
        var quantitylist:ArrayList<Int> = ArrayList()

        for (i in 0 until cartItems.size) {
            idlist.add(cartItems[i]._id)
            quantitylist.add(cartItems[i].quantity)
        }

        if (product._id in idlist) {
            var index = idlist.indexOf(product._id)
            product.ordered_quantity = quantitylist[index]
            button_minus.visibility = View.VISIBLE
            button_plus.visibility = View.VISIBLE
            button_tocart.visibility = View.GONE
            text_view_quantity.text = quantitylist[index].toString()
        } else{
            button_minus.visibility = View.GONE
            button_plus.visibility = View.GONE
            button_tocart.visibility = View.VISIBLE
            product.ordered_quantity = 0
        }

        button_tocart.setOnClickListener {
            if(product.ordered_quantity>=1){
                button_tocart.visibility = View.GONE
            }else if(product.ordered_quantity == 0){
                button_tocart.visibility = View.VISIBLE
                product.ordered_quantity += 1
                var newCartItem = CartProduct(
                    product._id,
                    product.image,
                    product.mrp,
                    product.price,
                    product.productName,
                    product.ordered_quantity
                )
                dbHelper.addProduct(newCartItem)
                button_tocart.visibility = View.GONE
                button_minus.visibility = View.VISIBLE
                button_plus.visibility = View.VISIBLE
                text_view_quantity.text = "1"
            }
        }

        button_minus.setOnClickListener {
            if (product.ordered_quantity > 1) {
                product.ordered_quantity -= 1
               text_view_quantity.text = product.ordered_quantity.toString()
                var newCartItem = CartProduct(
                    product._id,
                    product.image,
                    product.mrp,
                    product.price,
                    product.productName,
                    product.ordered_quantity
                )
                dbHelper.updateCart(newCartItem)
            } else if (product.ordered_quantity == 1) {
                product.ordered_quantity = 0
                text_view_quantity.text = ""
                button_tocart.visibility = View.VISIBLE
                button_minus.visibility = View.GONE
                button_plus.visibility = View.GONE
                dbHelper.deleteProduct(product._id)
            } else {
                product.ordered_quantity = 0
                button_tocart.visibility = View.VISIBLE
                button_minus.visibility = View.GONE
                button_plus.visibility = View.GONE
                text_view_quantity.text = ""
            }

        }

        button_plus.setOnClickListener {
            product.ordered_quantity+=1
            var newCartItem = CartProduct(
                product._id,
                product.image,
                product.mrp,
                product.price,
                product.productName,
                product.ordered_quantity
            )

            if (product.ordered_quantity == 1) {
                dbHelper.addProduct(newCartItem)
            } else {
                dbHelper.updateCart(newCartItem)
            }
            text_view_quantity.text = newCartItem.quantity.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_cart_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
            R.id.menu_home -> startActivity(Intent(this,CategoryActivity::class.java))
            R.id.menu_cart -> startActivity(Intent(this,CartActivity::class.java))
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        init()
    }

}
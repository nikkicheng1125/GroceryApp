package com.yiwencheng.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.adapters.AddressAdapter
import com.yiwencheng.groceryapp.adapters.HistoryAdapter
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.*
import kotlinx.android.synthetic.main.activity_orderhistory.*
import kotlinx.android.synthetic.main.app_bar.*

class OrderhistoryActivity : AppCompatActivity(),HistoryAdapter.OnHistoryAdapterListener {

    var myHistoryList: ArrayList<History> = ArrayList()
    var userId:String?=null
    var historyAdapter: HistoryAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderhistory)

        var toolbar = my_toolbar
        toolbar.title = "Order History"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
    }

    private fun init(){
        getHistory()
        historyAdapter = HistoryAdapter(this,myHistoryList)
        historyAdapter!!.setOnHistoryAdapterListener(this)

        recycler_view_order_history.adapter = historyAdapter
        recycler_view_order_history.layoutManager = LinearLayoutManager(this)


    }

    private fun getHistory(){
        var sessionManager = SessionManager(this)
        userId = sessionManager.getUserId()

        var date: String
        var orderSummary: OrderSummary
        var payment: Payment
        var products: List<OrderedProduct>
        var shippingAddress: ShippingAddress
        var user: User

        var requestQueue = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(
            Request.Method.GET,
            Endpoints.getOrderUrlById(userId!!),
            null,
            Response.Listener {
                var jsonArray = it.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    var jsonObject = jsonArray.getJSONObject(i)
                    var gson = Gson()
                    var history = gson.fromJson(jsonObject.toString(),History::class.java)

                    myHistoryList.add(history)
                    Log.d("History",history.toString())
                }

                historyAdapter?.setData(myHistoryList)
            },
            Response.ErrorListener {
                Log.e("history", it.message.toString())
            }
        )
        requestQueue.add(request)
    }

    override fun onItemClick(history: History) {
        TODO("Not yet implemented")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_cart_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var sessionManager = SessionManager(this)
        when(item.itemId){
            android.R.id.home-> finish()
            R.id.menu_home -> startActivity(Intent(this,CategoryActivity::class.java))
            R.id.menu_cart -> startActivity(Intent(this,CartActivity::class.java))
            R.id.menu_logout -> {
                sessionManager.logout()
                startActivity(Intent(this,LoginActivity::class.java))
            }
            R.id.menu_address -> startActivity(Intent(this,AddressActivity::class.java))
            R.id.menu_setting -> Toast.makeText(applicationContext,"Settings", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}
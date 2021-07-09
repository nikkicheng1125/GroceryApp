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
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.adapters.AddressAdapter
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.Address
import com.yiwencheng.groceryapp.models.Address.Companion.KEY_ADDRESS
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.app_bar.*

class AddressActivity : AppCompatActivity(),AddressAdapter.OnAddressAdapterListener {

    var myAddressList:ArrayList<Address> = ArrayList()
    var addressAdapter:AddressAdapter?=null
    var userId:String?=null
    var totalprice = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        var mtoolbar = my_toolbar
        mtoolbar.title = "Address"

        setSupportActionBar(mtoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button_add_address.setOnClickListener {
            startActivity(Intent(this,AddAddressActivity::class.java))
        }

        init()
    }

    private fun init(){
        getAddress()
        addressAdapter = AddressAdapter(this,myAddressList)
        addressAdapter!!.setOnAddressAdapterListener(this)
        recycler_view_address.adapter = addressAdapter
        recycler_view_address.layoutManager = LinearLayoutManager(this)

    }

    private fun getAddress(){
        var sessionManager = SessionManager(this)
        myAddressList.clear()

        var name:String
        var email:String=""
        var city: String
        var houseNo: String
        var pincode: Int
        var streetName: String
        var type: String
        var _id:String
        userId = sessionManager.getUserId().toString()

        var requestQueue = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(
            Request.Method.GET,
            Endpoints.getAddressUrlByUserId(userId!!),
            null,
            Response.Listener {
                var jsonArray = it.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    var jsonObject = jsonArray.getJSONObject(i)
                    name = jsonObject.getString("name")
                    _id = jsonObject.getString("_id")
                    city = jsonObject.getString("city")
                    houseNo = jsonObject.getString("houseNo")
                    pincode = jsonObject.getInt("pincode")
                    streetName = jsonObject.getString("streetName")
                    type = jsonObject.getString("type")
                    var address = Address(_id, city,  houseNo, pincode, streetName, type,userId!!,name,email)
                    myAddressList.add(address)
                }
                addressAdapter?.setData(myAddressList)
            },
            Response.ErrorListener {
                Log.e("abc", it.message.toString())
            }
        )
        requestQueue.add(request)

    }

    override fun onAddressClick(address: Address) {
        var intent = Intent(this,PaymentActivity::class.java)
        intent.putExtra(KEY_ADDRESS,address)
        startActivity(intent)
    }

    override fun onClickDelete(address: Address) {
        var requestQueue = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(
            Request.Method.DELETE,
            Endpoints.getAddressUrlById(address._id),
            null,
            Response.Listener {
                Toast.makeText(applicationContext,"Deleted",Toast.LENGTH_SHORT).show()
                init()
            },
            Response.ErrorListener {
                Log.e("abc", it.message.toString())
            }
        )
        requestQueue.add(request)
    }

    override fun onClickEdit(address: Address) {
        var intent = Intent(this,AddAddressActivity::class.java)
        intent.putExtra("_ID",address._id)
        intent.putExtra("CODE",1)
        startActivity(intent)
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

}
package com.yiwencheng.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yiwencheng.groceryapp.LoginResponse
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.Address
import com.yiwencheng.groceryapp.models.Address.Companion.KEY_ADDRESS
import com.yiwencheng.groceryapp.models.AddressResponse
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONArray
import org.json.JSONObject

class AddAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        init()
        var mtoolbar = my_toolbar
        mtoolbar.title = "Address"

        setSupportActionBar(mtoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun init(){
        var sessionManager =  SessionManager(this)
        var code = intent.getIntExtra("CODE",0)

        button_save_address.setOnClickListener {
            if (code==0){
                var userId = sessionManager.getUserId()
                var type:String

                if(radio_home.isChecked){
                    type = "Home"
                }else if(radio_office.isChecked){
                    type = "Office"
                }else{
                    type = "Other"
                }

                var name = edit_text_name.text.toString()
                var email = edit_text_email.text.toString()
                var street1 = edit_text_street.text.toString()
                var street2 = edit_text_street2.text.toString()
                var city = edit_text_city.text.toString()
                var zipcode = edit_text_zipcode.text.toString().toInt()

                var jsonObject = JSONObject()
                jsonObject.put("city", city)
                jsonObject.put("houseNo",street2)
                jsonObject.put("pincode",zipcode)
                jsonObject.put("streetName",street1)
                jsonObject.put("type",type)
                jsonObject.put("userId",userId)
                jsonObject.put("name",name)

                var requestQueue = Volley.newRequestQueue(this)
                var request = JsonObjectRequest(
                    Request.Method.POST,
                    Endpoints.getAddressUrl(),
                    jsonObject,
                    Response.Listener {
                        Log.d("add",it.toString())
                    },
                    Response.ErrorListener {
                        Log.e("abc", it.message.toString())
                    }
                )
                requestQueue.add(request)
                Toast.makeText(applicationContext,"Address Saved",Toast.LENGTH_SHORT).show()
                finish()
                var intent =Intent(this,AddressActivity::class.java)
                startActivity(intent)
            }else{
                var sessionManager =  SessionManager(this)
                var userId = sessionManager.getUserId()
                var _id = intent.getStringExtra("_ID")

                var type:String
                if(radio_home.isChecked){
                    type = "Home"
                }else if(radio_office.isChecked){
                    type = "Office"
                }else{
                    type = "Other"
                }

                var name = edit_text_name.text.toString()
                var email = edit_text_email.text.toString()
                var street1 = edit_text_street.text.toString()
                var street2 = edit_text_street2.text.toString()
                var city = edit_text_city.text.toString()
                var zipcode = edit_text_zipcode.text.toString().toInt()

                var jsonObject = JSONObject()
                jsonObject.put("city", city)
                jsonObject.put("houseNo",street2)
                jsonObject.put("pincode",zipcode)
                jsonObject.put("streetName",street1)
                jsonObject.put("type",type)
                jsonObject.put("userId",userId)
                jsonObject.put("name",name)

                var requestQueue = Volley.newRequestQueue(this)
                var request = JsonObjectRequest(
                    Request.Method.PUT,
                    Endpoints.getAddressUrlById(_id!!),
                    jsonObject,
                    Response.Listener {
                        Log.d("edit",it.toString())
                    },
                    Response.ErrorListener {
                        Log.e("abc", it.message.toString())
                    }
                )
                requestQueue.add(request)
                Toast.makeText(applicationContext,"Address Saved",Toast.LENGTH_SHORT).show()
                finish()
                var intent =Intent(this,AddressActivity::class.java)
                startActivity(intent)

            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var sessionManager =  SessionManager(this)
        when(item.itemId){
            android.R.id.home-> finish()
            R.id.menu_cart -> startActivity(Intent(this,CartActivity::class.java))
            R.id.menu_logout -> {
                sessionManager.logout()
                startActivity(Intent(this,LoginActivity::class.java))
            }
            R.id.menu_address -> Toast.makeText(applicationContext,"Profile", Toast.LENGTH_SHORT).show()
            R.id.menu_setting -> Toast.makeText(applicationContext,"Settings",Toast.LENGTH_SHORT).show()
        }
        return true
    }
}
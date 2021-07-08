package com.yiwencheng.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yiwencheng.groceryapp.LoginResponse
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.User
import com.yiwencheng.groceryapp.app.Endpoints
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        button_login.setOnClickListener {
            var sessionManager = SessionManager(this)
            var input_email = edit_text_email.text.toString()
            var input_password = edit_text_password.text.toString()
            var token: String
            var user: User
            var remember = false

            if (checkbox_remember.isChecked){
                remember = true
            }

            var jsonObject = JSONObject()
            jsonObject.put("email", input_email)
            jsonObject.put("password", input_password)

            var requestQueue = Volley.newRequestQueue(this)
            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoints.getLoginUrl(),
                jsonObject,
                Response.Listener {
                    Log.d("login",it.toString())
                    var gson = Gson()
                    var loginResponse = gson.fromJson(it.toString(), LoginResponse::class.java)
                    token = loginResponse.token
                    user = loginResponse.user

                    var intent = Intent(this, CategoryActivity::class.java)
                    sessionManager.saveUser(token,user,remember)
                    startActivity(intent)
                },
                Response.ErrorListener {
                    Log.e("abc", it.message.toString())
                }
            )
            requestQueue.add(request)

        }
        new_user.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
package com.yiwencheng.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity(),  View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init(){
        var mode = intent.getIntExtra("MODE",0)

        button_register.setOnClickListener {
            if(mode == 0){
                var firstName = edit_text_name.text.toString()
                var email = edit_text_email.text.toString()
                var password = edit_text_password.text.toString()
                var mobile = edit_text_mobile.text.toString()

                var jsonObject = JSONObject()
                jsonObject.put("firstName",firstName)
                jsonObject.put("email",email)
                jsonObject.put("mobile",mobile)
                jsonObject.put("password",password)

                var requestQueue = Volley.newRequestQueue(this)
                var request = JsonObjectRequest(
                    Request.Method.POST,
                    Endpoints.getRegisterUrl(),
                    jsonObject,
                    Response.Listener {
                        Log.d("register",it.toString())
                    },
                    Response.ErrorListener {
                        Log.e("abc",it.message.toString())
                    }
                )
                requestQueue.add(request)
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                var firstName = edit_text_name.text.toString()
                var email = edit_text_email.text.toString()
                var password = edit_text_password.text.toString()
                var mobile = edit_text_mobile.text.toString()
                var sessionManager = SessionManager(this)
                var _id = sessionManager.getUserId()

                var jsonObject = JSONObject()
                jsonObject.put("firstName",firstName)
                jsonObject.put("email",email)
                jsonObject.put("mobile",mobile)
                jsonObject.put("password",password)
                var requestQueue = Volley.newRequestQueue(this)
                var request = JsonObjectRequest(
                    Request.Method.PUT,
                    Endpoints.getUserUrlById(_id!!),
                    jsonObject,
                    Response.Listener {
                        Log.d("modifieduser",it.toString())
                        sessionManager.logout()
                        Toast.makeText(applicationContext,_id,Toast.LENGTH_SHORT).show()
                    },
                    Response.ErrorListener {
                        Log.e("abc",it.message.toString())
                    }
                )
                requestQueue.add(request)
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }


        already_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
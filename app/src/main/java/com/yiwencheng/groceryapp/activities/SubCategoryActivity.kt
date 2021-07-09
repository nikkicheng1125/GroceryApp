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
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.adapters.ProductAdapter
import com.yiwencheng.groceryapp.adapters.SubCatAdapter
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.Data
import com.yiwencheng.groceryapp.models.Data.Companion.KEY_DATA
import com.yiwencheng.groceryapp.models.ProductData
import com.yiwencheng.groceryapp.models.SubCatData
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.app_bar.*

class SubCategoryActivity : AppCompatActivity() {

    var subCatList: ArrayList<SubCatData> = ArrayList()
    var subCatAdapter: SubCatAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        var toolbar = my_toolbar
        toolbar.title = "Detailed Categories"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun init() {
        subCatList.clear()
        var subId: Int
        var subName: String
        var data = intent.getSerializableExtra(KEY_DATA) as Data
        var catId = data.catId

        var requestQueue = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(
            Request.Method.GET,
            Endpoints.getSubCategoryByCatId(catId),
            null,
            Response.Listener {
                var jsonArray = it.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    var jsonObject = jsonArray.getJSONObject(i)
                    subId = jsonObject.getInt("subId")
                    subName = jsonObject.getString("subName")
                    var subCatdata = SubCatData(catId, subId, subName)
                    subCatList.add(subCatdata)
                }
                subCatAdapter?.setData(subCatList)
                var subCatAdapter = SubCatAdapter(supportFragmentManager)

                for (item in subCatList) {
                    subCatAdapter.addFragment(item)
                }

                view_pager_sub.adapter = subCatAdapter
                tab_layout_sub.setupWithViewPager(view_pager_sub)
            },
            Response.ErrorListener {
                Log.e("abc", it.message.toString())
            }
        )
        requestQueue.add(request)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_cart_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_home -> startActivity(Intent(this, CategoryActivity::class.java))
            R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        init()
    }

}
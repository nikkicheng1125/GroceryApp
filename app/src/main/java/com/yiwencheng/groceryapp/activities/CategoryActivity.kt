package com.yiwencheng.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.adapters.Adapter
import com.yiwencheng.groceryapp.adapters.ViewPagerAdapter
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.SessionManager
import com.yiwencheng.groceryapp.models.Data
import com.yiwencheng.groceryapp.models.Data.Companion.KEY_DATA
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_category.*
import kotlinx.android.synthetic.main.nav_header.view.*


class CategoryActivity : AppCompatActivity(),Adapter.OnAdapterListener,NavigationView.OnNavigationItemSelectedListener  {

    var myList:ArrayList<Data> = ArrayList()
    var adapterImage:Adapter?=null
    var adapterSlide:ViewPagerAdapter?=null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        var mtoolbar = my_toolbar
        mtoolbar.title = "Home"

        setSupportActionBar(mtoolbar)

        init()

    }

    private fun init(){
        getData()

        var sessionManager = SessionManager(this)
        drawerLayout = drawer_layout
        navView = nav_view
        var headerView = navView.getHeaderView(0)
        headerView.text_view_header_name.text = sessionManager.getName()
        headerView.text_view_nav_header_email.text = sessionManager.getEmail()
        var toggle = ActionBarDrawerToggle(
            this,drawerLayout,my_toolbar,0,0
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        adapterSlide = ViewPagerAdapter(this,myList)
        view_pager.adapter = adapterSlide
        adapterImage = Adapter(this,myList)
        adapterImage!!.setOnAdapterListener(this)
        recycler_view.adapter = adapterImage
        recycler_view.layoutManager = GridLayoutManager(this,2)
    }

    private fun getData(){
        var cat_image:String
        var title:String
        var catId:Int

        var requestQueue = Volley.newRequestQueue(this)
        var request =JsonObjectRequest(
            Request.Method.GET,
            Endpoints.getCategoryUrl(),
            null,
            Response.Listener {
                var jsonArray = it.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    var jsonObject = jsonArray.getJSONObject(i)
                    cat_image = jsonObject.getString("catImage")
                    title = jsonObject.getString("catName")
                    catId = jsonObject.getInt("catId")
                    var data = Data(catId,cat_image,title)
                    myList.add(data)
                }
                adapterImage?.setData(myList)
                adapterSlide?.setData(myList)
            },
            Response.ErrorListener {
                Log.e("abc", it.message.toString())
            }
        )
        requestQueue.add(request)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_cart -> startActivity(Intent(this,CartActivity::class.java))
        }
        return true
    }


    override fun onItemClick(data: Data) {
        var intent = Intent(this,SubCategoryActivity::class.java)
        intent.putExtra(KEY_DATA,data)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var sessionManager = SessionManager(this)
        when(item.itemId){
            R.id.item_account->{
                var intent = Intent(this,RegisterActivity::class.java)
                intent.putExtra("MODE",1)
                startActivity(intent)
            }
            R.id.item_orders -> startActivity(Intent(this,OrderhistoryActivity::class.java))
            R.id.item_logout -> {
                sessionManager.logout()
                startActivity(Intent(this,LoginActivity::class.java))
            }
            R.id.item_rate -> Toast.makeText(applicationContext,"To be added in future",Toast.LENGTH_SHORT).show()
            R.id.item_setting -> Toast.makeText(applicationContext,"To be added in future",Toast.LENGTH_SHORT).show()
            R.id.item_address ->  startActivity(Intent(this,AddressActivity::class.java))
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

package com.yiwencheng.groceryapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.activities.ProductDetailActivity
import com.yiwencheng.groceryapp.adapters.ProductAdapter
import com.yiwencheng.groceryapp.app.Endpoints
import com.yiwencheng.groceryapp.helpers.DBHelper
import com.yiwencheng.groceryapp.models.CartProduct
import com.yiwencheng.groceryapp.models.ProductData
import com.yiwencheng.groceryapp.models.ProductData.Companion.KEY_PRODUCT
import kotlinx.android.synthetic.main.fragment_product_list.view.*
import kotlinx.android.synthetic.main.row_adapter_cart.*
import kotlinx.android.synthetic.main.row_adapter_cart.text_view_quantity
import kotlinx.android.synthetic.main.row_adapter_product.*

class ProductListFragment : Fragment(),ProductAdapter.OnProductAdapterListener {

    var myProductList: ArrayList<ProductData> = ArrayList()
    var adapterProduct: ProductAdapter? = null
    private var subId: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId = it.getInt("subId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_product_list, container, false)
        init(view)

        return view
    }

    private fun init(view: View) {
        var id:String
        var image: String
        var mrp: Int
        var price: Int
        var productName: String
        var unit: String
        var ordered_quantity:Int=0

        var requestQueue = Volley.newRequestQueue(this.context)
        var request = JsonObjectRequest(
            Request.Method.GET,
            Endpoints.getProductUrlBySubId(subId),
            null,
            Response.Listener {
                var jsonArray = it.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    var jsonObject = jsonArray.getJSONObject(i)
                     image = jsonObject.getString("image")
                     mrp = jsonObject.getInt("mrp")
                     price = jsonObject.getInt("price")
                     productName = jsonObject.getString("productName")
                     id = jsonObject.getString("_id")
                     unit = jsonObject.getString("unit")
                    var product = ProductData(id,image, mrp.toFloat(), price.toFloat(), productName, unit,ordered_quantity)
                    myProductList.add(product)
                }
                adapterProduct?.setData(myProductList)
                adapterProduct = ProductAdapter(requireActivity().applicationContext, myProductList)
                adapterProduct!!.setOnProductAdapterListener(this)

                view.recycler_view_fragment.adapter = adapterProduct
                view.recycler_view_fragment.layoutManager = LinearLayoutManager(this.context)
            },
            Response.ErrorListener {
                Log.e("abc", it.message.toString())
            }
        )
        requestQueue.add(request)

    }

    companion object {
        @JvmStatic
        fun newInstance(subId: Int) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putInt("subId", subId)
                }
            }
    }

    override fun onProductItemClick(productData: ProductData) {
       var intent = Intent(activity,ProductDetailActivity::class.java)
        intent.putExtra(KEY_PRODUCT,productData)
        startActivity(intent)
    }

}
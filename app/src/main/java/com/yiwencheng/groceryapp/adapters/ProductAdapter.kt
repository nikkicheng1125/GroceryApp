package com.yiwencheng.groceryapp.adapters

import android.content.Context
import android.graphics.Paint
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.helpers.DBHelper
import com.yiwencheng.groceryapp.models.CartProduct
import com.yiwencheng.groceryapp.models.ProductData
import kotlinx.android.synthetic.main.row_adapter_cart.*
import kotlinx.android.synthetic.main.row_adapter_product.view.*
import kotlinx.android.synthetic.main.row_adapter_product.view.button_minus
import kotlinx.android.synthetic.main.row_adapter_product.view.button_plus

import java.util.ArrayList

class ProductAdapter(var myContext: Context?, var myList: ArrayList<ProductData>) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    var dbHelper = DBHelper(myContext!!)
    lateinit var product: ProductData
    private var mycartList: ArrayList<CartProduct> = ArrayList()
    private var listener: OnProductAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view =
            LayoutInflater.from(myContext).inflate(R.layout.row_adapter_product, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        product = myList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(list: ArrayList<ProductData>) {
        myList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(productData: ProductData) {
            mycartList = dbHelper.readProduct()
            var idlist: ArrayList<String> = ArrayList()
            var quantitylist: ArrayList<Int> = ArrayList()

            for (i in 0 until mycartList.size) {
                idlist.add(mycartList[i]._id)
                quantitylist.add(mycartList[i].quantity)
            }

            if (productData._id in idlist) {
                var index = idlist.indexOf(productData._id)
                productData.ordered_quantity = quantitylist[index]
                itemView.button_minus.visibility = View.VISIBLE
                itemView.button_plus.visibility = View.VISIBLE
                itemView.button_tocart.visibility = View.GONE
                itemView.text_view_quantity.text = quantitylist[index].toString()
            } else {
                itemView.button_minus.visibility = View.GONE
                itemView.button_plus.visibility = View.GONE
                itemView.button_tocart.visibility = View.VISIBLE
                itemView.text_view_quantity.text = ""
                productData.ordered_quantity = 0
            }

            var url = "https://rjtmobile.com/grocery/images/" + productData.image
            Picasso.get().load(url).placeholder(R.drawable.noimage).error(R.drawable.noimage)
                .into(itemView.image_view_product)
            itemView.text_view_productname.text = productData.productName
            itemView.text_view_price.text = "$" + productData.price.toString()
            itemView.text_view_mrp.text = "$" + productData.mrp.toString()
            itemView.text_view_mrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.text_view_unit.text = "Unit: ${productData.unit}"

            itemView.setOnClickListener {
                listener?.onProductItemClick(productData)
            }

            itemView.button_tocart.setOnClickListener {
                if(productData.ordered_quantity >= 1){
                    itemView.button_tocart.visibility = View.GONE
                }else if(productData.ordered_quantity == 0){
                    itemView.button_tocart.visibility = View.VISIBLE

                    productData.ordered_quantity += 1
                    var newCartItem = CartProduct(
                        productData._id,
                        productData.image,
                        productData.mrp,
                        productData.price,
                        productData.productName,
                        productData.ordered_quantity
                    )
                    dbHelper.addProduct(newCartItem)
                    itemView.button_tocart.visibility = View.GONE
                    itemView.button_minus.visibility = View.VISIBLE
                    itemView.button_plus.visibility = View.VISIBLE
                    itemView.text_view_quantity.text = "1"
                }
            }

            itemView.button_plus.setOnClickListener {
                productData.ordered_quantity += 1
                var newCartItem = CartProduct(
                    productData._id,
                    productData.image,
                    productData.mrp,
                    productData.price,
                    productData.productName,
                    productData.ordered_quantity
                )
                if (productData.ordered_quantity == 1) {
                    dbHelper.addProduct(newCartItem)
                } else {
                    dbHelper.updateCart(newCartItem)
                }
                itemView.text_view_quantity.text = newCartItem.quantity.toString()
            }

            itemView.button_minus.setOnClickListener {
                if (productData.ordered_quantity > 1) {
                    productData.ordered_quantity -= 1
                    itemView.text_view_quantity.text = productData.ordered_quantity.toString()
                    var newCartItem = CartProduct(
                        productData._id,
                        productData.image,
                        productData.mrp,
                        productData.price,
                        productData.productName,
                        productData.ordered_quantity
                    )
                    dbHelper.updateCart(newCartItem)
                } else if (productData.ordered_quantity == 1) {
                    productData.ordered_quantity = 0
                    itemView.text_view_quantity.text = ""
                    itemView.button_tocart.visibility = View.VISIBLE
                    itemView.button_minus.visibility = View.GONE
                    itemView.button_plus.visibility = View.GONE
                    dbHelper.deleteProduct(productData._id)
                } else {
                    productData.ordered_quantity = 0
                    itemView.button_tocart.visibility = View.VISIBLE
                    itemView.button_minus.visibility = View.GONE
                    itemView.button_plus.visibility = View.GONE
                    itemView.text_view_quantity.text = ""
                }
            }

        }
    }

    fun setOnProductAdapterListener(onProductAdapterListener: OnProductAdapterListener) {
        listener = onProductAdapterListener
    }

    interface OnProductAdapterListener {
        fun onProductItemClick(productData: ProductData)
    }

}
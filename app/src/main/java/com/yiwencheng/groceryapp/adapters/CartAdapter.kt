package com.yiwencheng.groceryapp.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.helpers.DBHelper
import com.yiwencheng.groceryapp.models.CartProduct
import com.yiwencheng.groceryapp.models.ProductData
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.row_adapter_cart.view.*

class CartAdapter(var myContext: Context, var cartList:ArrayList<CartProduct>):RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    private  var listener:OnCartAdapterListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(myContext).inflate(R.layout.row_adapter_cart,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var product = cartList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    fun setData(list: ArrayList<CartProduct>) {
        cartList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(cartProduct: CartProduct){
            var url ="https://rjtmobile.com/grocery/images/"+ cartProduct.image
            Picasso.get().load(url).placeholder(R.drawable.noimage).error(R.drawable.noimage).into(itemView.image_view_cart)
            itemView.text_view_name.text = cartProduct.productName
            itemView.text_view_mrp_price.text = "$"+cartProduct.mrp.toString()
            itemView.text_view_mrp_price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.text_view_actual_price.text = "$" +cartProduct.price.toString()
            itemView.text_view_quantity.text = cartProduct.quantity.toString()

            itemView.button_plus.setOnClickListener {
                listener?.onClickPlus(cartProduct)
            }

            itemView.button_minus.setOnClickListener {
                listener?.onClickMinus(cartProduct)
            }
            itemView.text_view_x.setOnClickListener {
                listener?.onClickDelete(cartProduct)
            }
        }
    }

    fun setOnCartAdapterListener(onAdapterListener: OnCartAdapterListener){
        listener = onAdapterListener
    }

    interface OnCartAdapterListener{
        fun onClickDelete(cartProduct: CartProduct)
        fun onClickMinus(cartProduct: CartProduct)
        fun onClickPlus(cartProduct: CartProduct)
    }
}
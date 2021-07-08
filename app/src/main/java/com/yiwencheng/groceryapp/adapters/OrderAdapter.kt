package com.yiwencheng.groceryapp.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.models.CartProduct
import kotlinx.android.synthetic.main.row_adapter_order_summary.view.*
import kotlinx.android.synthetic.main.row_adapter_order_summary.view.text_view_quantity

class OrderAdapter(var myContext: Context, var cartList:ArrayList<CartProduct>):RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.MyViewHolder {
        var view = LayoutInflater.from(myContext).inflate(R.layout.row_adapter_order_summary,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderAdapter.MyViewHolder, position: Int) {
        var products = cartList[position]
        holder.bind(products)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(cartProduct: CartProduct){
            var url ="https://rjtmobile.com/grocery/images/"+ cartProduct.image
            Picasso.get().load(url).placeholder(R.drawable.noimage).error(R.drawable.noimage).into(itemView.image_view_product)
            itemView.text_view_productname.text = cartProduct.productName
            itemView.text_view_mrp.text = "$"+cartProduct.mrp.toString()
            itemView.text_view_mrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.text_view_price.text = "$" +cartProduct.price.toString()
            itemView.text_view_quantity.text = "Quantity: "+cartProduct.quantity.toString()
        }
    }



}
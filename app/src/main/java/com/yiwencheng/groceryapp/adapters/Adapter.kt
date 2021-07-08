package com.yiwencheng.groceryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.models.Data
import kotlinx.android.synthetic.main.row_adapter.view.*

class Adapter(var myContext: Context,var myList:ArrayList<Data>):RecyclerView.Adapter<Adapter.MyViewHolder>() {

    private  var listener:OnAdapterListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(myContext).inflate(R.layout.row_adapter,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var data = myList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(list:ArrayList<Data>){
        myList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(data:Data){
           var url ="https://rjtmobile.com/grocery/images/"+data.catImage
            Picasso.get().load(url).into(itemView.image1)
            itemView.text_view1.text = data.catName

            itemView.setOnClickListener {
                listener?.onItemClick(data)
            }
        }
    }

    fun setOnAdapterListener(onAdapterListener: OnAdapterListener){
        listener = onAdapterListener
    }

    interface OnAdapterListener{
        fun onItemClick(data:Data)
    }
}



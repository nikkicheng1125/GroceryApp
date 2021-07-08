package com.yiwencheng.groceryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.models.History
import kotlinx.android.synthetic.main.row_adapter_order_history.view.*

class HistoryAdapter(var myContext: Context,var myHistoryList: ArrayList<History>):RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    private var listener: HistoryAdapter.OnHistoryAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(myContext).inflate(R.layout.row_adapter_order_history,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var history = myHistoryList[position]
        holder.bind(history)
    }

    fun setData(list: ArrayList<History>) {
        myHistoryList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return myHistoryList.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(history: History){
            var url ="https://rjtmobile.com/grocery/images/" + history.products[0].image
            Picasso.get().load(url).placeholder(R.drawable.noimage).error(R.drawable.noimage).into(itemView.image_view_history)
            itemView.text_view_date.text = "Date: ${history.date.subSequence(0,10)}"
            itemView.text_view_total_mrp.text = "Subtotal: ${history.orderSummary.totalAmount}"
            itemView.text_view_total_pay.text = "Payment: ${history.orderSummary.ourPrice}"
            itemView.text_view_status.text = "Status: ${history.payment.paymentStatus}"

            itemView.button_view_details.setOnClickListener {
                listener?.onItemClick(history)
            }
        }
    }

    fun setOnHistoryAdapterListener(onHistoryAdapterListener: OnHistoryAdapterListener) {
        listener = onHistoryAdapterListener
    }

    interface OnHistoryAdapterListener {
        fun onItemClick(history: History)
    }

}
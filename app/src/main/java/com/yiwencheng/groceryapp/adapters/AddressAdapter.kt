package com.yiwencheng.groceryapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.models.Address
import com.yiwencheng.groceryapp.models.Data
import kotlinx.android.synthetic.main.row_adapter_address.view.*

class AddressAdapter(var myContext: Context, var myAddressList: ArrayList<Address>) :
    RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {

    private var listener: AddressAdapter.OnAddressAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view =
            LayoutInflater.from(myContext).inflate(R.layout.row_adapter_address, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var address = myAddressList[position]
        holder.bind(address)
    }

    override fun getItemCount(): Int {
        return myAddressList.size
    }

    fun setData(list: ArrayList<Address>) {
        myAddressList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(address: Address) {
            itemView.text_view_name.text = address.name
            itemView.text_view_email.text =address.email
            itemView.text_view_address_line1.text = address.streetName+","
            itemView.text_view_address_line2.text = address.houseNo
            itemView.text_view_address_city.text = address.city
            if(address.pincode.toString().length == 4){
                itemView.text_view_address_zipcode.text = "0"+address.pincode.toString()
            }else{
                itemView.text_view_address_zipcode.text = address.pincode.toString()
            }

            itemView.text_view_address_housetype.text = address.type

            itemView.setOnClickListener {
                listener?.onAddressClick(address)
            }

            itemView.text_view_delete_address.setOnClickListener {
                listener?.onClickDelete(address)
            }

            itemView.text_view_edit.setOnClickListener {
                listener?.onClickEdit(address)
            }
        }
    }

    fun setOnAddressAdapterListener(onAddressAdapterListener: OnAddressAdapterListener) {
        listener = onAddressAdapterListener
    }

    interface OnAddressAdapterListener {
        fun onAddressClick(address: Address)
        fun onClickDelete(address: Address)
        fun onClickEdit(address: Address)
    }
}
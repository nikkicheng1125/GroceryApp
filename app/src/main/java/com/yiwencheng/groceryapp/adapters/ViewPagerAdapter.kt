package com.yiwencheng.groceryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import com.yiwencheng.groceryapp.R
import com.yiwencheng.groceryapp.models.Data
import kotlinx.android.synthetic.main.row_adapter.view.*
import kotlinx.android.synthetic.main.viewpager_adapter.view.*
import java.util.*

class ViewPagerAdapter(var myContext: Context,var myList:ArrayList<Data>): PagerAdapter(){

    override fun getCount(): Int {
        return myList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view == `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = LayoutInflater.from(myContext).inflate(R.layout.viewpager_adapter,container,false)
        var data = myList[position]
        var url ="https://rjtmobile.com/grocery/images/"+ data.catImage
        Picasso.get().load(url).into(view.pager_image)
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    fun setData(list:ArrayList<Data>){
        myList = list
        notifyDataSetChanged()
    }
}

//
//class ViewPagerAdapter(fragmentManager: FragmentManager, var myList:ArrayList<Data> = ArrayList()): FragmentPagerAdapter(fragmentManager) {
//
//
//
//    override fun getCount(): Int {
//       return myList.size
//    }
//
//    override fun getItem(position: Int): Fragment {
//       return Fragment1(position,myList)
//
//       }
//    }




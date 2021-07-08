package com.yiwencheng.groceryapp.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yiwencheng.groceryapp.fragments.ProductListFragment
import com.yiwencheng.groceryapp.models.SubCatData

class SubCatAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {

    var myFragmentList:ArrayList<Fragment> = ArrayList()
    var myTitleList:ArrayList<String> = ArrayList()
    var subCatList:ArrayList<SubCatData> = ArrayList()

    override fun getCount(): Int {
        return myTitleList.size
    }

    override fun getItem(position: Int): Fragment {
        return myFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return myTitleList[position]
    }

    fun addFragment(subCatData: SubCatData){
        myFragmentList.add(ProductListFragment.newInstance(subCatData.subId))
        myTitleList.add(subCatData.subName)
    }

    fun setData(list: ArrayList<SubCatData>){
        subCatList = list
        notifyDataSetChanged()
    }

}
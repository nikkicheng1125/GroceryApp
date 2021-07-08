package com.yiwencheng.groceryapp.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.yiwencheng.groceryapp.models.CartProduct
import com.yiwencheng.groceryapp.models.ProductData

class DBHelper(var myContext: Context) : SQLiteOpenHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "myproductDatabase"
        const val DATABASE_VERSION = 2
        const val TABLE_NAME = "product"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "productName"
        const val COLUMN_MRP = "mrp"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_QUANTITY = "quantity"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("abc", "onCreate")
        var createTable = "create table $TABLE_NAME ($COLUMN_ID char(100), $COLUMN_NAME char(50), $COLUMN_MRP float,$COLUMN_PRICE float, $COLUMN_IMAGE char(100),$COLUMN_QUANTITY integer)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("abc", "onUpgrade")
        var dropTable = "drop table $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    fun addProduct(cartProduct: CartProduct) {
        var db = writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COLUMN_ID, cartProduct._id)
        contentValues.put(COLUMN_NAME, cartProduct.productName)
        contentValues.put(COLUMN_MRP, cartProduct.mrp)
        contentValues.put(COLUMN_PRICE,cartProduct.price)
        contentValues.put(COLUMN_IMAGE, cartProduct.image)
        contentValues.put(COLUMN_QUANTITY, cartProduct.quantity)
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun updateCart(cartProduct: CartProduct) {
        var db = writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COLUMN_QUANTITY, cartProduct.quantity)
        var whereClause = "$COLUMN_ID = ?"
        var whereArg = arrayOf(cartProduct._id)
        db.update(TABLE_NAME,contentValues,whereClause,whereArg)
    }

    fun deleteProduct(_id:String){
        var db = writableDatabase
        var whereClause = "$COLUMN_ID = ?"
        var whereArgs = arrayOf(_id)
        db.delete(TABLE_NAME,whereClause,whereArgs)
    }

    fun readProduct():ArrayList<CartProduct>{
        var db = readableDatabase
        var productList:ArrayList<CartProduct> = ArrayList()
        var columns = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_MRP ,
            COLUMN_PRICE,
            COLUMN_IMAGE,
            COLUMN_QUANTITY
        )
        var cursor = db.query(TABLE_NAME,columns,null,null,null,null,null)
        if(cursor != null && cursor.moveToFirst()){
            do {
                var _id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                var mrp = cursor.getFloat(cursor.getColumnIndex(COLUMN_MRP))
                var price = cursor.getFloat(cursor.getColumnIndex(COLUMN_PRICE))
                var image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                var quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                var product = CartProduct(_id,image,mrp,price,name,quantity)
                productList.add(product)
            }while (cursor.moveToNext())
        }
        return productList
    }

}
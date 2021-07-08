package com.yiwencheng.groceryapp.helpers

import android.content.Context
import com.yiwencheng.groceryapp.User

class SessionManager(var myContext: Context) {
    private val FILE_NAME = "login_info"
    private val NAME = "NAME"
    private val EMAIL = "EMAIL"
    private val PASSWORD = "PASSWORD"
    private val MOBILE = "MOBILE"
    private val TOKEN = "TOKEN"
    private val IS_LOGGED_IN = "isLoggedIn"
    private val USERID = "ID"

    private var sharePreference = myContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    private var editor = sharePreference.edit()

    fun saveUser(token: String,user: User,remember:Boolean) {
        editor.putString(TOKEN,token)
        editor.putString(NAME, user.firstName)
        editor.putString(EMAIL, user.email)
        editor.putString(PASSWORD, user.password)
        editor.putString(MOBILE, user.mobile)
        editor.putString(USERID,user._id)
        editor.putBoolean(IS_LOGGED_IN, remember)
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        return sharePreference.getBoolean(IS_LOGGED_IN, false)
    }


    fun login(email: String, password: String): Boolean {
        var saveEmail = sharePreference.getString(EMAIL, null)
        var savePassword = sharePreference.getString(PASSWORD, null)
        return email.equals(saveEmail) && password.equals(savePassword)
    }

    fun getName(): String? {
        return sharePreference.getString(NAME, null)
    }

    fun getEmail():String?{
        return sharePreference.getString(EMAIL, null)
    }

    fun getMobile():String?{
        return sharePreference.getString(MOBILE, null)
    }

    fun getToken():String?{
        return sharePreference.getString(TOKEN, null)
    }

    fun getUserId():String?{
        return sharePreference.getString(USERID, null)
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }


}
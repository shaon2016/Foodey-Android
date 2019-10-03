package com.example.foodey.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.preference.PreferenceScreen
import com.example.foodey.models.User
import com.example.foodey.util.U
import org.json.JSONObject


/**
 * Created by touhidroid on 11/16/16.
 *
 * @author touhidroid
 */

object P {
    private val TAG = P::class.java.simpleName

    private val PREF_USER_ID = "_pref_user_id"
    private val PREF_USER_NAME = "_pref_user_name"
    private val PREF_USER_USER_NAME = "_pref_user_user_name"
    private val PREF_USER_EMAIL = "_pref_user_email"
    private val PREF_USER_MOBILE = "_pref_user_mobile"
    private val PREF_API_TOKEN = "_pref_user_api_token"


    private var prefs: SharedPreferences? = null
    private var prefsEditor: SharedPreferences.Editor? = null

    @SuppressLint("CommitPrefEdits")
    private fun assurePrefNotNull(context: Context) {
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
        if (prefsEditor == null) {
            prefsEditor = PreferenceManager
                .getDefaultSharedPreferences(context).edit()
            prefsEditor!!.apply()
        }
    }


    //User name
    fun setUserName(context: Context, countryCode: String) {
        assurePrefNotNull(context)
        prefsEditor!!.putString(PREF_USER_NAME, countryCode)
        prefsEditor!!.commit()
    }

    fun getUserName(ctx: Context): String? {
        assurePrefNotNull(ctx)
        return prefs!!.getString(PREF_USER_NAME, "")
    }

    fun getUserUserName(ctx: Context): String? {
        assurePrefNotNull(ctx)
        return prefs!!.getString(PREF_USER_USER_NAME, "")
    }

    //User email
    fun setUserEmail(context: Context, countryCode: String) {
        assurePrefNotNull(context)
        prefsEditor!!.putString(PREF_USER_EMAIL, countryCode)
        prefsEditor!!.commit()
    }

    fun getUserEmail(ctx: Context): String? {
        assurePrefNotNull(ctx)
        return prefs!!.getString(PREF_USER_EMAIL, "")
    }

    // User Mobile no
    fun setUserMobile(context: Context, phoneNo: String) {
        assurePrefNotNull(context)
        prefsEditor!!.putString(PREF_USER_MOBILE, phoneNo)
        prefsEditor!!.commit()
    }

    fun getUserMobile(ctx: Context): String? {
        assurePrefNotNull(ctx)
        return prefs!!.getString(PREF_USER_MOBILE, "")
    }

    // User id
    fun setUserId(context: Context, id: Int) {
        assurePrefNotNull(context)
        prefsEditor!!.putInt(PREF_USER_ID, id)
        prefsEditor!!.commit()
    }


    fun getUserId(ctx: Context): Int {
        assurePrefNotNull(ctx)
        return prefs!!.getInt(PREF_USER_ID, 0)
    }


    fun getApiToken(ctx: Context): String {
        assurePrefNotNull(ctx)
        return prefs!!.getString(PREF_API_TOKEN, "") ?: ""
    }


    //Login
    fun isLoggedIn(ctx: Context): Boolean {
        assurePrefNotNull(ctx)
        return prefs!!.getInt(PREF_USER_ID, 0) > 0
    }


    fun saveLoginResponse(context: Context, user: User) {
        assurePrefNotNull(context)

        prefsEditor?.putInt(PREF_USER_ID, user.id)
        prefsEditor?.putString(PREF_USER_NAME, user.name)
        prefsEditor?.putString(PREF_USER_MOBILE, user.mobile)

        prefsEditor?.apply()
    }

    fun signOut(context: Context) {
        assurePrefNotNull(context)

        prefsEditor?.remove(PREF_USER_ID)
        prefsEditor?.remove(PREF_USER_NAME)
        prefsEditor?.remove(PREF_USER_MOBILE)

        prefsEditor?.apply()
    }

    //End of login


}


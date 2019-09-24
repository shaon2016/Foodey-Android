package com.example.foodey.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.text.SpannableString
import android.text.TextUtils
import android.text.format.DateUtils
import android.text.style.UnderlineSpan
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.example.foodey.R

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.NetworkInterface
import java.net.URISyntaxException
import java.net.URL
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.EnumSet
import java.util.LinkedHashMap
import java.util.Locale
import java.util.StringTokenizer
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Created by touhidroid on 4/21/16.
 *
 * @author touhidroid
 */
object U {

    private val TAG = U::class.java.simpleName
    private val KEY_NOTIF_GRP_SN = "sn_gcm_notifs_grp"

    // private static RegimeApp regimeAppInstance;
    private val LTRIM = Pattern.compile("^\\s+")
    private val RTRIM = Pattern.compile("\\s+$")

    val curSDK: Int
        get() = Build.VERSION.SDK_INT

    val isAboveLollipop: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    val currentTimeStamp: String
        get() {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val now = Date()
            return sdfDate.format(now)
        }
    //endregion

    val timeElapsedCurrent = getTimeElapsedAfterPost(currentTimeStamp) as String


    fun isSDCardMounted(): Boolean {
        val status = Environment.getExternalStorageState()
        return status == Environment.MEDIA_MOUNTED
    }

    val todayDate: String
        get() {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val date = Date()
            return formatter.format(date)
        }

    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * @return Screen width or height, depending on which is minimum
     */
    fun getScreenMinHW(context: Context): Int {
        val dm = context.resources.displayMetrics
        val w = dm.widthPixels
        val h = dm.heightPixels
        return if (w < h) w else h
    }

    fun getDeviceDetailsJSONString(ctx: Context): String {
        val jo = JSONObject()
        try {
            jo.put("os", Build.VERSION.SDK_INT)
            jo.put("model", Build.MODEL)
            jo.put("manufacturer", Build.MANUFACTURER)
            jo.put("ip", getDeviceIp(true))
            // jo.put("imei", getDeviceIMEI(ctx));
            // jo.put("mac", getDeviceMAC(ctx));
            jo.put("dpi", getDeviceDpi(ctx).toDouble())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jo.toString()
    }

    fun getDeviceDpi(context: Context): Float {
        return context.resources.displayMetrics.density * 160.0f
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun pxToDp(context: Context, px: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    @SuppressLint("DefaultLocale")
    fun getDeviceIp(useIPv4: Boolean): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(0, delim).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            // for now eat exceptions
        }

        return ""
    }

    fun isGPSProviderEnabled(locationManager: LocationManager): Boolean {
        // getting GPS status
        return locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun isNetProviderEnabled(locationManager: LocationManager): Boolean {
        // getting network status
        return locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun isDevicePortrait(res: Resources): Boolean {
        return res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    fun isNetConnected(context: Context?): Boolean {
        if (context == null)
            return false
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission")
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission")
        val wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return wifiInfo.isConnected
    }

    fun saveBitmapToFile(bmpToSave: Bitmap, directory: File,
                         fileName: String) {
        if (!directory.exists())
            directory.mkdirs()
        val file = File(directory, fileName)
        val fOut: FileOutputStream
        try {
            fOut = FileOutputStream(file)
            bmpToSave.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun getResId(drawableName: String): Int {
        try {
            val res = R.drawable::class.java
            val field = res.getField(drawableName)
            return field.getInt(null)
        } catch (e: Exception) {
            Log.e("COUNTRYPICKER", "Failure to get drawable id.", e)
            return -1
        }

    }

    // region Validation Section
    fun isEmailValid(email: String): Boolean {
        return email.length > 3 && email.indexOf('@') > 0 && isValidEmailAddress(email)
    }
    //endregion

    @SuppressLint("MissingPermission")
    fun getFirstSimCardNumber(tContext: Context): String {
        try {
            val tm = tContext
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.line1Number
        } catch (e: Exception) {
            Log.e(TAG,
                    "getFirstSimCardNumber : Could not retrieve the SIM card number")
            return ""
        }

    }

    fun isValidEmailAddress(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPhoneNumberValid(phoneNo: String): Boolean {
        return (phoneNo.startsWith("880") && phoneNo.length > 11 || !phoneNo.startsWith("880") && phoneNo.length > 9) && phoneNo.matches("\\d+".toRegex())
    }

    @JvmOverloads
    fun sendEmail(context: Context, mailTos: Array<String>, subject: String, body: String,
                  type: String = "plain/text", chooserTitle: String = "Send eMail") {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = type
        intent.putExtra(Intent.EXTRA_EMAIL, mailTos)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(intent, chooserTitle))
    }

    fun dialToCall(context: Context, phoneNumber: String) {
        if (phoneNumber.contains(",")) {
            showCallChooser(context, phoneNumber)
        } else {
            val callUri = Uri.parse("tel:$phoneNumber")
            context.startActivity(Intent(Intent.ACTION_DIAL, callUri))
        }
    }

    private fun showCallChooser(context: Context, phoneNumber: String) {
        val builderSingle = AlertDialog.Builder(context)
        // builderSingle.setIcon(R.mipmap.ic_launcher_round);
        builderSingle.setTitle("Select to Dial:")

        val arrayAdapter = ArrayAdapter<String>(context,
                android.R.layout.select_dialog_item)
        val tokens = StringTokenizer(phoneNumber, ",")
        while (tokens.hasMoreTokens()) {
            val num = tokens.nextToken().trim { it <= ' ' }
            if (!num.toLowerCase().startsWith("ext:"))
                arrayAdapter.add(num)
        }

        builderSingle.setAdapter(arrayAdapter) { dialog, which -> dialToCall(context, arrayAdapter.getItem(which)!!) }
        builderSingle.show()
    }

    fun parseSecondsToTimeStr(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return minutes.toString() + ":" + secs
    }

    fun parseMinuteToTimeStr(minutes: Int): String {
        val hours = minutes / 60
        val min = minutes % 60
        val minStr = min.toString() + " minute" + if (min > 1) "s" else ""
        val amp = " & "
        return if (hours > 0)
            hours.toString() + " hour" + (if (hours > 1) "s" else "") + if (min > 0) amp + minStr else ""
        else
            minStr
    }

    fun getFormattedSize(sizeInBytes: Long): String {
        var ret = ""
        if (sizeInBytes < 1024)
        // Less than 1KB
            ret = sizeInBytes.toString() + "B"
        else if (sizeInBytes < 1024 * 1024)
        // less than 1MB
            ret = String.format(Locale.US, "%.2fKB", sizeInBytes.toFloat() / 1024.0)
        else if (sizeInBytes < 1024 * 1024 * 1024)
        // less than 1GB
            ret = String.format(Locale.US, "%.2fMB", sizeInBytes.toFloat() / (1024.0 * 1024.0))
        else
            ret = String.format(Locale.US, "%.3fGB", sizeInBytes.toFloat() / (1024.0 * 1024.0 * 1024.0))
        return ret
    }

    fun startActivityWithBgTransition(activity: Activity,
                                      toClass: Class<out Activity>) {
        val it = Intent(activity, toClass)
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        /*View bg = activity.findViewById(R.id.bg);
        if (bg != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity,
                            Pair.create(bg, AppConstants.KEY_BG_TRANSITION));
            ActivityCompat.startActivity(activity, it, options.toBundle());
        } else*/
        activity.startActivity(it)
    }

    fun startActivityWithBgTransition(activity: Activity, intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        /*View bg = activity.findViewById(R.id.bg);
        if (bg != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity,
                            Pair.create(bg, AppConstants.KEY_BG_TRANSITION));
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        } else*/
        activity.startActivity(intent)
    }

    fun startActivityWithBgTransition(activity: Activity, intent: Intent, pairs: Array<androidx.core.util.Pair<View, String>>) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, *pairs)
        ActivityCompat.startActivity(activity, intent, options.toBundle())
    }

    fun startActivityNormally(context: Context, toClass: Class<out Activity>) {
        val intent = Intent(context, toClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

    fun getColor(context: Context, colorResId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            context.resources.getColor(colorResId, null)
        else
            context.resources.getColor(colorResId)
    }

    fun getDrawerWidth(context: Context): Int {
        return (getScreenWidth(context) * 0.60).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun get3LetterFormattedCount(count: Int): String {
        var ret = count.toString() + ""
        if (count > 999999999)
            ret = (count / 1000000000).toString() + "b"
        else if (count > 999999)
            ret = (count / 1000000).toString() + "m"
        else if (count > 999)
            ret = (count / 1000).toString() + "k"
        // else if (count > 99)
        // ret = (count / 100) + "h";
        return ret
    }

    /**
     * Default return is an empty JSONArray [ new JSONArray() ]
     */
    fun getJSONArrayJ(jo: JSONObject?, key: String): JSONArray {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.getJSONArray(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return JSONArray()
    }

    /**
     * Default return is an empty JSONObject [ new JSONObject() ]
     */
    fun getJSONObjectJ(jo: JSONObject?, key: String): JSONObject {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.getJSONObject(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return JSONObject()
    }

    fun getJSONObjectJ(jo: JSONObject?, key: String, defRet: JSONObject): JSONObject {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.getJSONObject(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return defRet
    }

    /**
     * @param defMsg Default return if there is null/empty string inside JSONObject under the key
     */
    @JvmStatic
    fun getStringJ(jo: JSONObject?, key: String, defMsg: String): String {
        if (jo == null)
            return defMsg
        val s = getStringJ(jo, key)
        return if (s == null || s.length < 1 || s.trim { it <= ' ' }.toLowerCase() == "null")
            defMsg
        else
            s
    }

    /**
     * Default return empty string("")
     */
    @JvmStatic
    fun getStringJ(jo: JSONObject?, key: String): String {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.getString(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * Default return is 0.0
     */
    fun getDoubleJ(jo: JSONObject?, key: String): Double {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.getDouble(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return 0.0
    }

    /**
     * @param defRetVal The default return value in case of failed parsing
     */
    fun getDoubleJ(jo: JSONObject?, key: String, defRetVal: Double): Double {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.getDouble(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return defRetVal
    }

    /**
     * Default return is 0
     */
    @JvmStatic
    fun getIntJ(jo: JSONObject?, key: String): Int {
        if (jo == null)
            return 0
        try {
            if (jo.has(key))
                return if (jo.get(key) != null)
                    jo.get(key).toString().toInt()
                else
                    0
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return 0
    }

    @JvmStatic
    fun getLong(jo: JSONObject?, key: String): Long {
        if (jo == null)
            return 0
        try {
            if (jo.has(key))
                return if (jo.get(key) != null)
                    jo.get(key).toString().toLong()
                else
                    0
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return 0
    }

    /**
     * @param defaultRetInt The default return value in case of failed parsing
     */
    @JvmStatic
    fun getIntJ(jo: JSONObject?, key: String, defaultRetInt: Int): Int {
        if (jo == null)
            return 0
        try {
            if (jo.has(key))
                return if (jo.get(key) != null)
                    Integer.parseInt(jo.get(key).toString() + "")
                else
                    0
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return defaultRetInt
    }

    /**
     * Default return is false
     */
    fun getBooleanJ(jo: JSONObject?, key: String): Boolean {
        if (jo == null)
            return false
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return jo.getBoolean(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * @param defRet Default return in case of no value under the specified key.
     */
    fun getBooleanJ(jo: JSONObject?, key: String, defRet: Boolean): Boolean {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.getBoolean(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return defRet
    }

    /**
     * Default return is an empty object [ new Object() ]
     */
    fun getJ(jo: JSONObject?, key: String): Any {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.get(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return Any()
    }

    fun parseStringList(ja: JSONArray): ArrayList<String> {
        val strList = ArrayList<String>()

        val sz = ja.length()
        for (i in 0 until sz) {
            try {
                strList.add(ja.getString(i))
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }

        return strList
    }

    //region Time-related calculations
    fun getTimeElapsedAfterPost(createdAt: String): CharSequence {
        var result: CharSequence = ""
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            try {
                val mDate = sdf.parse(createdAt)
                val tms = mDate.time
                result = DateUtils.getRelativeTimeSpanString(tms,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            Log.e("getTimeElapsedAfterPost", "Date format can't be parsed: $createdAt", e)
        }

        return result
    }

    fun getSmallDateFormat(dateTimeAt: String): String {
        try {
            // Thu, Feb 2, 2017 12:00 AM
            val sdf = SimpleDateFormat("EEE, MMM d, yyyy hh:mm aaa", Locale.ENGLISH)
            try {
                val mDate = sdf.parse(dateTimeAt)
                val sdfDate = SimpleDateFormat("d MMM, yy", Locale.ENGLISH)
                return sdfDate.format(mDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            Log.e("getTimeElapsedAfterPost", "Date format can't be parsed: $dateTimeAt", e)
        }

        return ""
    }

    fun getDateInFormat(d: String): String {
        val sdfFrom = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var date: Date? = null
        try {
            date = sdfFrom.parse(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val sdfTo = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        return sdfTo.format(date)
    }

    @JvmStatic
    fun getDateInFlatFormat(d: String): String {
        val sdfFrom = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        var date: Date? = null
        try {
            date = sdfFrom.parse(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val sdfTo = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return sdfTo.format(date)
    }

    @JvmStatic
    fun getDateInYearMonthDateFormat(d: String): String {
        val sdfFrom = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        var date: Date? = null
        try {
            date = sdfFrom.parse(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val sdfTo = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdfTo.format(date)
    }

    /*This util function only created for admin dashboard
    * It converts a date into readable format*/
    @JvmStatic
    fun getDateInFormatForDashboard(apiDateStr: String): String {
        val sdfFrom = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var date: Date? = null
        try {
            date = sdfFrom.parse(apiDateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val sdfTo = SimpleDateFormat("d MMM", Locale.US)
        return sdfTo.format(date)
    }

    fun convertDateTimeToSecondsForOrder(dateTime: String): Long {
        val c = Calendar.getInstance(TimeZone.getDefault())
        val sdfFrom = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        sdfFrom.timeZone = TimeZone.getTimeZone("UTC")
        try {
            c.time = sdfFrom.parse(dateTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return c.timeInMillis / 1000
    }


    fun dayMonthFormatted(t: Int): String {
        return if (t < 10)
            "0" + (t + 1)
        else
            (t + 1).toString()
    }

    fun getDayFormatted(t: Int): String {
        return if (t < 10)
            "0$t"
        else
            t.toString()
    }

    fun getTimeStamp(formattedTimeStr: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        var timeStamp = java.lang.Long.MIN_VALUE
        try {
            val mDate = sdf.parse(formattedTimeStr)
            timeStamp = mDate.time
        } catch (e: ParseException) {
            Log.e("U.getTimeStamp()",
                    "Failed to format the string: $formattedTimeStr", e)
        }

        return timeStamp
    }

    /**
     * Default return : now
     */
    fun parseDate(dateStr: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val timeStamp = java.lang.Long.MIN_VALUE
        try {
            return sdf.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return Date()
    }

    fun parseDateSimple(dateStr: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            return sdf.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return Date()
    }

    fun readDate(date: Date): String {
        val toDateFormat = SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH)
        return toDateFormat.format(date)
    }

    fun readDate(calendar: Calendar): String {
        val toDateFormat = SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH)
        return toDateFormat.format(calendar.time)
    }

    fun reformatDate(dateStr: String, fromFormat: String, toFormat: String): String {
        val fromDateFormat = SimpleDateFormat(fromFormat, Locale.ENGLISH)
        try {
            val formattedDate = fromDateFormat.parse(dateStr)
            // SimpleDateFormat toDateFormat = new SimpleDateFormat(toFormat, Locale.ENGLISH);
            return reformatDate(formattedDate, toFormat)// toDateFormat.format(formattedDate);
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return dateStr
    }

    fun reformatDate(date: Date, toFormat: String): String {
        val toDateFormat = SimpleDateFormat(toFormat, Locale.ENGLISH)
        return toDateFormat.format(date)
    }

    fun computeDiff(date1: Date, date2: Date): Map<TimeUnit, Long> {
        val diffInMillis = date2.time - date1.time
        val units = ArrayList(EnumSet.allOf(TimeUnit::class.java))
        Collections.reverse(units)
        val result = LinkedHashMap<TimeUnit, Long>()
        var millisRest = diffInMillis
        for (unit in units) {
            val diff = unit.convert(millisRest, TimeUnit.MILLISECONDS)
            val diffInMilliesForUnit = unit.toMillis(diff)
            millisRest = millisRest - diffInMilliesForUnit
            result[unit] = diff
        }
        return result
    }

    fun getDateInMillis(date: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val v = sdf.parse(date)
        return v.time
    }

    fun computeDiffInDays(d1: Date, d2: Date): Int {
        Log.d("DATE", "computeDiffInDays : d1=$d1, d2=$d2")
        var a = (d1.time - d2.time) / (1000 * 60 * 60 * 24)
        if (a < 0) a = a * -1
        return Math.round(a.toFloat())
    }

    fun getSmallFormattedTime(timeInSecs: Int): String {
        var timeInSecs = timeInSecs
        var ret = ""

        if (timeInSecs < 0)
            timeInSecs *= -1

        if (timeInSecs / 60 > 0) {
            val x = timeInSecs / 60
            if (x < 10)
                ret += "0$x"
            else
                ret += "" + x
            timeInSecs %= 60
        } else
            ret += "00"
        if (timeInSecs >= 0) {
            if (timeInSecs < 10)
                ret += "  :  0$timeInSecs"
            else
                ret += "  :  $timeInSecs"
        }

        return ret
    }

    fun getDrawable(context: Context, resId: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            context.resources.getDrawable(resId, null)
        else
            context.resources.getDrawable(resId)
    }

    fun logHashKey(activity: Activity) {
        try {
            val info = activity.packageManager.getPackageInfo(
                    "com.osellers", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val k = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("KeyHash:", k)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "showHashKey()", e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "showHashKey()", e)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //region Animation
    fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun willChangeBounds(): Boolean {
                return true
            }

            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }


        }

        // 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }
    //endregion

    //region Picture-utilities

    fun collapseWithout1dp(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms upto 1dp remains
        a.duration = ((initialHeight / v.context.resources.displayMetrics.density).toInt() - 1).toLong()
        v.startAnimation(a)
    }

    fun animateTextView(textview: TextView, initialValue: Int, finalValue: Int) {

        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnimator.duration = 1500

        valueAnimator.addUpdateListener { valueAnimator -> textview.text = valueAnimator.animatedValue.toString() }
        valueAnimator.start()

    }

    //endregion

    fun decodeFile(picFile: File, requiredSize: Int): Bitmap? {
        try {
            // decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            val stream1 = FileInputStream(picFile)
            BitmapFactory.decodeStream(stream1, null, o)
            stream1.close()

            // Find the correct scale value. It should be the power of 2.
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1

            while (true) {
                if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                    break
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }
            Log.i("SCALE", "scale = $scale")

            // decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val stream2 = FileInputStream(picFile)
            val bitmap = BitmapFactory.decodeStream(stream2, null, o2)
            stream2.close()
            return bitmap
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun getImageBase64Str(bitmap: Bitmap): String {
        val bao = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao)
        val ba = bao.toByteArray()
        return Base64.encodeToString(ba, Base64.DEFAULT)
    }

    fun getUnderlinedString(string: String): SpannableString {
        val content = SpannableString(string)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        return content
    }

    /*public static void fillImageWidth(Activity activity, String url, ImageView iv) {
        Bitmap bitmap = AqImgLoaderSingleton.getCachedBitmap(activity, url);
        if (bitmap != null)
            fillImageWidth(activity, bitmap, iv);
    }*/

    fun gotoPlayStorePage(context: Context) {
        val appPackageName = context.packageName // getPackageName() from Context or Activity object
        try {
            context.startActivity(
                    Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(
                    Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }

    }

    fun fillImageWidth(context: Context, bitmap: Bitmap, iv: ImageView) {
        val imgHeight = bitmap.height
        val imgWidth = bitmap.width
        val sw = getScreenWidth(context)// * 0.80);
        val h = imgHeight * sw / imgWidth
        val lp = iv.layoutParams
        lp.height = h
        lp.width = sw
        iv.layoutParams = lp
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyboardFromFragment(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getHTMLStr(source: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(source, Html.FROM_HTML_OPTION_USE_CSS_COLORS).toString()
        else
            Html.fromHtml(source).toString()
    }


    fun parseInt(numStr: String, defValue: Int): Int {
        try {
            return Integer.parseInt(numStr)
        } catch (nfe: NumberFormatException) {
            nfe.printStackTrace()
            return defValue
        }

    }

    fun isUserIdCorrect(etName: EditText?): Boolean {
        if (etName == null)
            return false
        val userName = etName.text.toString()
        if (userName.contains(" "))
            etName.error = "User-Id can't have any space."
        else if (!userName.matches("[a-zA-Z0-9]*".toRegex()))
            etName.error = "Only alphabets (a-z & A-Z) and numbers (0-9) are allowed."
        else
            return true
        etName.requestFocus()
        return false
    }

    @JvmStatic
    fun isStrEmpty(str: String?): Boolean {
        return str == null || str.isEmpty() || str.toLowerCase() == C.NULL_STR
    }

    fun ltrim(s: String): String {
        return LTRIM.matcher(s).replaceAll("")
    }

    fun rtrim(s: String): String {
        return RTRIM.matcher(s).replaceAll("")
    }

    fun readJSONFromAsset(context: Context, fileName: String): String? {
        var json: String? = null
        try {
            val `is` = context.assets.open(fileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun validateAnyEditText(ev: EditText, activity: Activity, msg: String): Boolean {
        var valid = true

        val str = ev.text.toString().trim { it <= ' ' }

        if (isStrEmpty(str)) {
            ev.error = msg
            requestFocus(ev, activity)
            valid = false
        } else {
            ev.error = null
        }

        return valid
    }



    /**Focusing the error message to the user*/
    fun requestFocus(view: View, activity: Activity) {
        if (view.requestFocus()) {
            activity.window.setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    fun calculatingCurrentTime(remTime: Int): String {
        val d = Date()
        d.time = d.time + remTime * 60 * 1000
        val sdf = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        return sdf.format(d)
    }

    fun calculatingPreviousDateTime(updateTime: String, requireTime: Int): String {
        val c = Calendar.getInstance(TimeZone.getDefault())
        val sdfFrom = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        sdfFrom.timeZone = TimeZone.getTimeZone("UTC")
        val sdfTo = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        try {
            c.time = sdfFrom.parse(updateTime)
            c.add(Calendar.MINUTE, requireTime)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return sdfTo.format(c.time)
    }

    fun showMsgToView(activity: Activity, v: EditText, msg: String) {
        if (v.requestFocus()) {
            v.error = msg
            activity.window.setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    fun showMsgToView( v: TextView,activity: Activity, msg: String) {
        if (v.requestFocus()) {
            v.error = msg
            activity.window.setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }


    fun deActivateBtn3Seconds(v: View, timeInMill: Int) {
        v.isEnabled = false
        Handler().postDelayed({ v.isEnabled = true }, timeInMill.toLong())
    }

    fun deactivateBtn3Seconds(v: View) {
        val timeInMill = 3000
        deActivateBtn3Seconds(v, timeInMill)
    }

    class ViewMeasure {
        var height: Int = 0
        var width: Int = 0
    }

    fun getViewHeightWidth(view: View): ViewMeasure {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY), View
                .MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY))
        val width = view.measuredWidth
        val height = view.measuredHeight
        val viewMeasure = ViewMeasure()
        viewMeasure.height = height
        viewMeasure.width = width
        return viewMeasure
    }

    fun decipherUId(s: String): String {
        val r = s.toCharArray()
        var i = 0
        var key = 0
        var result = ""

        while (i < r.size) {
            result += (r[i].toInt() - (3 + key)).toChar()
            i += 6
            key++
        }

        return result
    }

    @JvmStatic
    fun isAlphaOnly(data: String?): Boolean = !isStrEmpty(data)
            && (data?.matches("[a-zA-Z]+".toRegex()) ?: false)

    @JvmStatic
    fun isNumber(str: String?): Boolean = !isStrEmpty(str) && str?.matches("\\d+".toRegex()) ?: false

    @JvmStatic
    fun formattedInK(value: Double): String {
        val valueStr: String
        val containValue: Double
        if (value > 1000) {
            containValue = value / 1000
            valueStr = String.format(Locale.ENGLISH, "%.1fk", containValue)
        } else {
            valueStr = if (value > 0)
                String.format(Locale.ENGLISH, "%.1f", value)
            else "0"
        }

        return valueStr
    }

    fun getDeviceUniqueID(context: Context) = Settings.Secure.ANDROID_ID



    /**
     * Convert a xml view to bitmap
     * [v] Recieves a XML layout view*/
    fun convertViewToBitmap(v: View): Bitmap {
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.measuredWidth, v.measuredHeight);
        val bitmap = Bitmap.createBitmap(v.measuredWidth,
                v.measuredHeight,
                Bitmap.Config.ARGB_8888);

        val c = Canvas(bitmap);
        v.layout(v.left, v.top, v.right, v.bottom);
        v.draw(c);
        return bitmap
    }

    fun gotoAppSettings(context: Context) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    }

    fun formatNumberUsingComma(n: Int): String {
        val formatter = DecimalFormat("#,##,##,###")
        return formatter.format(n)
    }

    fun newLineBreakForTextArea(str: String) = str.replace("\\n".toRegex(), "<br />")

    fun formattingTimeForTimePickerDialog(h: Int, m: Int): String {
        var hr = ""
        var min = ""

        hr = when (h) {
            in 1..9 -> "0$h"
            in 13..24 -> String.format(if ((h - 12) < 10) "0${(h - 12)}" else "${h - 12}")
            else -> "$h"
        }
        min = when (m) {
            in 1..9 -> "0$m"
            else -> "$m"
        }


        return String.format("$hr:$min ${if (h > 11) "PM" else "AM"}")
    }

    fun convert24HrTo12HrFormatTime(input: String): String {
        //Date/time pattern of input date
        val df = SimpleDateFormat("HH:mm:ss", Locale.US)
        //Date/time pattern of desired output date
        val outputformat = SimpleDateFormat("hh:mm aa", Locale.US)

        try {
            //Conversion of input String to date
            val date = df.parse(input);
            //old date format to new date format
            val output = outputformat.format(date);

            return output
        } catch (pe: ParseException) {
            pe.printStackTrace()
            return ""
        }
    }

    fun getTimeInCalendarFormat(input: String): Calendar? {
        //Date/time pattern of input date
        val df = SimpleDateFormat("HH:mm:ss", Locale.US)

        try {
            //Conversion of input String to date
            val date = df.parse(input)

            val cal = Calendar.getInstance()
            cal.time = date

            return cal
        } catch (pe: ParseException) {
            pe.printStackTrace()
            return null
        }
    }

    fun isUrlValid(url: String): Boolean {
        return try {
            URL(url).toURI()
            true
        } catch (e: URISyntaxException) {
            false
        } catch (e: MalformedURLException) {
            false
        }
    }


}

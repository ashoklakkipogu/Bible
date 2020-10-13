package com.ashok.bible.utils

import android.R.id.message
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.ClipData
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashok.bible.common.AppConstants
import com.ashok.bible.data.local.BibleDatabase
import com.ashok.bible.ui.MainActivity
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {

        fun gridRecyclerView(recyclerView: RecyclerView, activity: Context?, spanCount: Int) {
            recyclerView.layoutManager = GridLayoutManager(activity, spanCount)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.setHasFixedSize(false)
        }

        fun verticalRecyclerView(recyclerView: RecyclerView, activity: Context?) {
            recyclerView.setLayoutManager(LinearLayoutManager(activity))
        }


        fun copyAttachedDatabase(activity: Activity, lan: String) {
            val dbPath: File =
                activity.getDatabasePath(
                    BibleDatabase.DATABASE
                )
            if (dbPath.exists()) {
                return
            }
            dbPath.parentFile.mkdirs()
            try {
                val inputStream: InputStream =
                    activity.getResources()
                        .openRawResource(
                            activity.resources.getIdentifier(
                                lan,
                                "raw",
                                activity.packageName
                            )
                        )
                val output: OutputStream = FileOutputStream(dbPath)
                val buffer = ByteArray(8192)
                var length: Int
                while (inputStream.read(buffer, 0, 8192).also { length = it } > 0) {
                    output.write(buffer, 0, length)
                }
                output.flush()
                output.close()
                inputStream.close()
            } catch (e: IOException) {
                Log.d("Utils", "Failed to open file", e)
                e.printStackTrace()
            }
        }

        fun getStringTimeStampWithDate(): String {
            val data = Date()
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.getDefault()
            )
            //dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormat.format(data)
        }

        public fun getUtcToDDMMYYYYHHMMA(utcDate: String): String {
            var outputDate = ""
            try {
                if (utcDate.isNotEmpty()) {
                    val date =
                        SimpleDateFormat(AppConstants.DATE_FORMAT_yyyy_MM_dd_HH_mm_ss_SSS_Z).parse(
                            utcDate
                        )
                    outputDate =
                        SimpleDateFormat(AppConstants.DATE_FORMAT_dd_MM_yyyy_hh_mm_a).format(date)
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return outputDate
        }

        fun goToPlayStore(activity: Activity) {
            val playStoreMarketUrl = "market://details?id="
            val playStoreWebUrl = "https://play.google.com/store/apps/details?id="
            val packageName: String = activity.packageName
            try {
                var intent: Intent =
                    activity.packageManager.getLaunchIntentForPackage("com.android.vending")!!
                if (intent != null) {
                    val androidComponent = ComponentName(
                        "com.android.vending",
                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"
                    )
                    intent.component = androidComponent
                    intent.data = Uri.parse(playStoreMarketUrl + packageName)
                } else {
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(playStoreMarketUrl + packageName)
                    )
                }
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(playStoreWebUrl + packageName))
                activity.startActivity(intent)
            }
        }

        fun shareApp(activity: Activity) {
            try {
                ShareCompat.IntentBuilder.from(activity)
                    .setType("text/plain")
                    .setChooserTitle("Chooser title")
                    .setText("http://play.google.com/store/apps/details?id=" + activity.packageName)
                    .startChooser();

            } catch (e: Exception) { //e.toString();
            }


        }

        fun sendMail(activity: Activity) {
            try {
                val email = Intent(Intent.ACTION_SEND)
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf(AppConstants.EMAIL))
                email.putExtra(Intent.EXTRA_SUBJECT, AppConstants.SUBJECT)
                email.putExtra(Intent.EXTRA_TEXT, message)
                email.type = "message/rfc822"
                activity.startActivity(Intent.createChooser(email, "Choose an Email client :"))

            } catch (e: Exception) { //e.toString();
            }
        }


        fun deleteDb(activity: Activity, selectedLan: String, pref: SharedPreferences) {
            SharedPrefUtils.setLanguage(pref, selectedLan)
            val isDeleted = activity.deleteDatabase(BibleDatabase.DATABASE)
            if (isDeleted) {
                copyAttachedDatabase(activity, selectedLan)
                triggerRebirth(activity)
            }
        }

        fun triggerRebirth(context: Context) {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = intent!!.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }

        fun shareText(context: Context?, str: String) {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, str)
            sendIntent.type = "text/plain"
            context?.startActivity(sendIntent)

        }

        fun copyText(context: Context?, str: String) {
            val clipboard =
                context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", str)
            clipboard!!.setPrimaryClip(clip)
        }

        fun customEvent(
            firebaseAnalytics: FirebaseAnalytics,
            pref: SharedPreferences,
            book: Int,
            chapter: Int,
            verseCount: Int,
            verse: String,
            bibleId: Int,
            logEvent: String
        ) {
            val params = Bundle()
            params.putString(AppConstants.USER_NAME, SharedPrefUtils.getUserName(pref))
            params.putString(SharedPrefUtils.DEVICE_NAME, SharedPrefUtils.getDeviceName(pref))
            params.putString(SharedPrefUtils.ID, SharedPrefUtils.getId(pref))
            params.putInt(AppConstants.BOOK, book)
            params.putInt(AppConstants.CHAPTER, chapter)
            params.putInt(AppConstants.VERSECOUNT, verseCount)
            params.putString(AppConstants.VERSE, verse)
            params.putInt(AppConstants.BIBLE_ID, bibleId)
            firebaseAnalytics.logEvent(logEvent, params)

        }
        fun getCurrentTime(): String {
            val myCalendar = Calendar.getInstance()
            val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            return utcFormat.format(myCalendar.time)

        }
    }

    fun customEvent(
        firebaseAnalytics: FirebaseAnalytics,
        pref: SharedPreferences
    ) {
        val params = Bundle()
        params.putString(AppConstants.USER_NAME, SharedPrefUtils.getUserName(pref))
        params.putString(SharedPrefUtils.DEVICE_NAME, SharedPrefUtils.getDeviceName(pref))
        params.putString(SharedPrefUtils.ID, SharedPrefUtils.getId(pref))
        firebaseAnalytics.logEvent(AppConstants.VERSION, params)

    }


}
package com.gadgetsfolk.kidsgk.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import com.gadgetsfolk.kidsgk.BuildConfig
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.helper.HelperMethods.isNetworkAvailable
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import android.content.pm.PackageManager

import android.content.pm.PackageInfo

import android.R.attr.versionCode

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        changeStatusBarColor() //Change Status Bar Color -> Transparent
        setContentView(R.layout.activity_splash)

        try {
            val pInfo: PackageInfo =
                this.packageManager.getPackageInfo(this.packageName, 0)
            val versionCode: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toInt() // avoid huge version numbers and you will be ok
            } else {
                pInfo.versionCode
            }
            //val versionCode = pInfo.longVersionCode
            versionCodeApp = versionCode.toString()
            if (isNetworkAvailable(this@SplashActivity)) checkVersionCode()
            else Toast.makeText(this@SplashActivity, "Please check your Internet connection", Toast.LENGTH_SHORT).show()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }



    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun checkVersionCode() {
        FirebaseFirestore.getInstance().collection("version_code")
                .document("version_code")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    versionCode = documentSnapshot["version_code"].toString()
                    if (versionCode.equals(versionCodeApp, ignoreCase = true)) {
                        val SPLASH_TIME_OUT = 3000
                        Handler().postDelayed({ //This method will be executed once the timer is over
                            //Start your app main activity
                            val intent = Intent(this@SplashActivity, BottomActivity::class.java)
                            startActivity(intent)
                            //Close the activity
                            finish()
                        }, SPLASH_TIME_OUT.toLong())
                    } else {
                        val intent = Intent(this@SplashActivity, UpdateVersionActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { e -> Toast.makeText(this@SplashActivity, e.message, Toast.LENGTH_SHORT).show() }
    }

    companion object {
        private var versionCodeApp: String? = null
        private var versionCode: String? = null
    }
}
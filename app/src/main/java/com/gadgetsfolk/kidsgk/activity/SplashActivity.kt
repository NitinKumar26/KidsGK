package com.gadgetsfolk.kidsgk.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyAppOptions
import com.gadgetsfolk.kidsgk.BuildConfig
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.helper.HelperMethods.isNetworkAvailable
import com.google.ads.mediation.adcolony.AdColonyAdapterUtils
import com.google.ads.mediation.adcolony.AdColonyMediationAdapter
import com.google.ads.mediation.unity.UnityMediationAdapter
import com.google.android.gms.ads.AdFormat
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.mediation.InitializationCompleteCallback
import com.google.android.gms.ads.mediation.MediationConfiguration
import com.google.firebase.firestore.FirebaseFirestore
import com.unity3d.ads.metadata.MetaData
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        changeStatusBarColor() //Change Status Bar Color -> Transparent
        setContentView(R.layout.activity_splash)

        //GDPR consent for Unity Personalized Ads
        val metaData = MetaData(this)
        metaData["gdpr.consent"] = true
        metaData.commit()
        MobileAds.initialize(this@SplashActivity, getString(R.string.admob_app_id))

        //GDPR Consent for AdColony Personalized Ads
        val appOptions = AdColonyMediationAdapter.getAppOptions()
        appOptions.setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true)
        appOptions.setPrivacyConsentString(AdColonyAppOptions.GDPR, "1")
        appOptions.keepScreenOn = true

        AdColony.configure(this@SplashActivity, appOptions,
                getString(R.string.adcolony_app_id),
                getString(R.string.adcolony_interstitial),
                getString(R.string.adcolony_banner))
        val bundleInterstitial = Bundle()
        bundleInterstitial.putString(AdColonyAdapterUtils.KEY_APP_ID, getString(R.string.adcolony_app_id))
        bundleInterstitial.putString(AdColonyAdapterUtils.KEY_ZONE_ID, getString(R.string.adcolony_interstitial))

        val bundleBanner = Bundle()
        bundleBanner.putString(AdColonyAdapterUtils.KEY_APP_ID, getString(R.string.adcolony_app_id))
        bundleBanner.putString(AdColonyAdapterUtils.KEY_ZONE_ID, getString(R.string.adcolony_banner))

        val adColonyMediationAdapter = AdColonyMediationAdapter()

        val config: MutableList<MediationConfiguration> = ArrayList()
        config.add(MediationConfiguration(AdFormat.INTERSTITIAL, bundleInterstitial))
        config.add(MediationConfiguration(AdFormat.BANNER, bundleBanner))

        adColonyMediationAdapter.initialize(this@SplashActivity, object : InitializationCompleteCallback {
            override fun onInitializationSucceeded() {}
            override fun onInitializationFailed(s: String) {
                Log.e("adColonyInit", s)
            }
        }, config)

        val unityInterstitial = Bundle()
        unityInterstitial.putString("gameId", getString(R.string.unity_game_id))
        unityInterstitial.putString("zoneId", getString(R.string.unity_interstitial))

        val unityBanner = Bundle()
        unityBanner.putString("gameId", getString(R.string.unity_game_id))
        unityBanner.putString("zoneId", getString(R.string.unity_banner))

        val unityConfig: MutableList<MediationConfiguration> = ArrayList()
        unityConfig.add(MediationConfiguration(AdFormat.INTERSTITIAL, unityInterstitial))
        unityConfig.add(MediationConfiguration(AdFormat.BANNER, unityBanner))

        val adapter = UnityMediationAdapter()
        adapter.initialize(this, object : InitializationCompleteCallback {
            override fun onInitializationSucceeded() {}
            override fun onInitializationFailed(s: String) {
                Log.e("unityInit", s)
            }
        }, unityConfig)


        //BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE
        versionCodeApp = versionCode.toString()
        if (isNetworkAvailable(this@SplashActivity)) checkVersionCode()
        else Toast.makeText(this@SplashActivity, "Please check your Internet connection", Toast.LENGTH_SHORT).show()

        if (BuildConfig.DEBUG) {
            val configuration = RequestConfiguration.Builder().setTestDeviceIds(listOf("69D4E189C8A6E5F18028F747691D0BC6")).build()
            MobileAds.setRequestConfiguration(configuration)
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
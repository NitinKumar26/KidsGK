package com.gadgetsfolk.kidsgk.app

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.utility.AppOpenManager
import com.google.ads.mediation.unity.UnityMediationAdapter
import com.google.android.gms.ads.AdFormat
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.mediation.InitializationCompleteCallback
import com.google.android.gms.ads.mediation.MediationConfiguration
import com.unity3d.ads.metadata.MetaData
import java.util.*

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {
            RequestConfiguration.Builder().setTestDeviceIds(listOf("5DEFCFD375E7F4B45F46448F1CDC425B"))
        }

        var appOpenManager:AppOpenManager? = AppOpenManager(this)

        //GDPR consent for Unity Personalized Ads
        val metaData = MetaData(this)
        metaData[getString(R.string.gdpr_consent)] = true
        metaData.commit()

        val unityInterstitial = Bundle()
        unityInterstitial.putString(getString(R.string.game_id), getString(R.string.unity_game_id))
        unityInterstitial.putString(getString(R.string.zone_id), getString(R.string.unity_interstitial))

        val unityBanner = Bundle()
        unityBanner.putString(getString(R.string.game_id), getString(R.string.unity_game_id))
        unityBanner.putString(getString(R.string.zone_id), getString(R.string.unity_banner))

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

    }
}
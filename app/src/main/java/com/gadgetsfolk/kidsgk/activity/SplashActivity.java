package com.gadgetsfolk.kidsgk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.gadgetsfolk.kidsgk.BuildConfig;
import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.google.ads.mediation.adcolony.AdColonyAdapterUtils;
import com.google.ads.mediation.adcolony.AdColonyMediationAdapter;
import com.google.ads.mediation.unity.UnityMediationAdapter;
import com.google.android.gms.ads.AdFormat;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unity3d.ads.metadata.MetaData;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static String versionCodeApp;
    private static String versionCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor(); //Change Status Bar Color -> Transparent
        setContentView(R.layout.activity_splash);



        //GDPR consent for Unity Personalized Ads
        MetaData metaData = new MetaData(this);
        metaData.set("gdpr.consent", true);
        metaData.commit();

        MobileAds.initialize(SplashActivity.this, getString(R.string.admob_app_id));

        //GDPR Consent for AdColony Personalized Ads
        AdColonyAppOptions appOptions = AdColonyMediationAdapter.getAppOptions();
        appOptions.setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true);
        appOptions.setPrivacyConsentString(AdColonyAppOptions.GDPR, "1");
        appOptions.setKeepScreenOn(true);

        AdColony.configure(SplashActivity.this, appOptions,
                getString(R.string.adcolony_app_id),
                getString(R.string.adcolony_interstitial),
                getString(R.string.adcolony_banner));

        Bundle bundleInterstitial = new Bundle();
        bundleInterstitial.putString(AdColonyAdapterUtils.KEY_APP_ID, getString(R.string.adcolony_app_id));
        bundleInterstitial.putString(AdColonyAdapterUtils.KEY_ZONE_ID, getString(R.string.adcolony_interstitial));

        Bundle bundleBanner = new Bundle();
        bundleBanner.putString(AdColonyAdapterUtils.KEY_APP_ID, getString(R.string.adcolony_app_id));
        bundleBanner.putString(AdColonyAdapterUtils.KEY_ZONE_ID, getString(R.string.adcolony_banner));

        AdColonyMediationAdapter adColonyMediationAdapter = new AdColonyMediationAdapter();
        List<MediationConfiguration> config = new ArrayList<>();

        config.add(new MediationConfiguration(AdFormat.INTERSTITIAL, bundleInterstitial));
        config.add(new MediationConfiguration(AdFormat.BANNER, bundleBanner));

        adColonyMediationAdapter.initialize(SplashActivity.this, new InitializationCompleteCallback() {
            @Override
            public void onInitializationSucceeded() {}
            @Override
            public void onInitializationFailed(String s) { Log.e("adColonyInit", s); }
        }, config);

        Bundle unityInterstitial = new Bundle();
        unityInterstitial.putString("gameId", getString(R.string.unity_game_id));
        unityInterstitial.putString("zoneId", getString(R.string.unity_interstitial));

        Bundle unityBanner = new Bundle();
        unityBanner.putString("gameId", getString(R.string.unity_game_id));
        unityBanner.putString("zoneId", getString(R.string.unity_banner));

        List<MediationConfiguration> unityConfig = new ArrayList<>();
        unityConfig.add(new MediationConfiguration(AdFormat.INTERSTITIAL, unityInterstitial));
        unityConfig.add(new MediationConfiguration(AdFormat.BANNER, unityBanner));

        UnityMediationAdapter adapter  = new UnityMediationAdapter();
        adapter.initialize(this, new InitializationCompleteCallback() {
            @Override
            public void onInitializationSucceeded() {}
            @Override
            public void onInitializationFailed(String s) { Log.e("unityInit", s); }
        }, unityConfig);


        //BuildConfig.VERSION_NAME
        int versionCode = BuildConfig.VERSION_CODE;
        versionCodeApp = String.valueOf(versionCode);

        if (HelperMethods.isNetworkAvailable(SplashActivity.this))
            checkVersionCode();
        else
            Toast.makeText(SplashActivity.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void checkVersionCode(){
        FirebaseFirestore.getInstance().collection("version_code")
                .document("version_code")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                versionCode = String.valueOf(documentSnapshot.get("version_code"));
                if (versionCode.equalsIgnoreCase(versionCodeApp)){
                    int SPLASH_TIME_OUT = 3000;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //This method will be executed once the timer is over
                            //Start your app main activity
                            Intent intent = new Intent(SplashActivity.this, BottomActivity.class);
                            startActivity(intent);
                            //Close the activity
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }else{
                    Intent intent = new Intent(SplashActivity.this, UpdateVersionActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

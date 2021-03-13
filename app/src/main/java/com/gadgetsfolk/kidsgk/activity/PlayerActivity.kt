package com.gadgetsfolk.kidsgk.activity

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.gadgetsfolk.kidsgk.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class PlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private var videoID: String? = null
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_player)
        // YouTube player view
        val youtubeView: YouTubePlayerView = findViewById(R.id.youtube_view)

        // Initializing video player with developer key
        youtubeView.initialize(getString(R.string.api_key), this)
        videoID = intent.getStringExtra("videoID")

        InterstitialAd.load(this, getString(R.string.interstitial_ad_id), AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    //Log.i(TAG, "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    //Log.i(TAG, loadAdError.message)
                    mInterstitialAd = null
                }
            })
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
        if (!b) {
            youTubePlayer.loadVideo(videoID)
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(getString(R.string.error_player), youTubeInitializationResult.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mInterstitialAd != null) mInterstitialAd?.show(this)
        else Log.d("TAG: InerstitialError", "The interstitial ad wasn't ready yet.")
    }

    companion object {
        private const val RECOVERY_DIALOG_REQUEST = 1
    }
}
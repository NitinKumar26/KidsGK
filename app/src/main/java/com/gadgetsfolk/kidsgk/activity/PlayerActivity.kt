package com.gadgetsfolk.kidsgk.activity

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.gadgetsfolk.kidsgk.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
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
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = this@PlayerActivity.resources.getString(R.string.vid_interstitial_ad_id)
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
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
        if (mInterstitialAd!!.isLoaded) {
            mInterstitialAd!!.show()
        }
    }

    companion object {
        private const val RECOVERY_DIALOG_REQUEST = 1
    }
}
package com.gadgetsfolk.kidsgk.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.databinding.ActivityScoreBinding
import com.gadgetsfolk.kidsgk.model.Score
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.LoadAdError

class ScoreActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private var mNativeAd: NativeAd? = null

    private lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScoreBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val score: Score? = intent.getParcelableExtra("score_obj")

        if (score != null) {
            binding.tvCorrect.text = score.correct_questions
            binding.tvTotal.text = score.total_questions
            binding.tvYouHave.text = getString(R.string.tv_correct, score.correct_questions)
            binding.tvOutOf.text = getString(R.string.tv_out_of, score.total_questions)
            binding.tvRemark.text = score.remark
        }

        QuizActivity.questionsAttempted = 0
        QuizActivity.quizScore = 0
        QuizActivity.score = Score()

        InterstitialAd.load(this, getString(R.string.interstitial_ad_id), AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    //Log.i(TAG, "onAdLoaded")
                    mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            //Log.d(TAG, 'Ad was dismissed.')
                            binding.btnPlayAgain.setOnClickListener {
                                val intent = Intent(this@ScoreActivity, BottomActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                            //Log.d(TAG, 'Ad failed to show.')
                        }

                        override fun onAdShowedFullScreenContent() {
                            //Log.d(TAG, 'Ad showed fullscreen content.')
                            mInterstitialAd = null;
                        }
                    }

                    binding.btnPlayAgain.setOnClickListener {
                        mInterstitialAd!!.show(this@ScoreActivity)
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    //Log.i(TAG, loadAdError.message)
                    mInterstitialAd = null
                    binding.btnPlayAgain.setOnClickListener {
                        val intent = Intent(this@ScoreActivity, BottomActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            })

        refreshAd()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mInterstitialAd != null) mInterstitialAd?.show(this)
        else
            Log.d("TAG: InerstitialError", "The interstitial ad wasn't ready yet.")
    }

    /**
     * Populates a [NativeAdView] object with data from a given
     * [NativeAd].
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView the view to be populated
     */
    private fun populateUnifiedNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
        adView.mediaView = adView.findViewById<View>(R.id.ad_media) as MediaView

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) adView.bodyView.visibility = View.INVISIBLE
        else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) adView.callToActionView.visibility = View.INVISIBLE
        else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) adView.iconView.visibility = View.GONE else {
            (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) adView.priceView.visibility = View.INVISIBLE else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) adView.storeView.visibility = View.INVISIBLE else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) adView.starRatingView.visibility = View.INVISIBLE else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) adView.advertiserView.visibility = View.INVISIBLE else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private fun refreshAd() {
        //refresh.setEnabled(false);
        val builder = AdLoader.Builder(this, getString(R.string.native_ad_unit))
        builder.forNativeAd(NativeAd.OnNativeAdLoadedListener { nativeAd ->
            //OnNativeAdLoadedListener implementation.
            //If this callback occurs after the activity is destroyed, you must call
            //destroy and return or you may get a memory leak.
            if (isDestroyed) {
                mNativeAd?.destroy()
                return@OnNativeAdLoadedListener
            }
            //You must call destroy on old ads when you are done with them,
            //otherwise you will have a memory leak
            mNativeAd?.destroy()
            mNativeAd = nativeAd
            val frameLayout = findViewById<FrameLayout>(R.id.fl_adplaceholder)
            val adView = layoutInflater.inflate(R.layout.ad_unified, null) as NativeAdView
            populateUnifiedNativeAdView(nativeAd, adView)
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
        })

        val videoOptions = VideoOptions.Builder().build()
        val adOptions = com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.withAdListener(object: AdListener(){
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                Log.e("nativeAdError", p0?.message?: "")
                super.onAdFailedToLoad(p0)
            }
        }
        ).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }
}
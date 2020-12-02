package com.gadgetsfolk.kidsgk.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.model.Score
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import kotlinx.android.synthetic.main.activity_score.*

class ScoreActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private var nativeAd: UnifiedNativeAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val score: Score? = intent.getParcelableExtra("score_obj")
        if (score != null) {
            tv_correct.text = score.correct_questions
            tv_total.text = score.total_questions
            tv_you_have.text = getString(R.string.tv_correct, score.correct_questions)
            tv_out_of.text = getString(R.string.tv_out_of, score.total_questions)
            tv_remark.text = score.remark
        }
        QuizActivity.questionsAttempted = 0
        QuizActivity.quizScore = 0
        QuizActivity.score = Score()
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = this@ScoreActivity.resources.getString(R.string.interstitial_ad_id)
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
        mInterstitialAd!!.adListener = adListener

        refreshAd()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
    }

    private var adListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            btn_play_again.setOnClickListener { mInterstitialAd!!.show() }
        }

        override fun onAdClosed() {
            btn_play_again.setOnClickListener {
                val intent = Intent(this@ScoreActivity, BottomActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        override fun onAdFailedToLoad(i: Int) {
            btn_play_again.setOnClickListener {
                val intent = Intent(this@ScoreActivity, BottomActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    /**
     * Populates a [UnifiedNativeAdView] object with data from a given
     * [UnifiedNativeAd].
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
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
        if (nativeAd.body == null) adView.bodyView.visibility = View.INVISIBLE else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) adView.callToActionView.visibility = View.INVISIBLE else {
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

        /*
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            //videoStatus.setText(String.format(Locale.getDefault(), "Video status: Ad contains a %.2f:1 video asset.", vc.getAspectRatio()));

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    //refresh.setEnabled(true);
                    //videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        }
        else {
            //videoStatus.setText("Video status: Ad does not contain a video asset.");
            //refresh.setEnabled(true);
        }
         */
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private fun refreshAd() {
        //refresh.setEnabled(false);
        val builder = AdLoader.Builder(this, getString(R.string.native_ad_unit))
        builder.forUnifiedNativeAd(OnUnifiedNativeAdLoadedListener { unifiedNativeAd ->
            // OnUnifiedNativeAdLoadedListener implementation.
            // If this callback occurs after the activity is destroyed, you must call
            // destroy and return or you may get a memory leak.
            if (isDestroyed) {
                unifiedNativeAd.destroy()
                return@OnUnifiedNativeAdLoadedListener
            }
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (nativeAd != null) {
                nativeAd!!.destroy()
            }
            nativeAd = unifiedNativeAd
            val frameLayout = findViewById<FrameLayout>(R.id.fl_adplaceholder)
            val adView = layoutInflater.inflate(R.layout.ad_unified, null) as UnifiedNativeAdView
            populateUnifiedNativeAdView(unifiedNativeAd, adView)
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
        })
        val videoOptions = VideoOptions.Builder().build()
        val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                //refresh.setEnabled(true);
                /*
                                        String error =
                                                String.format(
                                                        "domain: %s, code: %d, message: %s",
                                                        loadAdError.getDomain(),
                                                        loadAdError.getCode(),
                                                        loadAdError.getMessage());
                                         */
                Toast.makeText(this@ScoreActivity, "Failed to load native ad", Toast.LENGTH_SHORT).show()
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())

        //videoStatus.setText("");
    }
}
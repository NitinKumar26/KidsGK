package com.gadgetsfolk.kidsgk.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gadgetsfolk.kidsgk.R
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_update_version.*

class UpdateVersionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_version)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        update_now_button.setOnClickListener{
            updateNow()
        }
    }


    fun updateNow() {
        val intent = Intent("android.intent.action.VIEW",
                Uri.parse("https://play.google.com/store/apps/details?id=com.gadgetsfolk.kidsgk"))
        startActivity(intent)
    }

}
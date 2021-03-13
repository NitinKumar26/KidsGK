package com.gadgetsfolk.kidsgk.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gadgetsfolk.kidsgk.databinding.ActivityUpdateVersionBinding
import com.google.android.gms.ads.AdRequest

class UpdateVersionActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUpdateVersionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateVersionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.updateNowButton.setOnClickListener{
            updateNow()
        }
    }

    fun updateNow() {
        val intent = Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.gadgetsfolk.kidsgk"))
        startActivity(intent)
    }

}
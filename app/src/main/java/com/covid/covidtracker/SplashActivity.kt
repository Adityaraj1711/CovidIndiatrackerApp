package com.covid.covidtracker

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.WindowManager
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    lateinit var versionTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        bindViews()

        try{
            versionTV.setText("V " + packageManager.getPackageInfo(packageName, 0).versionName)
        }
        catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
        // will provide thread. The intent will run in 2500ms
        Handler().postDelayed(object : Runnable {
            override fun run() {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }, 2500 )
    }

    private fun bindViews() {
        versionTV = findViewById(R.id.version)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}



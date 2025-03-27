package com.example.favdish

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.favdish.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var splashBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)//Calls the parent on create function to set up some basics things
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)//Set the UI content using view binding

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){//checks if the version is above Android 11+
            window.insetsController?.hide(WindowInsets.Type.statusBars())//hide status bar
        }else{//For Pre_Android 11+
            window.setFlags(//hide status bar // setFlags(int flags, int mask)
                WindowManager.LayoutParams.FLAG_FULLSCREEN, // flags = The flag to be set (FLAG_FULLSCREEN).
                WindowManager.LayoutParams.FLAG_FULLSCREEN // masks = Specifies which flag(s) should be affected.
                 )//Passing the same flag twice ensures that only the FLAG_FULLSCREEN flag is updated.
        }*/
        //improved version of above commented code
        //this if else is for support backward compatibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE //  temporarily appear when the user swipes from the edges.
            }
        } else {
            // @Suppress("DEPRECATION") suppresses the warning that systemUiVisibility is deprecated in API 30+.
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN // hides the status bar
        }


    }

}
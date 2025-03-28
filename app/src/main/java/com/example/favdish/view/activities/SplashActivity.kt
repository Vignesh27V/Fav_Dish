package com.example.favdish.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.favdish.R
import com.example.favdish.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var splashBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)//Calls the parent on create function to set up some basics things
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)//Set the UI content using view binding

        /*  HIDING STATUS BAR CODE STARTS  */

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
        /*  HIDING STATUS BAR CODE ENDS  */

        /*  IMPLEMENTATION OF ANIMATION CODE STARTS  */

        val splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        splashBinding.tvAppName.animation = splashAnimation
        splashAnimation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                //TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animation?) { // At the end of animation it will start main activity with 1sec delay
                Handler(Looper.getMainLooper()).postDelayed({
                    /* Handler used to schedule or execute a code in specific thread.
                    * (Looper.getMainLooper()) returns the main thread looper which also known as UI thread.
                    * PostDelayed - delays the block of code inside it with the define time in milli seconds. Here it is 1000 milli sec which means 1sec
                    * */
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    // Intent is a messaging object used to request an action or service
                    finish()
                    // it closes the current activity and it ensure that when user click back btn, they don't return to the splash activity
                },1000)// Handler gives an option to handle things with a delay.
            }

            override fun onAnimationRepeat(animation: Animation?) {
                //TODO("Not yet implemented")
            }

        })

        /*  IMPLEMENTATION OF ANIMATION CODE ENDS  */

    }

}
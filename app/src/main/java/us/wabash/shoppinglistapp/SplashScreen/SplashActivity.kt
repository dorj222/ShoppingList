package us.wabash.shoppinglistapp.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import us.wabash.shoppinglistapp.R
import us.wabash.shoppinglistapp.ScrollingActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    val anim = AnimationUtils.loadAnimation(this, R.anim.splash_animation)

    anim.setAnimationListener(object: Animation.AnimationListener{
        override fun onAnimationRepeat(animation: Animation?) {
        }
        override fun onAnimationEnd(animation: Animation?) {
        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3000)

                    val intent = Intent(baseContext, ScrollingActivity::class.java)
                    startActivity(intent)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()

                }
        override fun onAnimationStart(animation: Animation?) {
        }
    })
        splashImage.startAnimation(anim)
    }

    }





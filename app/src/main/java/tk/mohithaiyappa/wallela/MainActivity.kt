package tk.mohithaiyappa.wallela

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import tk.mohithaiyappa.wallela.ui.WallelaActivity

class MainActivity : AppCompatActivity() {
    private var decorView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        setContentView(R.layout.activity_main)
        val animationView = findViewById<LottieAnimationView>(R.id.animation_view)
        animationView.imageAssetsFolder = "images"
        animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                val handler = Handler()
                handler.postDelayed({
                    val intent = Intent(this@MainActivity, WallelaActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                }, 2000)
                showSystemUI()
                freeMemory()
            }

            override fun onAnimationCancel(animation: Animator) {
                val handler = Handler()
                handler.postDelayed({
                    val intent = Intent(this@MainActivity, WallelaActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                }, 2000)
                showSystemUI()
                freeMemory()
            }

            override fun onAnimationRepeat(animation: Animator) {}
        })
        animationView.playAnimation()
    }

    private fun hideSystemUI() {
        decorView = window.decorView
        decorView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    private fun showSystemUI() {
        decorView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    private fun freeMemory() {
        System.runFinalization()
        Runtime.getRuntime().gc()
        System.gc()
    }
}
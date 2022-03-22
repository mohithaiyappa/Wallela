package tk.mohithaiyappa.wallela.ui.splash

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tk.mohithaiyappa.wallela.databinding.FragSplashScreenBinding
import tk.mohithaiyappa.wallela.navigation.NavigatorImpl
import tk.mohithaiyappa.wallela.ui.WallelaActivity

class SplashFragment : Fragment() {

    private var decorView: View? = null
    private var binding: FragSplashScreenBinding? = null
    private var navigator: NavigatorImpl? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navigator = NavigatorImpl(requireActivity() as WallelaActivity)
        binding = FragSplashScreenBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSystemUI()
        //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        //setContentView(R.layout.activity_main)


        binding!!.animationView.imageAssetsFolder = "images"
        binding!!.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                val handler = Handler()
                handler.postDelayed({
                    navigator!!.gotoHomeFragment()
                    //todo
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    //finish()
                }, 2000)
                showSystemUI()
                freeMemory()
            }

            override fun onAnimationCancel(animation: Animator) {
                val handler = Handler()
                handler.postDelayed({
                    navigator!!.gotoHomeFragment()
                    //todo
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    //finish()
                }, 2000)
                showSystemUI()
                freeMemory()
            }

            override fun onAnimationRepeat(animation: Animator) {}
        })
        binding!!.animationView.playAnimation()
    }

    private fun hideSystemUI() {

        decorView = requireActivity().window.decorView
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
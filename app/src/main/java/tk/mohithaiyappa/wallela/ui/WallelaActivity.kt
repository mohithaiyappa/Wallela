package tk.mohithaiyappa.wallela.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tk.mohithaiyappa.wallela.ui.splash.SplashFragment
import tk.mohithaiyappa.wallela.ui.home.HomeFragment
import tk.mohithaiyappa.wallela.databinding.ActivityWallelaBinding

class WallelaActivity : AppCompatActivity() {

    var binding: ActivityWallelaBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallelaBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportFragmentManager
            .beginTransaction()
            .add(binding!!.root.id, SplashFragment())
            .commit()
    }

    fun gotoHomeFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(binding!!.root.id, HomeFragment())
            .commit()
    }
}
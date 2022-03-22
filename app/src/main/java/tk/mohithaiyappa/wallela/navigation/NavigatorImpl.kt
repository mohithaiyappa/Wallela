package tk.mohithaiyappa.wallela.navigation

import androidx.navigation.Navigation
import androidx.navigation.findNavController
import tk.mohithaiyappa.wallela.R
import tk.mohithaiyappa.wallela.data.UrlDataStorage
import tk.mohithaiyappa.wallela.ui.WallelaActivity
import tk.mohithaiyappa.wallela.ui.home.HomeFragmentDirections
import tk.mohithaiyappa.wallela.ui.splash.SplashFragmentDirections

class NavigatorImpl(
    private val activity: WallelaActivity
):Navigator{

    override fun gotoHomeFragment() {
        val action = SplashFragmentDirections.navigateToHomeFragment()
        activity.findNavController( R.id.fragment).navigate(action)
    }

    override fun gotoFullScreenFragment(data: UrlDataStorage) {
        val action = HomeFragmentDirections.navigateToFullScreenFragment(data)
        activity.findNavController( R.id.fragment).navigate(action)
    }

    override fun gotoContactUsFragment() {
        val action = HomeFragmentDirections.navigateToContactUsFragment()
        activity.findNavController( R.id.fragment).navigate(action)
    }
}
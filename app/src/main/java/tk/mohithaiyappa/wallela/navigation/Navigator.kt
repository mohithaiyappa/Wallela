package tk.mohithaiyappa.wallela.navigation

import tk.mohithaiyappa.wallela.data.UrlDataStorage

interface Navigator {
    fun gotoHomeFragment()
    fun gotoFullScreenFragment(data: UrlDataStorage)
    fun gotoContactUsFragment()
}
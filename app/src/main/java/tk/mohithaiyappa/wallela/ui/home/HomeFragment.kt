package tk.mohithaiyappa.wallela.ui.home

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import tk.mohithaiyappa.wallela.ui.contactus.ContactUsFragment
import tk.mohithaiyappa.wallela.DataBaseHelper
import tk.mohithaiyappa.wallela.R
import tk.mohithaiyappa.wallela.adapters.RecyclerAdapter
import tk.mohithaiyappa.wallela.data.UrlDataStorage
import tk.mohithaiyappa.wallela.databinding.ActivityHomeBinding
import tk.mohithaiyappa.wallela.navigation.NavigatorImpl
import tk.mohithaiyappa.wallela.ui.WallelaActivity
import java.util.*

class HomeFragment : Fragment() {

    private var binding: ActivityHomeBinding? = null
    private val viewModel by viewModels<HomeViewModel> { HomeViewModelFactory(requireContext().applicationContext) }

    private val arrayList = ArrayList<UrlDataStorage?>()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerAdapter? = null
    private val TAG = "homeActivity"
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var inFavorites = false
    private var adView: AdView? = null
    private var navigator: NavigatorImpl? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navigator = NavigatorImpl(requireActivity() as WallelaActivity)
        binding = ActivityHomeBinding.inflate(inflater)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdMob()
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        drawerLayout = view.findViewById(R.id.drawer_layout)
        navigationView = view.findViewById(R.id.drawer)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(), drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(layoutManager)
        inFavorites = false
        adapter = RecyclerAdapter(arrayList, requireContext(), navigator!!)
        recyclerView!!.adapter = adapter

        navigationView!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            recyclerView!!.scrollTo(0, 0)
            when (menuItem.itemId) {
                R.id.flat_art -> {
                    viewModel.fetchDataForLabel("Flat-Art/")
                    toolbar.title = "Flat-Art"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    viewModel.inFavorites = false
                    recyclerView!!.smoothScrollToPosition(0)
                }
                R.id.superhero -> {
                    viewModel.fetchDataForLabel("SuperHero/")
                    toolbar.title = "SuperHero"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    viewModel.inFavorites = false
                    recyclerView!!.smoothScrollToPosition(0)
                }
                R.id.photography -> {
                    viewModel.fetchDataForLabel("Photography/")
                    toolbar.title = "Photography"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    viewModel.inFavorites = false
                    recyclerView!!.smoothScrollToPosition(0)
                }
                R.id.nature -> {
                    viewModel.fetchDataForLabel("nature/")
                    toolbar.title = "Nature"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    viewModel.inFavorites = false
                    recyclerView!!.smoothScrollToPosition(0)
                }
                R.id.favorites -> {
                    recyclerView!!.scrollToPosition(0)
                    viewModel.loadFavorites()
                    toolbar.title = "Favorites"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    viewModel.inFavorites = true
                }
                R.id.contact_us -> {
                    if(binding!!.drawerLayout.isDrawerOpen(GravityCompat.START))
                        binding!!.drawerLayout.closeDrawer(GravityCompat.START)
                    navigator!!.gotoContactUsFragment()
                }
                R.id.privacy_policy -> {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://wallela.flycricket.io/privacy.html")
                        )
                    )
                }
            }
            true
        })

        viewModel.urlListLiveData.observe(viewLifecycleOwner) {
            arrayList.clear()
            arrayList.addAll(it)
            adapter!!.notifyDataSetChanged()
        }
    }

// todo: impl back press in base fragment
//    override fun onBackPressed() {
//        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout!!.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }

    override fun onResume() {
        super.onResume()
        if (viewModel.inFavorites) {
            viewModel.loadFavorites()
        }
    }

    private fun initAdMob() {
        //adView = findViewById(R.id.adView)
        //val adRequest = AdRequest.Builder().build()
        //adView!!.loadAd(adRequest)
    }
}
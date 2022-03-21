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
import java.util.*

class HomeFragment : Fragment() {

    private var binding: ActivityHomeBinding? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private val arrayList = ArrayList<UrlDataStorage?>()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerAdapter? = null
    private val TAG = "homeActivity"
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var inFavorites = false
    private var adView: AdView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("Flat-Art/")
        layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(layoutManager)
        inFavorites = false
        adapter = RecyclerAdapter(arrayList, requireContext())
        recyclerView!!.adapter = adapter
        navigationView!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            recyclerView!!.scrollTo(0, 0)
            when (menuItem.itemId) {
                R.id.flat_art -> {
                    databaseReference = firebaseDatabase!!.getReference("Flat-Art/")
                    loadDataSet()
                    toolbar.title = "Flat-Art"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    inFavorites = false
                    recyclerView!!.smoothScrollToPosition(0)
                }
                R.id.superhero -> {
                    databaseReference = firebaseDatabase!!.getReference("SuperHero/")
                    loadDataSet()
                    toolbar.title = "SuperHero"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    inFavorites = false
                    recyclerView!!.smoothScrollToPosition(0)
                }
                R.id.photography -> {
                    databaseReference = firebaseDatabase!!.getReference("Photography/")
                    loadDataSet()
                    toolbar.title = "Photography"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    inFavorites = false
                    recyclerView!!.smoothScrollToPosition(0)
                }
                R.id.nature -> {
                    databaseReference = firebaseDatabase!!.getReference("nature/")
                    loadDataSet()
                    toolbar.title = "Nature"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    inFavorites = false
                    recyclerView!!.smoothScrollToPosition(0)
                }
                R.id.favorites -> {
                    recyclerView!!.scrollToPosition(0)
                    loadFavorites()
                    toolbar.title = "Favorites"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    inFavorites = true
                }
                R.id.contact_us -> {
                    //val intent = Intent(requireContext(), ContactUsActivity::class.java)
                    //startActivity(intent)
                    if(binding!!.drawerLayout.isDrawerOpen(GravityCompat.START))
                        binding!!.drawerLayout.closeDrawer(GravityCompat.START)
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(binding!!.root.id, ContactUsFragment())
                        .addToBackStack(null)
                        .commit()
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
            freeMemory()
            true
        })
        loadDataSet()
    }

// todo: impl back press in base fragment
//    override fun onBackPressed() {
//        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout!!.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }

    fun loadDataSet() {
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayList.clear()
                for (data in dataSnapshot.children) {
                    val urlDataStorage = UrlDataStorage(
                        data.child("midResUrl").getValue(
                            String::class.java
                        ),
                        data.child("Url").getValue(String::class.java),
                        data.child("lowResUrl").getValue(String::class.java)
                    )
                    arrayList.add(urlDataStorage)
                }
                Collections.shuffle(arrayList)
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun freeMemory() {
        System.runFinalization()
        Runtime.getRuntime().gc()
        System.gc()
    }

    fun loadFavorites() {
        loadAsycFavorites().execute()
    }

    inner class loadAsycFavorites : AsyncTask<Any?, Any?, Any?>() {
        private var aBoolean = false
        override fun doInBackground(objects: Array<Any?>): Any? {
            val db = DataBaseHelper(requireContext())
            aBoolean = false
            val cursor = db.data
            arrayList.clear()
            if (cursor == null) {
                aBoolean = true
            } else {
                while (cursor.moveToNext()) {
                    val urlDataStorage = UrlDataStorage(
                        cursor.getString(2),
                        cursor.getString(3), cursor.getString(1)
                    )
                    arrayList.add(urlDataStorage)
                }
            }
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            adapter!!.notifyDataSetChanged()
            if (aBoolean) {
                Toast.makeText(requireContext(), "Add something to Favorites", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (inFavorites) {
            loadAsycFavorites().execute()
        }
    }

    private fun initAdMob() {
        //adView = findViewById(R.id.adView)
        //val adRequest = AdRequest.Builder().build()
        //adView!!.loadAd(adRequest)
    }
}
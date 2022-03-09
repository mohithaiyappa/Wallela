package tk.mohithaiyappa.wallela

import android.Manifest
import android.animation.Animator
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class FullscreenActivity : AppCompatActivity() {
    private var animationView: LottieAnimationView? = null
    private var url: String? = null
    private var midUrl: String? = null
    private var lowUrl: String? = null
    private var bitmapHighRes: Bitmap? = null
    private var uri: URL? = null
    private var fabSetWallpaper: FloatingActionButton? = null
    private var fabDownload: FloatingActionButton? = null
    private var fabSetAsFavorite: FloatingActionButton? = null
    private var fabLockScreen: FloatingActionButton? = null
    private var fabMenu: FloatingActionMenu? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        val intent = intent
        url = intent.getStringExtra("Url")
        lowUrl = intent.getStringExtra("lowUrl")
        midUrl = intent.getStringExtra("midUrl")
        downloadImage().execute()
        animationView = findViewById(R.id.full_screen_animation_view)
        animationView?.playAnimation()
        init()
        adInit()
        fabInit()
        animationView?.setOnClickListener(View.OnClickListener { if (animationView!!.isAnimating()) animationView!!.pauseAnimation() else animationView!!.playAnimation() })
    }

    private fun adInit() {
        //interstitialAd = new InterstitialAd(this);
        //interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private inner class downloadImage : AsyncTask<Any?, Any?, Any?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(objects: Array<Any?>): Any? {
            try {
                uri = URL(url)
                val httpURLConnection = uri!!.openConnection() as HttpURLConnection
                httpURLConnection.doInput = true
                httpURLConnection.connect()
                val inputStream = httpURLConnection.inputStream
                bitmapHighRes = BitmapFactory.decodeStream(inputStream)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            fabMenu!!.visibility = View.VISIBLE
            fabSetWallpaper!!.labelText = "Set As Wallpaper"
            fabDownload!!.labelText = "Download"
            fabLockScreen!!.labelText = "Set as LockScreen"
        }
    }

    private fun fabInit() {
        val dataBaseHelper = DataBaseHelper(this)
        if (dataBaseHelper.dataExits(lowUrl!!)) {
            fabSetAsFavorite!!.labelText = removeFavorite
            fabSetAsFavorite!!.setImageResource(R.drawable.ic_favorite_golden)
        } else {
            fabSetAsFavorite!!.labelText = setFavorite
            fabSetAsFavorite!!.setImageResource(R.drawable.ic_favorite_white)
        }
    }

    private fun init() {
        fabSetWallpaper = findViewById(R.id.action_setWallpaper)
        fabDownload = findViewById(R.id.action_download)
        fabSetAsFavorite = findViewById(R.id.saveAsFavorite)
        fabLockScreen = findViewById(R.id.setAsLockScreen)
        fabMenu = findViewById(R.id.menu)
        fabMenu!!.setVisibility(View.INVISIBLE)
        fabSetWallpaper!!.setOnClickListener(View.OnClickListener {
            val manager = WallpaperManager.getInstance(applicationContext)
            if (bitmapHighRes != null) {
                fabMenu!!.close(true)
                try {
                    manager.setBitmap(bitmapHighRes)
                    Toast.makeText(
                        this@FullscreenActivity,
                        "Setting Wallpaper...",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@FullscreenActivity,
                        "Could not set Wallpaper",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            //                if(interstitialAd.isLoaded()){
//                    interstitialAd.show();
//                }
        })
        fabDownload!!.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@FullscreenActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    this@FullscreenActivity,
                    "Need permission to download...",
                    Toast.LENGTH_SHORT
                ).show()
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@FullscreenActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    //already shown reason
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(
                        this@FullscreenActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
                // Permission has already been granted
                downloadWallpaper()
            }
        })
        fabSetAsFavorite!!.setOnClickListener(View.OnClickListener {
            val dataBaseHelper = DataBaseHelper(this@FullscreenActivity)
            when (fabSetAsFavorite!!.getLabelText()) {
                setFavorite -> if (dataBaseHelper.dataExits(
                        lowUrl!!
                    )
                ) {
                    Toast.makeText(
                        this@FullscreenActivity,
                        "Already in Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (dataBaseHelper.insertData(lowUrl, midUrl, url)) {
                    Toast.makeText(
                        this@FullscreenActivity,
                        "Added to Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    fabSetAsFavorite!!.setImageResource(R.drawable.ic_favorite_golden)
                    fabSetAsFavorite!!.setLabelText(removeFavorite)
                } else Toast.makeText(this@FullscreenActivity, "Please retry", Toast.LENGTH_SHORT)
                    .show()
                removeFavorite -> if (dataBaseHelper.dropEntry(lowUrl!!)) {
                    Toast.makeText(this@FullscreenActivity, "Removed", Toast.LENGTH_SHORT).show()
                    fabSetAsFavorite!!.setImageResource(R.drawable.ic_favorite_white)
                    fabSetAsFavorite!!.setLabelText(setFavorite)
                } else Toast.makeText(
                    this@FullscreenActivity,
                    "Could not Remove",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        fabLockScreen!!.setOnClickListener(View.OnClickListener {
            val lockScreenWallpaper = WallpaperManager.getInstance(applicationContext)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    lockScreenWallpaper.setBitmap(
                        bitmapHighRes,
                        null,
                        true,
                        WallpaperManager.FLAG_LOCK
                    )
                    Toast.makeText(
                        this@FullscreenActivity,
                        "Setting Lockscreen....",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@FullscreenActivity,
                        "Could not Set Lockscreen....",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@FullscreenActivity,
                    "Need Android N or above",
                    Toast.LENGTH_LONG
                ).show()
            }
            fabMenu!!.close(true)
        })
        animationView!!.setOnClickListener { if (fabMenu!!.isOpened()) fabMenu!!.close(true) }
        animationView!!.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {
                if (bitmapHighRes != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        window.setFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        )
                    }
                    animationView!!.setImageBitmap(bitmapHighRes)
                    animationView!!.scaleType = ImageView.ScaleType.FIT_XY
                    animationView!!.cancelAnimation()
                }
            }
        })
    }

    private fun downloadWallpaper() {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/Wallela")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmapHighRes!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(
                this@FullscreenActivity,
                "Downloaded to $root/Wallela",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@FullscreenActivity, e.toString(), Toast.LENGTH_LONG).show()
        }
        fabMenu!!.close(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        // If request is cancelled, the result arrays are empty.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                downloadWallpaper()
            } else {
                Toast.makeText(this, "Need permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val setFavorite = "Add to Favorites"
        private const val removeFavorite = "Remove from Favorites"
        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 7
    }
}
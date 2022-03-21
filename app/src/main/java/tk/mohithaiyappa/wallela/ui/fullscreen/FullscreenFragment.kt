package tk.mohithaiyappa.wallela.ui.fullscreen

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import tk.mohithaiyappa.wallela.DataBaseHelper
import tk.mohithaiyappa.wallela.R
import tk.mohithaiyappa.wallela.databinding.ActivityFullscreenBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class FullscreenFragment : Fragment() {

    private var binding: ActivityFullscreenBinding? = null

    private var url: String? = null
    private var midUrl: String? = null
    private var lowUrl: String? = null
    private var bitmapHighRes: Bitmap? = null
    private var uri: URL? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityFullscreenBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val args = arguments
        url = args?.getString("Url")
        lowUrl = args?.getString("lowUrl")
        midUrl = args?.getString("midUrl")
        downloadImage().execute()

        binding!!.fullScreenAnimationView.playAnimation()
        init()
        adInit()
        fabInit()
        binding!!.fullScreenAnimationView.setOnClickListener(View.OnClickListener {
            if (binding!!.fullScreenAnimationView.isAnimating)
                binding!!.fullScreenAnimationView.pauseAnimation()
            else
                binding!!.fullScreenAnimationView.playAnimation() })
    }

    private fun adInit() {
        //interstitialAd = new InterstitialAd(this);
        //interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //interstitialAd.loadAd(new AdRequest.Builder().build());
    }

//    override fun finish() {
//        super.finish()
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//    }

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
            binding!!.menu.visibility = View.VISIBLE
            binding!!.actionSetWallpaper.labelText = "Set As Wallpaper"
            binding!!.actionDownload.labelText = "Download"
            binding!!.setAsLockScreen.labelText = "Set as LockScreen"
        }
    }

    private fun fabInit() {
        val dataBaseHelper = DataBaseHelper(requireContext())
        if (dataBaseHelper.dataExits(lowUrl!!)) {
            binding!!.saveAsFavorite.labelText = removeFavorite
            binding!!.saveAsFavorite.setImageResource(R.drawable.ic_favorite_golden)
        } else {
            binding!!.saveAsFavorite.labelText = setFavorite
            binding!!.saveAsFavorite.setImageResource(R.drawable.ic_favorite_white)
        }
    }

    private fun init() {
        binding!!.menu.visibility = View.INVISIBLE
        binding!!.actionSetWallpaper.setOnClickListener(View.OnClickListener {
            val manager = WallpaperManager.getInstance(requireContext().applicationContext)
            if (bitmapHighRes != null) {
                binding!!.menu.close(true)
                try {
                    manager.setBitmap(bitmapHighRes)
                    Toast.makeText(
                        requireContext(),
                        "Setting Wallpaper...",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Could not set Wallpaper",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
        binding!!.actionDownload.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    requireContext(),
                    "Need permission to download...",
                    Toast.LENGTH_SHORT
                ).show()
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    //already shown reason
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
                // Permission has already been granted
                downloadWallpaper()
            }
        })
        binding!!.saveAsFavorite.setOnClickListener(View.OnClickListener {
            val dataBaseHelper = DataBaseHelper(requireContext())
            when (binding!!.saveAsFavorite.labelText) {
                setFavorite -> if (dataBaseHelper.dataExits(
                        lowUrl!!
                    )
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Already in Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (dataBaseHelper.insertData(lowUrl, midUrl, url)) {
                    Toast.makeText(
                        requireContext(),
                        "Added to Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding!!.saveAsFavorite.setImageResource(R.drawable.ic_favorite_golden)
                    binding!!.saveAsFavorite.setLabelText(removeFavorite)
                } else Toast.makeText(requireContext(), "Please retry", Toast.LENGTH_SHORT)
                    .show()
                removeFavorite -> if (dataBaseHelper.dropEntry(lowUrl!!)) {
                    Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show()
                    binding!!.saveAsFavorite.setImageResource(R.drawable.ic_favorite_white)
                    binding!!.saveAsFavorite.setLabelText(setFavorite)
                } else Toast.makeText(
                    requireContext(),
                    "Could not Remove",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        binding!!.setAsLockScreen.setOnClickListener(View.OnClickListener {
            val lockScreenWallpaper = WallpaperManager.getInstance(requireContext().applicationContext)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    lockScreenWallpaper.setBitmap(
                        bitmapHighRes,
                        null,
                        true,
                        WallpaperManager.FLAG_LOCK
                    )
                    Toast.makeText(
                        requireContext(),
                        "Setting Lockscreen....",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Could not Set Lockscreen....",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Need Android N or above",
                    Toast.LENGTH_LONG
                ).show()
            }
            binding!!.menu.close(true)
        })
        binding!!.root.setOnClickListener {
            if (binding!!.menu.isOpened)
                binding!!.menu.close(true)
        }
        binding!!.fullScreenAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {
                if (bitmapHighRes != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        requireActivity().window.setFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        )
                    }
                    binding!!.fullScreenAnimationView.setImageBitmap(bitmapHighRes)
                    binding!!.fullScreenAnimationView.scaleType = ImageView.ScaleType.FIT_XY
                    binding!!.fullScreenAnimationView.cancelAnimation()
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
                requireContext(),
                "Downloaded to $root/Wallela",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
        binding!!.menu.close(true)
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
                Toast.makeText(requireContext(), "Need permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val setFavorite = "Add to Favorites"
        private const val removeFavorite = "Remove from Favorites"
        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 7
    }
}
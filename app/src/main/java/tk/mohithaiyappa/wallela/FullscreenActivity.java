package tk.mohithaiyappa.wallela;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class FullscreenActivity extends AppCompatActivity {


    private LottieAnimationView animationView;
    private String url, midUrl, lowUrl;
    private Bitmap bitmapHighRes;
    private static final String setFavorite = "Add to Favorites";
    private static final String removeFavorite = "Remove from Favorites";
    private URL uri;
    private FloatingActionButton fabSetWallpaper, fabDownload, fabSetAsFavorite, fabLockScreen;
    private FloatingActionMenu fabMenu;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 7;
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        Intent intent = getIntent();
        url = intent.getStringExtra("Url");
        lowUrl = intent.getStringExtra("lowUrl");
        midUrl = intent.getStringExtra("midUrl");
        new downloadImage().execute();
        animationView = findViewById(R.id.full_screen_animation_view);
        animationView.playAnimation();
        init();
        adInit();
        fabInit();
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animationView.isAnimating()) animationView.pauseAnimation();
                else animationView.playAnimation();
            }
        });
    }


    private void adInit() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private class downloadImage extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                uri = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) uri.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bitmapHighRes = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            fabMenu.setVisibility(View.VISIBLE);
            fabSetWallpaper.setLabelText("Set As Wallpaper");
            fabDownload.setLabelText("Download");
            fabLockScreen.setLabelText("Set as LockScreen");
        }
    }


    private void fabInit() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        if (dataBaseHelper.dataExits(lowUrl)) {
            fabSetAsFavorite.setLabelText(removeFavorite);
            fabSetAsFavorite.setImageResource(R.drawable.ic_favorite_golden);
        } else {
            fabSetAsFavorite.setLabelText(setFavorite);
            fabSetAsFavorite.setImageResource(R.drawable.ic_favorite_white);
        }
    }


    private void init() {


        fabSetWallpaper = findViewById(R.id.action_setWallpaper);
        fabDownload = findViewById(R.id.action_download);
        fabSetAsFavorite = findViewById(R.id.saveAsFavorite);
        fabLockScreen = findViewById(R.id.setAsLockScreen);
        fabMenu = findViewById(R.id.menu);
        fabMenu.setVisibility(View.INVISIBLE);


        fabSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                if (bitmapHighRes != null) {
                    fabMenu.close(true);
                    try {
                        manager.setBitmap(bitmapHighRes);
                        Toast.makeText(FullscreenActivity.this, "Setting Wallpaper...", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(FullscreenActivity.this, "Could not set Wallpaper", Toast.LENGTH_SHORT).show();
                    }
                }
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }


            }
        });


        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(FullscreenActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(FullscreenActivity.this, "Need permission to download...", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(FullscreenActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //already shown reason
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(FullscreenActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    // Permission has already been granted
                    downloadWallpaper();
                }
            }
        });


        fabSetAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(FullscreenActivity.this);
                switch (fabSetAsFavorite.getLabelText()) {
                    case setFavorite:
                        if (dataBaseHelper.dataExits(lowUrl)) {
                            Toast.makeText(FullscreenActivity.this, "Already in Favorites", Toast.LENGTH_SHORT).show();
                        } else if (dataBaseHelper.insertData(lowUrl, midUrl, url)) {
                            Toast.makeText(FullscreenActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                            fabSetAsFavorite.setImageResource(R.drawable.ic_favorite_golden);
                            fabSetAsFavorite.setLabelText(removeFavorite);
                        } else
                            Toast.makeText(FullscreenActivity.this, "Please retry", Toast.LENGTH_SHORT).show();
                        break;
                    case removeFavorite:
                        if (dataBaseHelper.dropEntry(lowUrl)) {
                            Toast.makeText(FullscreenActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                            fabSetAsFavorite.setImageResource(R.drawable.ic_favorite_white);
                            fabSetAsFavorite.setLabelText(setFavorite);
                        } else
                            Toast.makeText(FullscreenActivity.this, "Could not Remove", Toast.LENGTH_SHORT).show();
                }

            }
        });


        fabLockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager lockScreenWallpaper = WallpaperManager.getInstance(getApplicationContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        lockScreenWallpaper.setBitmap(bitmapHighRes, null, true, WallpaperManager.FLAG_LOCK);
                        Toast.makeText(FullscreenActivity.this, "Setting Lockscreen....", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(FullscreenActivity.this, "Could not Set Lockscreen....", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(FullscreenActivity.this, "Need Android N or above", Toast.LENGTH_LONG).show();
                }
                fabMenu.close(true);
            }
        });


        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) fabMenu.close(true);
            }
        });


        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (bitmapHighRes != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    }
                    animationView.setImageBitmap(bitmapHighRes);
                    animationView.setScaleType(ImageView.ScaleType.FIT_XY);
                    animationView.cancelAnimation();

                }
            }
        });
    }


    private void downloadWallpaper() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Wallela");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapHighRes.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(FullscreenActivity.this, "Downloaded to " + root + "/Wallela", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(FullscreenActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
        fabMenu.close(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                downloadWallpaper();
            } else {
                Toast.makeText(this, "Need permission", Toast.LENGTH_SHORT).show();
            }

        }
    }


}

package tk.mohithaiyappa.wallela;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class FullscreenActivity extends AppCompatActivity {
   /* private String url , midUrl, lowUrl  ;
    private Bitmap bitmapHighRes /* ,bitmapLowRes *'/;
    private static final String setFavorite = "Add to Favorites";
    private static final String removeFavorite = "Remove from Favorites";
    //private ImageView imageView;
    private LottieAnimationView animationView;
    private URL uri;
    private FloatingActionButton fabSetWallpaper, fabDownload, fabSetAsFavorite, fabLockScreen;
    private FloatingActionMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new downloadImage().execute();
        setContentView(R.layout.activity_fullscreen);
        animationView = findViewById(R.id.full_screen_animation_view);
        animationView.playAnimation();
        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Intent intent = getIntent();
        url = intent.getStringExtra("Url");
        lowUrl = intent.getStringExtra("lowUrl");
        midUrl = intent.getStringExtra("midUrl");

        fabInit();
    }

    /**
     * initialize the fabs
     * TODO: Complete the favorites listener
     *'/
    public void init() {

        fabSetWallpaper = findViewById(R.id.action_setWallpaper);
        fabDownload = findViewById(R.id.action_download);
        fabSetAsFavorite = findViewById(R.id.saveAsFavorite);
        fabLockScreen = findViewById(R.id.setAsLockScreen);
        fabMenu = findViewById(R.id.menu);
        //imageView = findViewById(R.id.full_screen_image_view);



        fabSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                if (bitmapHighRes != null) {
                    try {
                        manager.setBitmap(bitmapHighRes);
                        Toast.makeText(FullscreenActivity.this, "Setting Wallpaper...", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(FullscreenActivity.this, "Could not set Wallpaper", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FullscreenActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
                            fabSetAsFavorite.setImageResource(R.drawable.ic_action_setfavorite);
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
            }
        });

        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) fabMenu.close(true);
            }
        });
    }

    public void fabInit() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        if (dataBaseHelper.dataExits(lowUrl)) {
            fabSetAsFavorite.setLabelText(removeFavorite);
            fabSetAsFavorite.setImageResource(R.drawable.ic_favorite_golden);
        } else {
            fabSetAsFavorite.setLabelText(setFavorite);
            fabSetAsFavorite.setImageResource(R.drawable.ic_action_setfavorite);
        }
    }

    /**
     * Downloading the bitmaps
     *'/
    public class downloadImage extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                /*if (bitmapLowRes == null && bitmapHighRes == null) {
                    uri = new URL(midUrl);
                } else if (bitmapLowRes != null && bitmapHighRes == null) {
                    uri = new URL(url);
                }*'/
                uri = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) uri.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                /*if (bitmapLowRes == null && bitmapHighRes == null) {
                    bitmapLowRes = BitmapFactory.decodeStream(inputStream);
                } else {
                    bitmapHighRes = BitmapFactory.decodeStream(inputStream);
                }*'/
               bitmapHighRes= BitmapFactory.decodeStream(inputStream);

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
            /*if (bitmapHighRes == null) {
                imageView.setImageBitmap(bitmapLowRes);
                new downloadImage().execute();
            } else imageView.setImageBitmap(bitmapHighRes);
            if (bitmapHighRes != null) {
                fabSetWallpaper.setLabelText("Set As Wallpaper");
                fabDownload.setLabelText("Download");
                fabLockScreen.setLabelText("Set as LockScreen");
            }*'/
            animationView.setImageBitmap(bitmapHighRes);
            fabSetWallpaper.setLabelText("Set As Wallpaper");
            fabDownload.setLabelText("Download");
            fabLockScreen.setLabelText("Set as LockScreen");

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }*/

   //---------------------------------------------------------------------------------


    private LottieAnimationView animationView;
    private String url , midUrl, lowUrl  ;
    private Bitmap bitmapHighRes;
    private static final String setFavorite = "Add to Favorites";
    private static final String removeFavorite = "Remove from Favorites";
    private URL uri;
    private FloatingActionButton fabSetWallpaper, fabDownload, fabSetAsFavorite, fabLockScreen;
    private FloatingActionMenu fabMenu;




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
        fabInit();
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(animationView.isAnimating()) animationView.pauseAnimation();
                else animationView.playAnimation();
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public class downloadImage extends AsyncTask {
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
                bitmapHighRes= BitmapFactory.decodeStream(inputStream);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
            animationView.setImageBitmap(bitmapHighRes);
            animationView.setScaleType(ImageView.ScaleType.FIT_XY);
            fabSetWallpaper.setLabelText("Set As Wallpaper");
            fabDownload.setLabelText("Download");
            fabLockScreen.setLabelText("Set as LockScreen");

        }
    }

    public void fabInit() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        if (dataBaseHelper.dataExits(lowUrl)) {
            fabSetAsFavorite.setLabelText(removeFavorite);
            fabSetAsFavorite.setImageResource(R.drawable.ic_favorite_golden);
        } else {
            fabSetAsFavorite.setLabelText(setFavorite);
            fabSetAsFavorite.setImageResource(R.drawable.ic_action_setfavorite);
        }
    }


    public void init() {

        fabSetWallpaper = findViewById(R.id.action_setWallpaper);
        fabDownload = findViewById(R.id.action_download);
        fabSetAsFavorite = findViewById(R.id.saveAsFavorite);
        fabLockScreen = findViewById(R.id.setAsLockScreen);
        fabMenu = findViewById(R.id.menu);



        fabSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                if (bitmapHighRes != null) {
                    try {
                        manager.setBitmap(bitmapHighRes);
                        Toast.makeText(FullscreenActivity.this, "Setting Wallpaper...", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(FullscreenActivity.this, "Could not set Wallpaper", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FullscreenActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
                            fabSetAsFavorite.setImageResource(R.drawable.ic_action_setfavorite);
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
            }
        });

        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) fabMenu.close(true);
            }
        });
    }
}

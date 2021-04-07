package tk.mohithaiyappa.wallela;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class FrescoApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
    }


}

package tk.mohithaiyappa.wallela

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class FrescoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}
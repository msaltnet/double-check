package net.msalt.doublecheck

import android.app.Application
import android.util.Log

class DoubleCheckApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("JSM_TEST", "Application is Created!")
    }
}
package net.msalt.doublecheck

import android.app.Application
import net.msalt.doublecheck.data.DoubleCheckDatabase
import timber.log.Timber

class DoubleCheckApplication : Application() {
    val database by lazy {
        Timber.d("database is Created!")
        DoubleCheckDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        }
        Timber.d("Application is Created!")
    }
}

class TimberDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "${element.fileName}:${element.lineNumber}#${element.methodName}"
    }
}

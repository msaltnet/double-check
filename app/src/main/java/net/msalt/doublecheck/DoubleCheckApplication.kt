package net.msalt.doublecheck

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import net.msalt.doublecheck.data.DoubleCheckDatabase

class DoubleCheckApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy {
        Log.d("JSM_TEST", "database is Created!")
        DoubleCheckDatabase.getDatabase(this, applicationScope)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("JSM_TEST", "Application is Created!")
    }
}
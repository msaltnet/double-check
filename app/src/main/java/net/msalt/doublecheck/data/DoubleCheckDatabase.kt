package net.msalt.doublecheck.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Bunch::class, CheckItem::class], version = 1, exportSchema = false)
abstract class DoubleCheckDatabase : RoomDatabase() {
    abstract fun bunchDao(): BunchDao
    abstract fun checkItemDao(): CheckItemDao
    abstract fun bunchWithCheckItemDao(): BunchWithCheckItemDao

    companion object {
        @Volatile
        private var INSTANCE: DoubleCheckDatabase? = null

        fun getDatabase(
            context: Context,
        ): DoubleCheckDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DoubleCheckDatabase::class.java,
                    "doublecheck_db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
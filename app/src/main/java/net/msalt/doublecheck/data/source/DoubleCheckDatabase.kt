package net.msalt.doublecheck.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem

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
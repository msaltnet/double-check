package net.msalt.doublecheck.data

import androidx.room.*

@Dao
interface BunchWithCheckItemDao {
    @Transaction
    @Query("SELECT * FROM bunch")
    suspend fun getAll(): List<BunchWithCheckItem>
}

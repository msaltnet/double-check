package net.msalt.doublecheck.data

import androidx.room.*

@Dao
interface BunchWithCheckItemDao {
    @Transaction
    @Query("SELECT * FROM bunch WHERE id is :bunchId")
    suspend fun get(bunchId: String): BunchWithCheckItem
}

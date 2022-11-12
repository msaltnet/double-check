package net.msalt.doublecheck.data

import androidx.room.*

@Dao
interface BunchWithCheckItemDao {
    @Transaction
    @Query("SELECT * FROM bunch")
    fun getAll(): List<BunchWithCheckItem>
}

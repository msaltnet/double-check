package net.msalt.doublecheck.data

import androidx.room.*

@Dao
interface BunchWithCheckItemDao {
    @Transaction
    @Query("SELECT * FROM checkitem")
    fun getAll(): List<BunchWithCheckItem>
}

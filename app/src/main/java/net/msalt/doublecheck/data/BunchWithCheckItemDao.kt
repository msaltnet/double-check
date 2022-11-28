package net.msalt.doublecheck.data

import androidx.room.*

@Dao
interface BunchWithCheckItemDao {
    @Query("SELECT * FROM bunch WHERE id = :bunchId")
    suspend fun getBunch(bunchId: String): Bunch

    @Query("SELECT * FROM checkitem WHERE bunchId = :bunchId ORDER BY `order` ASC")
    suspend fun getCheckItem(bunchId: String): List<CheckItem>

    @Transaction
    suspend fun get(bunchId: String): BunchWithCheckItem {
        return BunchWithCheckItem(getBunch(bunchId), getCheckItem(bunchId))
    }
}

package net.msalt.doublecheck.data.source

import androidx.room.*
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.BunchWithCheckItem
import net.msalt.doublecheck.data.CheckItem

@Dao
interface BunchWithCheckItemDao {
    @Query("SELECT * FROM bunch WHERE id = :bunchId")
    suspend fun getBunch(bunchId: String): Bunch?

    @Query("SELECT * FROM checkitem WHERE bunchId = :bunchId ORDER BY `order` ASC")
    suspend fun getCheckItem(bunchId: String): List<CheckItem>

    @Transaction
    suspend fun get(bunchId: String): BunchWithCheckItem? {
        return getBunch(bunchId)?.let { BunchWithCheckItem(it, getCheckItem(bunchId)) }
    }

    @Transaction
    @Query("SELECT * FROM bunch")
    suspend fun getAll(): List<BunchWithCheckItem>
}

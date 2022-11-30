package net.msalt.doublecheck.data

import androidx.room.*

@Dao
interface CheckItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkItems: List<CheckItem>)

    @Upsert
    suspend fun upsert(checkItem: CheckItem)

    @Upsert
    suspend fun upsert(checkItem: List<CheckItem>)

    @Delete
    suspend fun delete(checkItem: CheckItem)

    @Query("DELETE FROM checkitem WHERE bunchId = :bunchId")
    suspend fun deleteByBunchId(bunchId: String)

    @Query("SELECT * FROM checkitem WHERE bunchId = :bunchId ORDER BY `order` ASC")
    suspend fun getByBunchId(bunchId: String): List<CheckItem>
}

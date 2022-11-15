package net.msalt.doublecheck.data

import androidx.room.*

@Dao
interface CheckItemDao {
    @Upsert
    suspend fun upsert(checkItem: CheckItem)

    @Delete
    suspend fun delete(checkItem: CheckItem)

    @Query("SELECT * FROM checkitem")
    suspend fun getAll(): List<CheckItem>
}

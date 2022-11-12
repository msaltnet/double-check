package net.msalt.doublecheck.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CheckItemDao {
    @Insert
    suspend fun insert(checkItem: CheckItem)

    @Delete
    suspend fun delete(checkItem: CheckItem)

    @Query("SELECT * FROM checkitem")
    suspend fun getAll(): List<CheckItem>
}

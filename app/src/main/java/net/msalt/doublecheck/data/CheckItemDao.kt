package net.msalt.doublecheck.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CheckItemDao {
    @Insert
    fun insert(checkItem: CheckItem)

    @Delete
    fun delete(checkItem: CheckItem)

    @Query("SELECT * FROM CheckItem")
    fun getAll(): List<CheckItem>
}

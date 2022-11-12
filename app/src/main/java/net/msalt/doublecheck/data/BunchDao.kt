package net.msalt.doublecheck.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BunchDao {
    @Insert
    suspend fun insert(bunch: Bunch)

    @Delete
    suspend fun delete(bunch: Bunch)

    @Query("SELECT * FROM bunch")
    suspend fun getAll(): List<Bunch>
}

package net.msalt.doublecheck.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BunchDao {
    @Insert
    fun insert(bunch: Bunch)

    @Delete
    fun delete(bunch: Bunch)

    @Query("SELECT * FROM Bunch")
    fun getAll(): List<Bunch>
}

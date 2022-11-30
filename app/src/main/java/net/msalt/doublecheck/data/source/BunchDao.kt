package net.msalt.doublecheck.data.source

import androidx.room.*
import net.msalt.doublecheck.data.Bunch

@Dao
interface BunchDao {
    @Upsert
    suspend fun upsert(bunch: Bunch)

    @Delete
    suspend fun delete(bunch: Bunch)

    @Query("DELETE FROM bunch WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM bunch WHERE id is :bunchId")
    suspend fun getById(bunchId: String): Bunch
}

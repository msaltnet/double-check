package net.msalt.doublecheck.data.source

import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.BunchWithCheckItem
import net.msalt.doublecheck.data.CheckItem

interface CheckListRepository {
    suspend fun updateBunch(bunch: Bunch)

    suspend fun getBunch(bunchId: String): Bunch?

    suspend fun updateCheckItem(checkItem: CheckItem)

    suspend fun updateCheckItem(checkItem: List<CheckItem>)

    suspend fun deleteCheckItem(checkItem: CheckItem)

    suspend fun getCheckItems(bunchId: String): List<CheckItem>

    suspend fun deleteBunchWithItem(bunchId: String)

    suspend fun getBunchWithItem(bunchId: String): BunchWithCheckItem?

    suspend fun getAllBunchWithItem(): List<BunchWithCheckItem>
}

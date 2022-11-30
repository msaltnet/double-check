package net.msalt.doublecheck.data.source

import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.BunchWithCheckItem
import net.msalt.doublecheck.data.CheckItem

class DefaultCheckListRepository(private val database: DoubleCheckDatabase) : CheckListRepository {
    override suspend fun updateBunch(bunch: Bunch) {
        database.bunchDao().upsert(bunch)
    }

    override suspend fun getBunch(bunchId: String): Bunch {
        return database.bunchDao().getById(bunchId)
    }

    override suspend fun updateCheckItem(checkItem: CheckItem) {
        database.checkItemDao().upsert(checkItem)
    }

    override suspend fun updateCheckItem(checkItem: List<CheckItem>) {
        database.checkItemDao().upsert(checkItem)
    }

    override suspend fun deleteCheckItem(checkItem: CheckItem) {
        database.checkItemDao().delete(checkItem)
    }

    override suspend fun deleteBunchWithItem(bunchId: String) {
        database.bunchDao().deleteById(bunchId)
        database.checkItemDao().deleteByBunchId(bunchId)
    }

    override suspend fun getCheckItems(bunchId: String): List<CheckItem> {
        return database.checkItemDao().getByBunchId(bunchId)
    }

    override suspend fun getBunchWithItem(bunchId: String): BunchWithCheckItem {
        return database.bunchWithCheckItemDao().get(bunchId)
    }

    override suspend fun getAllBunchWithItem(): List<BunchWithCheckItem> {
        return database.bunchWithCheckItemDao().getAll()
    }
}

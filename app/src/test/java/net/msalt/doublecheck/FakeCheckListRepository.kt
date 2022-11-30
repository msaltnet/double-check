package net.msalt.doublecheck

import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.BunchWithCheckItem
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.data.source.CheckListRepository
import java.util.LinkedHashMap

class FakeCheckListRepository() : CheckListRepository {
    var bunchData: LinkedHashMap<String, Bunch> = LinkedHashMap()
    var checkItemData: LinkedHashMap<String, CheckItem> = LinkedHashMap()

    override suspend fun updateBunch(bunch: Bunch) {
        bunchData[bunch.id] = bunch
    }

    override suspend fun getBunch(bunchId: String): Bunch {
//        return bunchData[bunchId] ?: Bunch
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

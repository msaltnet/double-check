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
        bunchData[bunch.id] = bunch.copy()
    }

    override suspend fun getBunch(bunchId: String): Bunch? {
        return bunchData[bunchId]
    }

    override suspend fun updateCheckItem(checkItem: CheckItem) {
        checkItemData[checkItem.id] = checkItem.copy()
    }

    override suspend fun updateCheckItem(checkItem: List<CheckItem>) {
        for (item in checkItem) {
            checkItemData[item.id] = item.copy()
        }
    }

    override suspend fun deleteCheckItem(checkItem: CheckItem) {
        checkItemData.remove(checkItem.id)
    }

    override suspend fun deleteBunchWithItem(bunchId: String) {
        bunchData.remove(bunchId)
        val targetItems = java.util.ArrayList<String>()
        for (item in checkItemData.values)
            targetItems.add(item.id)

        for (id in targetItems)
            checkItemData.remove(id)
    }

    override suspend fun getCheckItems(bunchId: String): List<CheckItem> {
        val targetItems = java.util.ArrayList<CheckItem>()
        for (item in checkItemData.values) {
            if (item.bunchId == bunchId)
                targetItems.add(item.copy())
        }
        targetItems.sortWith(compareBy { it.order })
        return targetItems
    }

    override suspend fun getBunchWithItem(bunchId: String): BunchWithCheckItem? {
        val bunch = bunchData[bunchId] ?: return null
        val targetItems = java.util.ArrayList<CheckItem>()
        for (item in checkItemData.values) {
            if (item.bunchId == bunchId)
                targetItems.add(item.copy())
        }
        targetItems.sortWith(compareBy { it.order })
        return BunchWithCheckItem(bunch, checkItems = targetItems)
    }

    override suspend fun getAllBunchWithItem(): List<BunchWithCheckItem> {
        val bunchList = ArrayList<BunchWithCheckItem>()
        for (bunch in bunchData.values) {
            val items = ArrayList<CheckItem>(getCheckItems(bunch.id))
            items.sortWith(compareBy { it.order })
            bunchList.add(BunchWithCheckItem(bunch.copy(), checkItems = items))
        }
        return bunchList
    }
}

package net.msalt.doublecheck.bunchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.BunchCard
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.data.source.CheckListRepository
import net.msalt.doublecheck.data.source.DoubleCheckDatabase
import timber.log.Timber

class BunchListViewModel(private val checkListRepository: CheckListRepository) : ViewModel() {
    private val _items = ArrayList<BunchCard>()
    val items: LiveData<List<BunchCard>> = MutableLiveData(_items)
    private val _loaded = MutableLiveData(false)
    val loaded: LiveData<Boolean> = _loaded
    private val _cloneCompleted = MutableLiveData(false)
    val cloneCompleted: LiveData<Boolean> = _cloneCompleted

    fun start() {
        _loaded.value = false
        viewModelScope.launch {
            _items.clear()
            val all = checkListRepository.getAllBunchWithItem()
            for (item in all) {
                Timber.d("Bunch: ${item.bunch.id}: ${item.bunch.title}, ${item.checkItems.size}")
                val count = if (item.checkItems.size < 3) item.checkItems.size else 3
                val digest = ArrayList<String>()
                for (i in 0 until count) {
                    digest.add(item.checkItems[i].contents)
                }
                _items.add(
                    BunchCard(
                        id = item.bunch.id,
                        title = item.bunch.title,
                        category = item.bunch.category,
                        digest = digest.joinToString(separator = "\n")
                    )
                )
            }
            _loaded.value = true
        }
    }

    fun deleteBunch(item: BunchCard) {
        val id = item.id
        _items.remove(item)
        viewModelScope.launch {
            checkListRepository.deleteBunchWithItem(id)
        }
    }

    fun cloneBunch(item: BunchCard): String {
        _cloneCompleted.value = false
        val bunch = Bunch()
        bunch.title = item.title + "_copy"
        bunch.category = item.category
        viewModelScope.launch {
            checkListRepository.updateBunch(bunch)
            val all = checkListRepository.getCheckItems(item.id)
            val newList = mutableListOf<CheckItem>()
            for ((order, target) in all.withIndex()) {
                newList.add(
                    CheckItem(
                        contents = target.contents,
                        bunchId = bunch.id,
                        order = order
                    )
                )
            }
            checkListRepository.updateCheckItem(newList)
            _cloneCompleted.value = true
        }
        return bunch.id
    }
}

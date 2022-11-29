package net.msalt.doublecheck.bunchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.msalt.doublecheck.data.BunchCard
import net.msalt.doublecheck.data.DoubleCheckDatabase
import timber.log.Timber

class BunchListViewModel(private val database: DoubleCheckDatabase) : ViewModel() {
    private val _items = ArrayList<BunchCard>()
    val items: LiveData<List<BunchCard>> = MutableLiveData(_items)
    private val _loaded = MutableLiveData(false)
    val loaded: LiveData<Boolean> = _loaded

    fun update() {
        _loaded.value = false
        viewModelScope.launch {
            _items.clear()
            val all = database.bunchWithCheckItemDao().getAll()
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
            database.bunchDao().deleteById(id)
            database.checkItemDao().deleteByBunchId(id)
        }
    }
}

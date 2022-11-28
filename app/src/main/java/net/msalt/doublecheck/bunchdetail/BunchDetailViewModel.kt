package net.msalt.doublecheck.bunchdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.data.DoubleCheckDatabase
import timber.log.Timber

class BunchDetailViewModel(private val database: DoubleCheckDatabase) : ViewModel() {
    val title = MutableLiveData("")
    private val _items = MutableLiveData<List<CheckItem>>()
    val items: LiveData<List<CheckItem>> = _items
    private lateinit var bunch: Bunch

    fun start(newBunch: Bunch) {
        viewModelScope.launch {
            bunch = newBunch
            database.bunchDao().upsert(newBunch)
        }
    }

    fun start(bunchId: String) {
        viewModelScope.launch {
            val data = database.bunchWithCheckItemDao().get(bunchId)
            bunch = data.bunch
            title.value = data.bunch.title
            for (item in data.checkItems) {
                item.contents_data.value = item.contents
                Timber.d("Bunch items: ${item.id}: ${item.contents}")
            }
            _items.value = data.checkItems
        }
    }

    fun toggleCheck(item: CheckItem) {
        item.checked = !item.checked
        viewModelScope.launch(Dispatchers.IO) {
            database.checkItemDao().upsert(item)
        }
    }
}

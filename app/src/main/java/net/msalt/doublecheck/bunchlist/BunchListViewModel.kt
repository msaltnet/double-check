package net.msalt.doublecheck.bunchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.data.DoubleCheckDatabase
import timber.log.Timber

class BunchListViewModel(private val database: DoubleCheckDatabase) : ViewModel() {
    private val _items = MutableLiveData<List<Bunch>>()
    val items: LiveData<List<Bunch>> = _items

    fun update() {
        viewModelScope.launch {
            val all = database.bunchDao().getAll()
            for (i in all) {
                Timber.d("Bunch: ${i.id}: ${i.title}")
            }
            _items.value = all
        }
    }
}

package net.msalt.doublecheck.bunchdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.data.source.CheckListRepository
import net.msalt.doublecheck.data.source.DoubleCheckDatabase
import timber.log.Timber
import java.io.IOException

class BunchDetailViewModel(private val checkListRepository: CheckListRepository) : ViewModel() {
    val title = MutableLiveData("")
    private val _items = MutableLiveData<List<CheckItem>>()
    val items: LiveData<List<CheckItem>> = _items
    private lateinit var bunch: Bunch

    fun start(newBunch: Bunch) {
        bunch = newBunch
        viewModelScope.launch {
            checkListRepository.updateBunch(newBunch)
        }
    }

    fun start(bunchId: String) {
        viewModelScope.launch {
            val data = checkListRepository.getBunchWithItem(bunchId)
                ?: throw IllegalArgumentException("Invalid Bunch ID!")

            bunch = data.bunch
            title.value = data.bunch.title
            _items.value = data.checkItems

            for (item in data.checkItems) {
                Timber.d("Bunch items: ${item.id}: ${item.contents}, ${item.order}")
            }
        }
    }

    fun toggleCheck(item: CheckItem) {
        item.checked = !item.checked
        viewModelScope.launch {
            checkListRepository.updateCheckItem(item)
        }
    }

    fun resetCheckState() {
        for (item in _items.value!!) {
            item.checked = false
        }
        viewModelScope.launch {
            checkListRepository.updateCheckItem(_items.value!!)
        }
    }
}

package net.msalt.doublecheck.editbunch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.data.source.CheckListRepository
import net.msalt.doublecheck.data.source.DoubleCheckDatabase
import org.jetbrains.annotations.NotNull
import timber.log.Timber

class EditBunchViewModel(private val checkListRepository: CheckListRepository) : ViewModel() {
    val title = MutableLiveData("")
    val items = ArrayList<CheckItem>()
    private val _loaded = MutableLiveData(false)
    val loaded: LiveData<Boolean> = _loaded
    private lateinit var bunch: Bunch
    private var deferredUpdateTitleJob: Job? = null
    private var deferredUpdateItemJob: Job? = null
    private var deferredUpdateItem: CheckItem? = null

    fun start(bunchId: String) {
        Timber.d("Bunch items: ${items.size}")
        _loaded.value = false
        viewModelScope.launch {
            items.clear()
            val data = checkListRepository.getBunchWithItem(bunchId)
            bunch = data.bunch
            title.value = data.bunch.title
            for (item in data.checkItems) {
                item.contents_data.value = item.contents
                items.add(item)
                Timber.d("Bunch items: ${item.id}: ${item.contents}, ${item.order}")
            }
            _loaded.value = true
        }
    }

    fun deleteItem(item: CheckItem) {
        items.remove(item)
        viewModelScope.launch(Dispatchers.IO) {
            checkListRepository.deleteCheckItem(item)
        }
    }

    fun appendItem(item: CheckItem) {
        item.bunchId = bunch.id
        items.add(item)
        viewModelScope.launch(Dispatchers.IO) {
            checkListRepository.updateCheckItem(item)
        }
    }

    fun updateItem(item: CheckItem, deferredMs: Long, forced: Boolean) {
        if (item.contents == item.contents_data.value && !forced) {
            return
        }
        item.contents = item.contents_data.value.toString()

        deferredUpdateItemJob?.let {
            if (it.isActive && !it.isCompleted)
                it.cancel()
        }

        deferredUpdateItem?.let {
            if (it.id == item.id)
                return@let

            viewModelScope.launch(Dispatchers.IO) {
                Timber.d("[ITEM Update] START SINGLE")
                checkListRepository.updateCheckItem(it)
                Timber.d("[ITEM Update] END SINGLE")
            }
        }

        deferredUpdateItemJob = viewModelScope.launch(Dispatchers.IO) {
            Timber.d("[ITEM Update] START")
            delay(deferredMs)
            checkListRepository.updateCheckItem(item)
            Timber.d("[ITEM Update] END")
        }
        deferredUpdateItem = item
    }

    fun updateTitle(@NotNull title: String, deferredMs: Long, forced: Boolean) {
        if (bunch.title == title && !forced)
            return
        bunch.title = title

        deferredUpdateTitleJob?.let {
            if (it.isActive && !it.isCompleted)
                it.cancel()
        }

        deferredUpdateTitleJob = viewModelScope.launch(Dispatchers.IO) {
            Timber.d("[BUNCH UPDATE] START")
            delay(deferredMs)
            checkListRepository.updateBunch(bunch)
            Timber.d("[BUNCH UPDATE] END")
        }
    }

    fun flushUpdate() {
        deferredUpdateTitleJob?.let { job ->
            if (job.isCompleted)
                return@let
            title.value?.let {
                updateTitle(it, 0, true)
            }
        }
        deferredUpdateItemJob?.let { job ->
            if (job.isCompleted)
                return@let
            deferredUpdateItem?.let {
                updateItem(it, 0, true)
            }
        }
    }
}

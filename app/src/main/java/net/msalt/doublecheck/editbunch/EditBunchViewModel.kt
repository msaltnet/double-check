package net.msalt.doublecheck.editbunch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.data.DoubleCheckDatabase
import org.jetbrains.annotations.NotNull
import timber.log.Timber

class EditBunchViewModel(private val database: DoubleCheckDatabase) : ViewModel() {
    val title = MutableLiveData("")
    val items = ArrayList<CheckItem>()
    var bunch: Bunch? = null
    var updateTitleJob: Job? = null

    fun start(bunchId: String) {
        if (bunchId == "") {
            CoroutineScope(Dispatchers.IO).launch {
                Bunch().apply {
                    bunch = this
                    database.bunchDao().upsert(this)
                }
                // debug temp
                val all = database.bunchDao().getAll()
                for (i in all)
                    Timber.d("Bunch: ${i.id}: ${i.title}")
            }
        } else {
            CoroutineScope(Dispatchers.Default).launch {
                bunch = database.bunchDao().getById(bunchId)
                bunch?.let {
                    title.postValue(it.title)

                    // debug temp
                    Timber.d("Bunch: ${it.id}: ${it.title}")
                }
            }
        }
    }

    fun updateTitle(@NotNull title: String, deferredMs: Long, forced: Boolean) {
        bunch?.let {
            if (it.title == title && !forced)
                return
            it.title = title
        }

        updateTitleJob?.let {
            if (it.isActive && !it.isCompleted)
                it.cancel()
        }

        updateTitleJob = viewModelScope.launch(Dispatchers.IO) {
            Timber.d("START update")
            delay(deferredMs)
            bunch?.let {
                database.bunchDao().upsert(it)
            }
            Timber.d("END update")
        }
    }

    fun flushUpdate() {
        updateTitleJob?.let {
            if (it.isCompleted)
                return
            title.value?.let {
                updateTitle(it, 0, true)
            }
        }
    }
}

package net.msalt.doublecheck.editbunch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import net.msalt.doublecheck.data.DoubleCheckDatabase
import timber.log.Timber

class EditBunchViewModel(private val database: DoubleCheckDatabase) : ViewModel() {
    val title = MutableLiveData("")
    val items = ArrayList<CheckItem>()
    var bunch: Bunch? = null

    fun start(bunchId: String) {
        if (bunchId == "") {
            CoroutineScope(Dispatchers.IO).launch {
                Bunch().apply {
                    bunch = this
                    database.bunchDao().insert(this)
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
}

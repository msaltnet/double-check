package net.msalt.doublecheck.edittask

import android.content.Context
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
    fun start(bunchId: String) {
        if (bunchId == "") {
            // create new bunch
            CoroutineScope(Dispatchers.IO).launch {
                database.bunchDao().insert(Bunch())
                val all = database.bunchDao().getAll()
                for (i in all)
                    Timber.d("Bunch: ${i.id}: ${i.title}")
            }
        } else {
            // load bunch
        }
    }
}

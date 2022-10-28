package net.msalt.doublecheck.edittask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.msalt.doublecheck.data.CheckItem

class EditTaskViewModel : ViewModel() {
    val title = MutableLiveData("")
    val items = ArrayList<CheckItem>()
}
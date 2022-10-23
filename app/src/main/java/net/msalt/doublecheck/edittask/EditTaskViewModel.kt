package net.msalt.doublecheck.edittask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.msalt.doublecheck.data.CheckItem

class EditTaskViewModel : ViewModel() {

    var title = "string"

    val items = ArrayList<CheckItem>()

}
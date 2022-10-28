package net.msalt.doublecheck.data

import androidx.lifecycle.MutableLiveData
import java.util.*

data class CheckItem(var contents: MutableLiveData<String> = MutableLiveData<String>(""), val id: String = UUID.randomUUID().toString(), var checked: Boolean = false)
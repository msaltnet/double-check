package net.msalt.doublecheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import net.msalt.doublecheck.edittask.EditBunchViewModel

@Suppress("UNCHECKED_CAST")
val DoubleCheckViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
            with(modelClass) {
                val application = checkNotNull(extras[APPLICATION_KEY]) as DoubleCheckApplication
                val database = application.database
                when {
                    isAssignableFrom(EditBunchViewModel::class.java) ->
                        EditBunchViewModel(database)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
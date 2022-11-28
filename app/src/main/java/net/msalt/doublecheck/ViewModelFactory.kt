package net.msalt.doublecheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import net.msalt.doublecheck.bunchdetail.BunchDetailViewModel
import net.msalt.doublecheck.bunchlist.BunchListViewModel
import net.msalt.doublecheck.editbunch.EditBunchViewModel

@Suppress("UNCHECKED_CAST")
val DoubleCheckViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val application = checkNotNull(extras[APPLICATION_KEY]) as DoubleCheckApplication
            val database = application.database
            when {
                isAssignableFrom(EditBunchViewModel::class.java) ->
                    EditBunchViewModel(database)
                isAssignableFrom(BunchListViewModel::class.java) ->
                    BunchListViewModel(database)
                isAssignableFrom(BunchDetailViewModel::class.java) ->
                    BunchDetailViewModel(database)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
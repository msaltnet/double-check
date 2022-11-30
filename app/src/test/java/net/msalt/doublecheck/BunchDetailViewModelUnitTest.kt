package net.msalt.doublecheck

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import net.msalt.doublecheck.bunchdetail.BunchDetailViewModel
import net.msalt.doublecheck.data.Bunch
import org.junit.*

import org.junit.Assert.*
import timber.log.Timber

@ExperimentalCoroutinesApi
class BunchDetailViewModelUnitTest {
    private val dispatcher = StandardTestDispatcher()

    @get:Rule
    val timberRule = TimberRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Test
    fun start_should_call_update_bunch_when_called_with_bunch() = runTest {
        var repo = FakeCheckListRepository()
        val viewModel = BunchDetailViewModel(repo)
        val bunch = Bunch()
        viewModel.start(bunch)
        val retrieved = repo.getBunch(bunch.id)
        assertEquals(bunch, retrieved)
    }
}
package net.msalt.doublecheck

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.msalt.doublecheck.bunchdetail.BunchDetailViewModel
import net.msalt.doublecheck.data.Bunch
import net.msalt.doublecheck.data.CheckItem
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

@ExperimentalCoroutinesApi
class BunchDetailViewModelUnitTest {
    private val dispatcher = StandardTestDispatcher()

    @get:Rule
    val timberRule = TimberRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun start_should_call_update_bunch_when_called_with_bunch() = runTest {
        val repo = FakeCheckListRepository()
        val viewModel = BunchDetailViewModel(repo)

        val bunch = Bunch()
        viewModel.start(bunch)

        val retrieved = repo.getBunch(bunch.id)
        assertEquals(bunch, retrieved)
    }

    @Test
    fun start_should_update_title_and_items_when_called_with_bunchId() = runTest {
        val repo = FakeCheckListRepository()
        val viewModel = BunchDetailViewModel(repo)

        val bunch = Bunch(title = "mango_title")
        val checkItem = CheckItem(bunchId = bunch.id, contents = "mango")
        repo.updateBunch(bunch)
        repo.updateCheckItem(checkItem)
        viewModel.start(bunch.id)

        assertEquals(viewModel.title.value, "mango_title")
        assertEquals(viewModel.items.value?.size, 1)
        assertEquals(viewModel.items.value?.get(0)?.id, checkItem.id)
        assertEquals(viewModel.items.value?.get(0)?.contents, checkItem.contents)
    }

    @Test
    fun toggleCheck_update_toggle_state_when_called_with_item() = runTest {
        val repo = FakeCheckListRepository()
        val viewModel = BunchDetailViewModel(repo)

        val bunch = Bunch(title = "mango_title")
        val checkItem = CheckItem(bunchId = bunch.id, contents = "mango")
        repo.updateBunch(bunch)
        repo.updateCheckItem(checkItem)
        viewModel.start(bunch.id)

        assertEquals(viewModel.items.value?.get(0)?.checked, false)
        viewModel.toggleCheck(viewModel.items.value?.get(0)!!)

        // Execute pending coroutines actions
        delay(10)
        advanceUntilIdle()

        assertEquals(viewModel.items.value?.get(0)?.checked, true)
        var targetCheckItem = repo.getCheckItems(bunchId = bunch.id)[0]
        assertEquals(targetCheckItem.checked, true)
        assertEquals(targetCheckItem.id, checkItem.id)

        assertEquals(viewModel.items.value?.get(0)?.checked, true)
        viewModel.toggleCheck(viewModel.items.value?.get(0)!!)

        assertEquals(viewModel.items.value?.get(0)?.checked, false)
        targetCheckItem = repo.getCheckItems(bunchId = bunch.id)[0]
        assertEquals(targetCheckItem.checked, false)
        assertEquals(targetCheckItem.id, checkItem.id)
    }

    @Test
    fun resetCheckState_update_check_state_when_called() = runTest {
        val repo = FakeCheckListRepository()
        val viewModel = BunchDetailViewModel(repo)

        val bunch = Bunch(title = "mango_title")
        val checkItem = CheckItem(bunchId = bunch.id, checked = true, order = 1)
        val checkItem2 = CheckItem(bunchId = bunch.id, checked = false, order = 0)
        val checkItem3 = CheckItem(bunchId = bunch.id, checked = true, order = 2)
        repo.updateBunch(bunch)
        repo.updateCheckItem(checkItem)
        repo.updateCheckItem(checkItem2)
        repo.updateCheckItem(checkItem3)
        viewModel.start(bunch.id)

        assertEquals(viewModel.items.value?.get(0)?.checked, false)
        assertEquals(viewModel.items.value?.get(1)?.checked, true)
        assertEquals(viewModel.items.value?.get(2)?.checked, true)
        viewModel.resetCheckState()
        assertEquals(viewModel.items.value?.get(0)?.checked, false)
        assertEquals(viewModel.items.value?.get(1)?.checked, false)
        assertEquals(viewModel.items.value?.get(2)?.checked, false)
        delay(0)

        val targetCheckItems = repo.getCheckItems(bunchId = bunch.id)
        Timber.d("targetCheckItems $targetCheckItems")
        assertEquals(targetCheckItems[0].checked, false)
        assertEquals(targetCheckItems[1].checked, false)
        assertEquals(targetCheckItems[2].checked, false)
    }

}
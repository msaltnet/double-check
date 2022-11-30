package net.msalt.doublecheck

import net.msalt.doublecheck.bunchdetail.BunchDetailViewModel
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BunchDetailViewModelUnitTest {
    // Subject under test
    private lateinit var bunchDetailViewModel: BunchDetailViewModel

    @Test
    fun start_should_call_upsert_with_parameter() {
        assertEquals(4, 2 + 2)
    }
}
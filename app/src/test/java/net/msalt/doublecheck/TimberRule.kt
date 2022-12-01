package net.msalt.doublecheck

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import timber.log.Timber
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TimberRule : TestWatcher() {

    private val printlnTree = object : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            println(
                "${
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                        .withZone(ZoneOffset.ofHours(9))
                        .format(Instant.now())
                } ${tag?.padEnd(23)}: $message"
            )
        }
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Timber.plant(printlnTree)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Timber.uproot(printlnTree)
    }
}

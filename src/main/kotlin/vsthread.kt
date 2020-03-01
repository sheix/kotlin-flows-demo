package info.sheix.flows

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import kotlin.concurrent.thread

fun main() = runBlocking {
    val howMuch = 300_000
    runThreads(howMuch)
    runCoroutines(howMuch)
}

suspend fun runCoroutines(howMuch: Int) {
    val startTime = Instant.now()
    var sum = 0
    repeat((1..howMuch).count()) { GlobalScope.launch { sum += 1 }.join() }

    val time = Instant.now().toEpochMilli() - startTime.toEpochMilli()

    println("coroutines: $time ms, sum = $sum")
}

private fun runThreads(howMuch: Int) {
    val startTime = Instant.now()
    var sum = 0
    repeat((1..howMuch).count()) { thread { sum += 1 }.join() }

    val time = Instant.now().toEpochMilli() - startTime.toEpochMilli()

    println("threads: $time ms, sum = $sum")
}

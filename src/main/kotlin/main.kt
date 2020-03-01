package info.sheix.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.Instant
import kotlin.system.measureTimeMillis

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    println("-FLOW DEMO--")
    flowDemo()
    println("------------")
    println("-ASYNC DEMO-")
    asyncDemo()
    println("------------")
    println("-CANCEL DMO-")
    cancelDemo()
}


private fun asyncDemo() = runBlocking {
    val startTime = Instant.now()

    val longInt = async { longMap(1000) }
    val anotherLongInt = async { longMap(5) }

    val longSum = longInt.await()
    var long2 : Int? = null
    if (longSum>500)
        long2 = anotherLongInt.await() + longSum

    val time = Instant.now().toEpochMilli() - startTime.toEpochMilli()

    println("Here is the answer ($long2) and it took $time milliseconds to get there")

    assert(time < 1100)
    assert(time > 1000)
    long2
}

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
private suspend fun flowDemo() {
    val flows = mapOf(
        "simple" to createFlow(),
        "buffered" to createFlow(withBuffer = true),
        "distinct" to createFlow(withDistinct = true),
        "both" to createFlow(true, true)
    )

    flows.forEach { flow ->

        println("Flow (${flow.key}) created, will run it now")

        println("it takes ${measureTimeMillis {
            runBlocking {
                flow.value.collect { println(it) }
            }
        }} milliseconds")
        //program cancellation friendly
        yield()

        println("Done with the (${flow.key}) flow")
    }
}

@ExperimentalCoroutinesApi
fun createFlow(withBuffer: Boolean = false, withDistinct: Boolean = false): Flow<Int> {
    var flow = listOf(1, 2, 2, 2, 2, 2, 3, 4, 5, 2)
        .asFlow()
        .onCompletion { println("Flow completed") }
    if (withBuffer) flow = flow.buffer(1)
    if (withDistinct) flow = flow.distinctUntilChanged()

    return flow.map { longMap(it) }
}


suspend fun longMap(i: Int): Int {
    delay(1000)
    return i * i
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
private suspend fun CoroutineScope.cancelDemo() {
    val job = launch { flowDemo() }
    delay(200)
    job.cancel("KillAllHumans!")
    println("Cancelling job now")
}







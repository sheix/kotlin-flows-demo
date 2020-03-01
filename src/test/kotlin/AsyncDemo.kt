import info.sheix.flows.createFlow
import info.sheix.flows.longMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.`should be greater than`
import org.amshove.kluent.`should be less than`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.Test
import reactor.core.publisher.test
import java.time.Instant.now


@ExperimentalCoroutinesApi
class AsyncDemo {

    @Test
    fun `GIVEN - simple async await WHEN - run in blocking test THEN - delays are skipped`() = runBlockingTest {
        val startTime = now()
        val longInt = async { longMap(11) }
        val anotherLongInt = async { longMap(12) }

        val longSum = longInt.await() + anotherLongInt.await()

        longSum `should equal` 265

        val time = now().toEpochMilli() - startTime.toEpochMilli()

        time `should be greater than` 1
        time `should be less than` 100
    }


    @Test
    fun `GIVEN - blocking function WHEN - is run THEN - test works`() = runBlockingTest {
        val number = 10
        val result = longMap(number)

        result `should equal` 100
    }

    @Test
    fun `GIVEN - flow WHEN - converted to flux and subscribed THEN - runs as expected`() {
        createFlow(withBuffer = true, withDistinct = false).
            .asFlux().doOnNext {
                println(it)
            }.test()
            .expectNext(1)
            .expectNext(4)
            .expectNext(4)
            .expectNext(4)
            .expectNext(4)
            .expectNext(4)
            .expectNext(9)
            .expectNext(16)
            .expectNext(25)
            .expectNext(4)
            .verifyComplete()
    }

}

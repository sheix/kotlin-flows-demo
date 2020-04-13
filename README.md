# kotlin-flows-demo

Demo of kotlin coroutines and integration with project reactor

## Setup

Take a look at [build.gradle] file, especially on it's depenecies section:

```groovy
dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.1'
    compile group: 'io.projectreactor', name: 'reactor-core', version: '3.3.1.RELEASE'
    compile group: 'io.projectreactor', name: 'reactor-kotlin-extensions', version: '1.0.0.M1'
    testImplementation 'org.amshove.kluent:kluent:1.51'
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation 'io.projectreactor:reactor-test:3.3.1.RELEASE'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.1'
}
```

Also, in order to work, you have to add the `spring.io` repository to repositories, in addition to `mavenCentral` and `jcenter`:

```groovy
    maven {
        url 'https://repo.spring.io/release/'
    }
```

## What is coroutine

Coroutines is asynchronous programming model introduced in kotlin. To learn more about coroutines, take a look in official [documentation](https://kotlinlang.org/docs/reference/coroutines-overview.html)

## Where coroutines is better than threads

Take a look at [vsthread.kt](src\main\kotlin\vsthread.kt). There is a mainfile which spawns large amount of threads and coroutines and measures the time it takes. Run it and see the difference. Coroutines is also less memory-consuming.

## What `async` does

You may take a look at method `asyncDemo` in [main.kt](src\main\kotlin\main.kt#asyncDemo). The method launches 2 asyncronous operations and awaits for them to finish and than returns a result of them together. Take a note that each async method runs independently of others, thus time measured should be around 1 second.

It's also woth to note, that tests, in general, shouldn't be blocking or take more time. When writting tests one should use `runBlockingTest` for test body. Beware that all `delay`s will be skipped, as stated in [example test](src\test\kotlin\AsyncDemo.kt) which looks very similar to `asyncDemo`, but takes only time needed to run the code.

## What is Flow

Flow is kotlin's take on Publisher-Subscriber pattern. In order to work, the project should enable coroutines. Flows are created with the cold builders (`Flow` class). Subscribe is triggered via `.collect()` method. For example, you may see `flowDemo` function in [main.kt](src\main\kotlin\main.kt#flowDemo) file. This method demonstrates flow creation, subscribing, flow operators and variants.

Flows in kotlin support cancellation, but in order to cancel the execution of the flow, it should provide exit points via `yield` instruction. [cancelDemo](src\main\kotlin\main.kt#cancelDemo) shows the example of cancellation, and `flowDemo` is written with `yield` separating the parts of it.

## Flow interop with projectreactor

Finally, kotlin Flows are interoperable with other reactive streams implementations. Here is an example of importing interop library in [build.gradle] file and using extensions methods - as in [AsyncDemo.kt](src\test\kotlin\AsyncDemo.kt) test.

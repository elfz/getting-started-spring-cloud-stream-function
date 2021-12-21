package com.elfz.gettingstartedscsf

import mu.KLogger
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import java.util.logging.Level.INFO
import kotlin.random.Random

@Configuration
class KafkaConfiguration(
    private val log: KLogger = KotlinLogging.logger {}
) {

    @Bean
    fun fizzBuzzProducer(): Supplier<Flux<Int>> = Supplier {
        Flux.interval(Duration.ofSeconds(5))
            .map { Random.nextInt(10000 - 1) + 1 }
            .log("m=fizzBuzzProducer")
    }

    @Bean
    fun fizzBuzzProcessor(): Function<Flux<Int>, Flux<String>> = Function {
        it.map { i -> evaluateFizzBuzz(i) }
            .log("m=fizzBuzzProcessor")
    }

    private fun evaluateFizzBuzz(value: Int) =
        when {
            value % 15 == 0 -> "FizzBuzz"
            value % 5 == 0 -> "Buzz"
            value % 3 == 0 -> "Fizz"
            else -> value.toString()
        }

    @Bean
    fun fizzBuzzConsumer(): Consumer<String> = Consumer {
        log.info { "m=fizzBuzzConsumer, value=$it" }
    }
}




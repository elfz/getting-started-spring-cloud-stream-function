package com.elfz.gettingstartedscsf

import mu.KLogger
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.support.MessageBuilder
import org.springframework.kafka.support.KafkaHeaders
import reactor.core.publisher.Flux
import java.util.function.Consumer
import java.util.function.Function

@Configuration
class KafkaConfiguration(
    private val bridge: StreamBridge,

) {

    private val log: KLogger = KotlinLogging.logger {}

    // @Bean
    // fun fizzBuzzProducer(): Supplier<Flux<Int>> = Supplier {
    //     Flux.interval(Duration.ofSeconds(5))
    //         .map { Random.nextInt(10000 - 1) + 1 }
    //         .log("m=fizzBuzzProducer")
    // }

    fun send(value: Int){
        try{
            val message = MessageBuilder.withPayload(value)
                .build()
            bridge.send("fizzBuzzProducer-out-0", message)
        } catch (ex: Exception) {
            throw Exception("Failed to send event: ${ex.message}", ex)
        }

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




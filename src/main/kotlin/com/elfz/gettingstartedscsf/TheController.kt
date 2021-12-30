package com.elfz.gettingstartedscsf

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events")
class TheController(
    private val configuration: KafkaConfiguration
) {

    @PostMapping
    fun send(@RequestParam value: Int){
        configuration.send(value)
    }

}

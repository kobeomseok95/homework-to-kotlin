package com.triple.homework.event.controller

import com.triple.homework.event.controller.request.EventRequest
import com.triple.homework.event.service.ReviewEventService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class EventRestController(
    private val reviewEventService: ReviewEventService,
) {

    @PostMapping("/events")
    fun events(@RequestBody @Valid eventRequest: EventRequest) {

    }
}
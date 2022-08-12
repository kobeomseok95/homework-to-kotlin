package com.triple.homework

import com.fasterxml.jackson.databind.ObjectMapper
import com.triple.homework.common.exception.GlobalExceptionHandler
import com.triple.homework.event.controller.EventRestController
import com.triple.homework.event.service.ReviewEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(value = [
    GlobalExceptionHandler::class,
    EventRestController::class,
])
abstract class RestControllerTest {

    @MockBean
    protected lateinit var reviewEventService: ReviewEventService

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper
}

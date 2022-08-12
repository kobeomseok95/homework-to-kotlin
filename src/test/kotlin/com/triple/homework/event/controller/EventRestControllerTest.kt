package com.triple.homework.event.controller

import com.triple.homework.RestControllerTest
import com.triple.homework.common.exception.ClientErrorCode
import com.triple.homework.event.controller.request.EventRequest
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

internal class EventRestControllerTest: RestControllerTest() {

    @DisplayName("이벤트 수신 - 실패 / type, action이 없을 경우")
    @Test
    fun event_fail_require_type_action() {

        val request = EventRequest(
            type = null,
            action = null,
            reviewId = null,
            userId = null,
            placeId = null,
            content = null,
            attachedPhotoIds = null,
        )

        mockMvc.post("/events") {
            content = objectMapper.writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.errors", hasSize<Int>(5)) }
            content { jsonPath("$.code", `is`(ClientErrorCode.INVALID_REQUEST.getCode())) }
            content { jsonPath("$.status", `is`(HttpStatus.BAD_REQUEST.value())) }
        }
    }
}
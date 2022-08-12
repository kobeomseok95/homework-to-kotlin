package com.triple.homework.event.controller.request

enum class ActionType(
    private val value: String,
) {

    ADD("ADD"),
    MOD("MOD"),
    DELETE("DELETE"),
    ;
}
package io.github.tymoteuszsielach.fixturefactory.model

import io.github.tymoteuszsielach.fixturefactory.model.Car

data class User (
    val firstName: String,
    val lastName: String,
    val age: Int,
    val car: Car,
    val names: List<String>,
    val garage: List<List<Car>>
)

package io.github.tymoteuszsielach.fixturefactory.model

import java.time.Instant
import javax.money.MonetaryAmount

data class Car(
    val brand: String,
    val model: String,
    val vin: VIN,
    val price: MonetaryAmount,
    val weight: Double,
    val productionDate: Instant
)


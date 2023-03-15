package io.github.tymoteuszsielach.fixturefactory

import io.github.tymoteuszsielach.fixturefactory.model.Car
import io.github.tymoteuszsielach.fixturefactory.model.User
import io.github.tymoteuszsielach.fixturefactory.model.VIN

@Fixture
interface Fixtures {

    fun sampleUser(): User
    fun sampleCar(weight:Double = 3.0, brand:String = "BMW"): Car

    fun sampleVin(): VIN = VIN("XFBG")
}

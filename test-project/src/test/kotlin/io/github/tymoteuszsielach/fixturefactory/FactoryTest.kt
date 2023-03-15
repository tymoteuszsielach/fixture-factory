package io.github.tymoteuszsielach.fixturefactory

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class FactoryTest {

    @Test
    fun `should create user instance` () {
        val factory = getFixtureFactory<Fixtures>()

        val user = factory.sampleUser()

        Assertions.assertEquals("BMW", user.car.brand)
        Assertions.assertEquals("XFBG", user.car.vin.vin)

    }

    @Test
    fun `should drive a car` () {
        val factory = getFixtureFactory<Fixtures>()
        val car = factory.sampleCar(brand = "BMW")

        println(car)


    }

}
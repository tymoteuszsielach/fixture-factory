package io.github.tymoteuszsielach.fixturefactory

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Random
import javax.money.MonetaryAmount
import org.javamoney.moneta.Money


object GeneratorUtils {

    private val random = Random()
    private val exludedRange = 57..65
    private val exludedRange1 = 90..97


    fun generateString():String {
        return random.ints(48, 122)
            .filter{ it !in exludedRange && it !in exludedRange1 }
            .limit(10)
            .collect( {StringBuilder()} , StringBuilder::appendCodePoint, StringBuilder::append )
            .toString()
    }

    fun generateInt():Int = random.nextInt()
    fun generateLong():Long = random.nextLong()
    fun generateFloat():Float = random.nextFloat() * 100.0f
    fun generateDouble():Double = random.nextDouble() * 100.0
    fun generateBigDecimal():BigDecimal = BigDecimal(generateDouble()).setScale(2, RoundingMode.HALF_UP)
    fun generateMonetary(): MonetaryAmount = Money.of(generateBigDecimal(), "PLN")

}
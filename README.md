Fixture Factory
--------------

Sample factory is a code generator helping by writing tests. 
It allows to create data class instances filled with random data.
Let's assume we have data class `Car` in the project.

```kotlin
data class Car(
    val brand: String,
    val model: String,
    val vin: VIN,
    val price: MonetaryAmount,
    val weight: Double,
    val productionDate: Instant
)

```

And we need an instance in the unit test case. 
First we create an interface for our factory. @Fixture annotation makes the magic.

```kotlin
@Fixture
interface Fixtures {
    fun sampleCar(): Car
}
```

Then, in the test class ...

```kotlin
 @Test
 fun `should drive a car` () {
    val factory = getFixtureFactory<Fixtures>()
    val car = factory.sampleCar()
    
    ...
 }
```

And a Car instance looks like this:

```ignorelang
Car(brand=JCEVoI8CTe, model=y6XSn20rih, vin=VIN(vin=8KNU1xyhqc), price=PLN 0.42, weight=51.495863092903974, productionDate=2023-03-14T20:21:59.179562100Z)
```

If you need more control over some properties, factory methods should define params with default values.

```kotlin
@Fixture
interface Fixtures {
    fun sampleCar(weight:Double = 3.0, brand:String = "BMW"): Car
}
```

And all instances have fixed `brand` and `weight` properties

```ignorelang
Car(brand=BMW, model=tTymibpcnC, vin=VIN(vin=E7XK8pbRwR), price=PLN 94.68, weight=3.0, productionDate=2023-03-14T20:33:56.981470400Z)
```


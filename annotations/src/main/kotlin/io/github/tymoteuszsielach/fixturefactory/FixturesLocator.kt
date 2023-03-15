package io.github.tymoteuszsielach.fixturefactory

import kotlin.reflect.full.createInstance


inline fun <reified T:Any> getFixtureFactory():T {
    if (T::class.annotations.contains(Fixture())) {
        val fqcn = "io.github.tymoteuszsielach.fixturefactory." + T::class.simpleName + "Impl"
        return Class.forName(fqcn).kotlin.createInstance() as T
    }
    throw RuntimeException("Class was not annotated")
}


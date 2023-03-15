package io.github.tymoteuszsielach.fixturefactory

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class FixtureProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FixtureProcessor(
            options = environment.options,
            logger = environment.logger,
            codeGenerator = environment.codeGenerator
        )
    }
}

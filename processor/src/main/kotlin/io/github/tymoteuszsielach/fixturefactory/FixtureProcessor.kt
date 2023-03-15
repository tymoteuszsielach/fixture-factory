package io.github.tymoteuszsielach.fixturefactory

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.asClassName
import java.io.StringWriter
import java.time.Instant
import java.time.OffsetDateTime

const val PACKAGE_NAME = "io.github.tymoteuszsielach.fixturefactory"
const val FILE_NAME = "GeneratedFixtures"

class FixtureProcessor(private val options: Map<String, String>,
                       private val logger: KSPLogger,
                       private val codeGenerator: CodeGenerator):SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation("$PACKAGE_NAME.Fixture")
            .filterIsInstance<KSClassDeclaration>()

        if (!symbols.iterator().hasNext()) return emptyList()

        logger.info("Annotation found")

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
            packageName = PACKAGE_NAME,
            fileName = FILE_NAME
        )

        val fileSpecBuilder = FileSpec.builder(PACKAGE_NAME, FILE_NAME)
        fileSpecBuilder.addImport(Instant::class.asClassName().packageName, Instant::class.asClassName().simpleName)
        fileSpecBuilder.addImport(OffsetDateTime::class.asClassName().packageName, OffsetDateTime::class.asClassName().simpleName)
        fileSpecBuilder.addImport(GeneratorUtils::class.asClassName().packageName, GeneratorUtils::class.asClassName().simpleName)
        
        symbols.forEach { it.accept(FactoryInterfaceVisitor(fileSpecBuilder, resolver, logger), Unit) }

        val fileSpec = fileSpecBuilder.build()

        val stringWriter = StringWriter()

        fileSpec.writeTo(stringWriter)

        file.write(stringWriter.toString().toByteArray())
        file.close()

        return symbols.filterNot { it.validate() }.toList()
    }

}

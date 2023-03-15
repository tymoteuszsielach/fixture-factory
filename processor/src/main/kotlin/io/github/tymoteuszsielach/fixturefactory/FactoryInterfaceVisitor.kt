package io.github.tymoteuszsielach.fixturefactory

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec


class FactoryInterfaceVisitor(private val file: FileSpec.Builder,
                              private val resolver: Resolver,
                              private val logger: KSPLogger
) : KSVisitorVoid() {

    val excludedMethods = listOf("toString", "hashCode", "equals")

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        logger.info("visitor started")

        val classBuilder = TypeSpec.classBuilder(classDeclaration.simpleName.asString() + "Impl")
        classBuilder.addSuperinterface(
            ClassName(
                classDeclaration.packageName.asString(),
                classDeclaration.simpleName.asString()
            )
        )

        val factoryMap = classDeclaration
            .getAllFunctions()
            .map { it.returnType!!.resolve() to it.simpleName.asString() }
            .filterNot { excludedMethods.contains(it.second) }
            .toMap()

        classDeclaration.getAllFunctions().iterator().forEachRemaining {
            visitFunctionDeclaration3(it, classBuilder, factoryMap)
        }

        file.addType(classBuilder.build())
    }

    private fun visitFunctionDeclaration3(
        function: KSFunctionDeclaration,
        classBuilder: TypeSpec.Builder,
        factoryMap: Map<KSType, String>
    ) {
        if (!function.isAbstract) return

        logger.info("function started ${function.simpleName.asString()}")
        val returnType: KSType = function.returnType!!.resolve()

        val constructor = resolver.getClassDeclarationByName(returnType.declaration.qualifiedName!!)!!
            .getConstructors().iterator().next()

        var codeBuffer:String = "return ${returnType.declaration.simpleName.asString()}("

        val inputParams = function.parameters.map { it.name!!.asString() }
        val paramSpecs = function.parameters.map {
            val paramType = it.type.resolve()
            ParameterSpec.builder(
                it.name!!.asString(),
                ClassName(
                    paramType.declaration.packageName.asString(),
                    paramType.declaration.simpleName.asString()
                )
            ).build()
        }

        constructor.parameters.forEach {
            codeBuffer += it.name!!.asString() + "=" + genExpression(factoryMap, inputParams, it.name!!.asString(), it.type.resolve()) + ","
        }
        codeBuffer += ")"

        logger.info("code buffer $codeBuffer")

        classBuilder.addFunction(
            FunSpec.builder(function.simpleName.asString())
                .addParameters(paramSpecs.asIterable())
                .addModifiers(KModifier.OVERRIDE)
                .returns(returnType.declaration.let {
                    ClassName(
                        it.packageName.asString(),
                        it.simpleName.asString()
                    )
                }
                )
                .addCode(CodeBlock.of(codeBuffer))
                .build()
        )
    }

    private fun genExpression(factoryMap: Map<KSType, String>, inputParams:List<String>, paramName:String, paramType: KSType):String {
        return if (inputParams.contains(paramName)) {
            paramName
        } else if (factoryMap.contains(paramType)) {
            factoryMap[paramType]!! + "()"
        } else {
            if (paramType.declaration.simpleName.asString() == "List") {
                "listOf(" + genExpression(
                    factoryMap,
                    inputParams,
                    "",
                    paramType.arguments.first().type!!.resolve()) + ")"
            } else {
                genValue(paramType)
            }
        }
    }

    private fun genValue(type: KSType): String {
        val typeName = type.declaration.qualifiedName?.asString()
        return when(typeName) {
            "kotlin.String" -> "GeneratorUtils.generateString()"
            "kotlin.Int" -> "GeneratorUtils.generateInt()"
            "kotlin.Float" -> "GeneratorUtils.generateFloat()"
            "kotlin.Double" -> "GeneratorUtils.generateDouble()"
            "java.math.BigDecimal" -> "GeneratorUtils.generateBigDecimal()"
            "javax.money.MonetaryAmount" -> "GeneratorUtils.generateMonetary()"
            "java.time.Instant" -> "Instant.now()"
            "java.time.OffsetDateTime" -> "OffsetDateTime.now()"
            else -> throw RuntimeException("cannot guess value of type $typeName")
        }
    }
}
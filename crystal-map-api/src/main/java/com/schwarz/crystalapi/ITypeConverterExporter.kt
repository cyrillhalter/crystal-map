package com.schwarz.crystalapi

import kotlin.reflect.KClass

interface ITypeConverterExporter {

    val typeConverters: Map<KClass<*>, ITypeConverter<*, *>>

    val typeConverterImportables: List<TypeConverterImportable>
}

data class TypeConverterImportable(
    val typeConverterInstanceClassName: ClassNameDefinition,
    val domainClassName: ClassNameDefinition,
    val mapClassName: ClassNameDefinition
)

data class ClassNameDefinition(
    val packageName: String,
    val className: String
)

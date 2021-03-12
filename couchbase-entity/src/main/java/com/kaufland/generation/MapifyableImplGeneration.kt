package com.kaufland.generation

import com.kaufland.model.entity.BaseEntityHolder
import com.kaufland.util.TypeUtil
import com.squareup.kotlinpoet.*
import kaufland.com.coachbasebinderapi.mapify.Mapifyable

object MapifyableImplGeneration {

    data class Config(val clazzName: String, val typeParam: TypeName, val fromMap: (FunSpec.Builder) -> FunSpec, val toMap: (FunSpec.Builder) -> FunSpec)

    fun typeSpec(config: Config) =
            TypeSpec.classBuilder(config.clazzName)
                    .addSuperinterface(TypeUtil.iMapifyable(config.typeParam))
                    .addFunction(config.fromMap(FunSpec.builder("fromMap")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("map", TypeUtil.mapStringAny())
                            .returns(config.typeParam)))
                    .addFunction(config.toMap(FunSpec.builder("toMap")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("obj", config.typeParam)
                            .returns(TypeUtil.mapStringAny())))
                    .build()

    fun typeSpec(holder: BaseEntityHolder) : TypeSpec {
        val config = Config(
                clazzName = "Mapper",
                typeParam = holder.entityTypeName,
                fromMap = { it.addStatement("return %T.create(map.toMutableMap())", holder.entityTypeName).build() },
                toMap = { it.addStatement("return obj.toMap()", holder.entityTypeName).build() })

        return typeSpec(config)
    }

    fun impl(holder: BaseEntityHolder): AnnotationSpec {
        return AnnotationSpec.builder(Mapifyable::class).addMember("value = %T::class", ClassName(holder.`package`, "${holder.entitySimpleName}.Mapper"))
                .build()
    }

}
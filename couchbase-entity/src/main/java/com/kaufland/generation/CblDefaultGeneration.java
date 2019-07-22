package com.kaufland.generation;

import com.kaufland.model.entity.BaseEntityHolder;
import com.kaufland.model.field.CblBaseFieldHolder;
import com.kaufland.model.field.CblFieldHolder;
import com.kaufland.util.TypeUtil;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.thoughtworks.qdox.model.JavaField;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

public class CblDefaultGeneration {

    public static MethodSpec addDefaults(BaseEntityHolder holder){
        MethodSpec.Builder builder = MethodSpec.methodBuilder("addDefaults").
                addModifiers(Modifier.PRIVATE).
                addParameter(TypeUtil.createMapStringObject(), "map");

        for (CblBaseFieldHolder fieldHolder : holder.getFieldConstants()) {

            if (fieldHolder.isDefault()) {
                builder.addStatement("map.put($N, " + getConvertedValue(fieldHolder.getTypeMirror(), fieldHolder.getDefaultValue())+")", fieldHolder.getConstantName());
            }
        }
        return builder.build();
    }

    public static CodeBlock addAddCall(String nameOfMap){
        return CodeBlock.builder().addStatement("addDefaults($N)", nameOfMap).build();
    }

    private static String getConvertedValue(TypeMirror clazz, String value) {

        if (clazz.toString().equals(String.class.getCanonicalName())) {
            return "\"" + value + "\"";
        }
        return value;
    }

}

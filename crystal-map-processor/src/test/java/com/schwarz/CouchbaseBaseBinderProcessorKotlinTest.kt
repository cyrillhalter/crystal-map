package com.schwarz

import com.schwarz.crystalprocessor.CoachBaseBinderProcessor
import com.schwarz.testdata.TestDataHelper
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Assert
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCompilerApi::class)
class CouchbaseBaseBinderProcessorKotlinTest {

    @Test
    fun testSuccessSimpleMapper() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("SimpleMapperTest"))

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessSimpleReduce() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithSimpleReduce"))

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessMapperWithGetterAndSetter() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("MapperWithGetterAndSetter"))

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessMapperWithTypeParam() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("MapperWithTypeParam"))

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessMapperWithNullable() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("MapperWithNullable"))

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessWithQueries() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithQueries"))

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSucessWithQueriesAndEnums() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithQueriesAndEnums"))

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessWithGenerateAccessor() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithGenerateAccessor"))

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessWithQueriesAndSuspendFunctions() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithQueries"), useSuspend = true)

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessWithGenerateAccessorAndSuspendFunctions() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithGenerateAccessor"), useSuspend = true)

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessDeprecatedGeneration() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithDeprecatedFields"), useSuspend = true)
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessDeprecatedWithReduceGeneration() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithDeprecatedFieldsAndReduce"), useSuspend = true)
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessDocIdGeneration() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithDocId"), useSuspend = true)
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testSuccessDocIdSegmentGeneration() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithDocIdAndDocIdSegments"), useSuspend = true)
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testFailedWrongDeprecatedGeneration() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithWrongConfiguredDeprecatedFields"), useSuspend = true)
        Assert.assertEquals(compilation.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        Assert.assertTrue(compilation.messages.contains("replacement [name2] for field [name] does not exists"))
    }

    @Test
    fun testSuccessDeprecatedClassGeneration() {
        val compilation = compileKotlin(TestDataHelper.clazzAsJavaFileObjects("EntityWithDeprecatedClass"), useSuspend = true)
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testKotlinAbstractGeneration() {
        val subEntity = SourceFile.kotlin(
            "Sub.kt",
            ENTITY_HEADER +
                "@Entity\n" +
                "@Fields(\n" +
                "Field(name = \"test\", type = String::class),\n" +
                "Field(name = \"type\", type = String::class, defaultValue = Sub.TYPE, readonly = true)\n" +
                ")\n" +
                "abstract class Sub {\n" +
                "\n" +
                " companion object {\n" +
                "        const val TYPE: String = \"DWG\"" +
                "}\n" +
                " abstract var test : String?\n" +
                "}"
        )

        val compilation = compileKotlin(subEntity)

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @OptIn(ExperimentalCompilerApi::class)
    @Test
    fun testKotlinAbstractGenerationWithLongFields() {
        val subEntity = SourceFile.kotlin(
            "Sub.kt",
            ENTITY_HEADER +
                "@Entity\n" +
                "@Fields(\n" +
                "Field(name = \"test_test_test\", type = String::class),\n" +
                "Field(name = \"type\", type = String::class, defaultValue = Sub.TYPE, readonly = true)\n" +
                ")\n" +
                "abstract class Sub {\n" +
                "\n" +
                " companion object {\n" +
                "        const val TYPE: String = \"DWG\"" +
                "}\n" +
                " abstract var testTestTest : String?\n" +
                "}"
        )

        val compilation = compileKotlin(subEntity)

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
    }

    @Test
    fun testKotlinSchemaGeneration() {
        val expected = File("src/test/resources/ExpectedSchema.txt").readLines()
        val testObject = SourceFile.kotlin(
            "TestObject.kt",
            "package com.kaufland.testModels\n" +
                "import com.schwarz.crystalapi.Field\n" +
                "import com.schwarz.crystalapi.Fields\n" +
                "import com.schwarz.crystalapi.SchemaClass\n" +
                "@SchemaClass\n" +
                "class TestObject"
        )
        val sub = SourceFile.kotlin(
            "Sub.kt",
            "package com.kaufland.testModels\n" +
                "import com.kaufland.testModels.TestObject\n" +
                "import com.schwarz.crystalapi.Field\n" +
                "import com.schwarz.crystalapi.Fields\n" +
                "import com.schwarz.crystalapi.SchemaClass\n" +
                "@SchemaClass\n" +
                "@Fields(\n" +
                "Field(name = \"test_test_test\", type = Number::class),\n" +
                "Field(name = \"type\", type = String::class, defaultValue = \"test\", readonly = true),\n" +
                "Field(name = \"list\", type = String::class, list = true),\n" +
                "Field(name = \"someObject\", type = TestObject::class),\n" +
                "Field(name = \"objects\", type = TestObject::class, list = true),\n" +
                ")\n" +
                "class Sub"
        )
        val compilation = compileKotlin(testObject, sub)

        val actual = compilation.generatedFiles.find { it.name == "SubSchema.kt" }!!.readLines()

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, compilation.exitCode)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testKotlinPrivateGeneration() {
        val subEntity = SourceFile.kotlin(
            "Sub.kt",
            ENTITY_HEADER +
                "@Entity\n" +
                "@Fields(\n" +
                "Field(name = \"test\", type = String::class),\n" +
                "Field(name = \"type\", type = String::class, defaultValue = Sub.TYPE, readonly = true)\n" +
                ")\n" +
                "class Sub {\n" +
                "\n" +
                " companion object {\n" +
                "        const val TYPE: String = \"DWG\"" +
                "}\n" +
                "}"
        )

        val compilation = compileKotlin(subEntity)

        Assert.assertEquals(compilation.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        Assert.assertTrue(compilation.messages.contains("Entity can not be final"))
    }

    @OptIn(ExperimentalCompilerApi::class)
    @Test
    fun testKotlinConstructorFailGeneration() {
        val subEntity = SourceFile.kotlin(
            "Sub.kt",
            ENTITY_HEADER +
                "@Entity\n" +
                "@Fields(\n" +
                "Field(name = \"test\", type = String::class),\n" +
                "Field(name = \"type\", type = String::class, defaultValue = Sub.TYPE, readonly = true)\n" +
                ")\n" +
                "open class Sub(a : String) {\n" +
                "\n" +
                " companion object {\n" +
                "        const val TYPE: String = \"DWG\"" +
                "}\n" +
                "}"
        )

        val compilation = compileKotlin(subEntity)

        Assert.assertEquals(compilation.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        Assert.assertTrue(compilation.messages.contains("Entity should not have a contructor"))
    }

    private fun compileKotlin(vararg sourceFiles: SourceFile, useSuspend: Boolean = false): JvmCompilationResult {
        return KotlinCompilation().apply {
            sources = sourceFiles.toList()

            // pass your own instance of an annotation processor
            annotationProcessors = listOf(CoachBaseBinderProcessor())
            correctErrorTypes = true
            jvmTarget = "17"

            kaptArgs["useSuspend"] = useSuspend.toString()
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()
    }

    companion object {
        const val ENTITY_HEADER: String =
            "package com.kaufland.testModels\n" +
                "\n" +
                "import com.schwarz.crystalapi.Entity\n" +
                "import com.schwarz.crystalapi.Field\n" +
                "import com.schwarz.crystalapi.Fields\n"
    }
}

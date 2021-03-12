package kaufland.com.demo.mapper

import kaufland.com.coachbasebinderapi.mapify.IMapifyable
import kaufland.com.coachbasebinderapi.mapify.Mapify
import kaufland.com.coachbasebinderapi.mapify.Mapifyable
import kaufland.com.coachbasebinderapi.mapify.Mapper
import kaufland.com.demo.entity.ProductEntity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.Serializable

@Mapper
class DummyMapperSource(simple: String = "test123") {

    @Mapify
    val myPrivateVal : String? = simple

    @Mapify
    private val innerObject : MyMapifyableTest = MyMapifyableTest()

    @Mapify
    private val innerObjectList : List<MyMapifyableTest> = listOf(MyMapifyableTest())

    @Mapify
    private val innerObjectMap : Map<String, MyMapifyableTest> = mapOf("test" to MyMapifyableTest())

    @Mapify
    val testSerializable : TestSerializable = TestSerializable(simple, 5)

    @Mapify
    private val product : ProductEntity? = null

    data class TestSerializable(val test1: String, val test2: Int) : Serializable

    @Mapifyable(MyMapifyableTest.Mapper::class)
    class MyMapifyableTest {

        class Mapper : IMapifyable<MyMapifyableTest> {
            override fun fromMap(map: Map<String, Any>): MyMapifyableTest {
                return MyMapifyableTest()
            }

            override fun toMap(obj: MyMapifyableTest): Map<String, Any> {
                return mapOf()
            }

        }
    }
}
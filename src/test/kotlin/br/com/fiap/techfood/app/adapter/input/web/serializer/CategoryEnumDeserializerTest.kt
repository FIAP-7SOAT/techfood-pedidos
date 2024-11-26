package br.com.fiap.techfood.app.adapter.input.web.serializer

import br.com.fiap.techfood.core.domain.enums.CategoryEnum
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryEnumDeserializerTest {

    private val objectMapper = ObjectMapper()

    init {
        // Registering the custom deserializer
        val module = SimpleModule()
        module.addDeserializer(CategoryEnum::class.java, CategoryEnumDeserializer())
        objectMapper.registerModule(module)
    }

    @Test
    fun `should deserialize valid category enum name`() {
        val json = "\"SNACK\""

        val category = objectMapper.readValue(json, CategoryEnum::class.java)

        assertEquals(CategoryEnum.SNACK, category)
    }

    @Test
    fun `should deserialize valid category enum name with lowercase input`() {
        val json = "\"snack\""

        val category = objectMapper.readValue(json, CategoryEnum::class.java)

        assertEquals(CategoryEnum.SNACK, category)
    }

    @Test
    fun `should deserialize valid category enum id`() {
        val json = "1"

        val category = objectMapper.readValue(json, CategoryEnum::class.java)

        assertEquals(CategoryEnum.SNACK, category)
    }

    @Test
    fun `should deserialize valid category enum id with string input`() {
        val json = "\"1\""

        val category = objectMapper.readValue(json, CategoryEnum::class.java)

        assertEquals(CategoryEnum.SNACK, category)
    }

    @Test
    fun `should throw exception for invalid category enum name`() {
        val json = "\"INVALID_CATEGORY\""

        val exception = assertThrows(IllegalArgumentException::class.java) {
            objectMapper.readValue(json, CategoryEnum::class.java)
        }

        assertTrue(exception.message!!.contains("Invalid category"))
    }

    @Test
    fun `should throw exception for invalid category enum id`() {
        val json = "999"

        val exception = assertThrows(IllegalArgumentException::class.java) {
            objectMapper.readValue(json, CategoryEnum::class.java)
        }

        assertTrue(exception.message!!.contains("Invalid category"))
    }

    @Test
    fun `should throw exception for invalid category enum id with string input`() {
        val json = "\"999\""

        val exception = assertThrows(IllegalArgumentException::class.java) {
            objectMapper.readValue(json, CategoryEnum::class.java)
        }

        assertTrue(exception.message!!.contains("Invalid category"))
    }

//    @Test
//    fun `should throw exception for null input`() {
//        val json = "null"
//        val exception = assertThrows<IllegalArgumentException> {
//            objectMapper.readValue(json, CategoryEnum::class.java)
//        }
//        assertTrue(exception.message!!.contains("Invalid category"))
//    }

    @Test
    fun `should throw exception for empty input`() {
        val json = "\"\""
        val exception = assertThrows<IllegalArgumentException> {
            objectMapper.readValue(json, CategoryEnum::class.java)
        }
                assertTrue(exception.message!!.contains("Invalid category"))
    }

}

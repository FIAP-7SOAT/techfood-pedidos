package br.com.fiap.techfood.app.adapter.input.web.serializer

import br.com.fiap.techfood.core.domain.enums.CategoryEnum
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException

class CategoryEnumDeserializer : JsonDeserializer<CategoryEnum>() {
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): CategoryEnum {
        val value = p.text.trim()

        try {
            return CategoryEnum.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            val id = value.toIntOrNull()
            id?.let {
                return CategoryEnum.values().firstOrNull { it.id == id }
                    ?: throw IllegalArgumentException("Invalid category ID: $id")
            }
            throw IllegalArgumentException("Invalid category: $value")
        }
    }
}

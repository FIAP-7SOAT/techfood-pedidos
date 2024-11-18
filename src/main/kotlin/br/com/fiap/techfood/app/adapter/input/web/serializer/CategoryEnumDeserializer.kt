package br.com.fiap.techfood.app.adapter.input.web.serializer

import br.com.fiap.techfood.core.domain.enums.CategoryEnum
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class CategoryEnumDeserializer : JsonDeserializer<CategoryEnum>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): CategoryEnum? {
        val value = parser.text
        return CategoryEnum.entries.find { it.name == value }
            ?: throw IllegalArgumentException("Invalid category: $value")
    }
}
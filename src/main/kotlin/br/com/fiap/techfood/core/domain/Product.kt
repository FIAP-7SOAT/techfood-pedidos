package br.com.fiap.techfood.core.domain

import br.com.fiap.techfood.app.adapter.input.web.serializer.CategoryEnumDeserializer
import br.com.fiap.techfood.core.domain.enums.CategoryEnum
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.math.BigDecimal
import java.util.UUID

data class Product(
    val id: UUID?,
    var name: String?,
    var description: String?,
    var price: BigDecimal?,
    @JsonDeserialize(using = CategoryEnumDeserializer::class)
    var category: CategoryEnum?,
    var imageURL: String?,
)
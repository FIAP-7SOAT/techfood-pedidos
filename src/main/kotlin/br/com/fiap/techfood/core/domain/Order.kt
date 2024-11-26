package br.com.fiap.techfood.core.domain

import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import java.util.*

data class Order(
    val id: UUID,
    val name: String,
    val items: List<OrderItem> = listOf(),
    var status: OrderStatusEnum,
    val isAnonymous: Boolean,
    val clientId: UUID?,
    var timeToPrepare: Int?
)

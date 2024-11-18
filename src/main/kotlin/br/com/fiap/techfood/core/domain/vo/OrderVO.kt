package br.com.fiap.techfood.core.domain.vo

import br.com.fiap.techfood.core.domain.OrderItem
import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum

data class OrderVO(
    var name: String,
    var items: List<OrderItem>,
    var status: OrderStatusEnum,
    var isAnonymous: Boolean,
)
package br.com.fiap.techfood.app.adapter.input.web.order.dto

import br.com.fiap.techfood.core.domain.OrderItem
import org.jetbrains.annotations.NotNull

data class WebOrderRequest(
    @NotNull
    var orderItems: List<OrderItem>? = null,
    @NotNull
    var orderName: String? = null,
    var clientCpf: String? = null
)
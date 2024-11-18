package br.com.fiap.techfood.core.domain

data class DomainOrderRequest(
    var orderItems: List<OrderItem>? = null,
    var orderName: String? = null,
    var clientCpf: String? = null
)
package br.com.fiap.techfood.app.adapter.input.web.order.mapper

import br.com.fiap.techfood.app.adapter.input.web.order.dto.WebOrderRequest
import br.com.fiap.techfood.app.adapter.input.web.order.dto.WebOrderResponse
import br.com.fiap.techfood.core.domain.DomainOrderRequest
import br.com.fiap.techfood.core.domain.Order
import br.com.fiap.techfood.core.domain.OrderItem

fun Order.toOrderResponse(): WebOrderResponse =
    WebOrderResponse(
        id = this.id,
        name = this.name,
        items = this.items,
        status = this.status,
        isAnonymous = this.isAnonymous,
        clientId = this.clientId,
        timeToPrepare = this.timeToPrepare
    )

fun WebOrderRequest.toDomainOrderRequest(): DomainOrderRequest {
    return DomainOrderRequest(
        orderItems = this.orderItems?.map { it.toDomainOrderItem() },
        orderName = this.orderName,
        clientCpf = this.clientCpf
    )
}

fun OrderItem.toDomainOrderItem(): OrderItem {
    return OrderItem(
        quantity = this.quantity,
        productId = this.productId,
        description = this.description
    )
}

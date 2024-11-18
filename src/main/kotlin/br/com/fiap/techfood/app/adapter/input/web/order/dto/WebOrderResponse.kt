    package br.com.fiap.techfood.app.adapter.input.web.order.dto

    import br.com.fiap.techfood.core.domain.OrderItem
    import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
    import java.util.*

    data class WebOrderResponse (
        var id: UUID,
        var name: String? = null,
        var items: List<OrderItem>? = null,
        var status: OrderStatusEnum = OrderStatusEnum.AWAITING_PAYMENT,
        var isAnonymous: Boolean? = true,
        var clientId: UUID? = null,
        val timeToPrepare: Int? = null
    )
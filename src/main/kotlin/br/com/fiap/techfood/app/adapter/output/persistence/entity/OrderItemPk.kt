package br.com.fiap.techfood.app.adapter.output.persistence.entity

import jakarta.persistence.*
import java.util.*

@Embeddable
data class OrderItemPk(
    @Column(name = "order_id", nullable = false)
    var orderId: UUID? = null,

    @Column(name = "product_id", nullable = false)
    var productId: UUID? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderItemPk) return false
        return orderId == other.orderId && productId == other.productId
    }

    override fun hashCode(): Int {
        return Objects.hash(orderId, productId)
    }
}


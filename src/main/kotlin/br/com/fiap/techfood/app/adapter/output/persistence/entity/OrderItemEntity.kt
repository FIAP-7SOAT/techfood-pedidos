package br.com.fiap.techfood.app.adapter.output.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "TB_ORDER_ITEMS")
data class OrderItemEntity(
    @EmbeddedId
    var id: OrderItemPk = OrderItemPk(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    var order: OrderEntity? = null,

    @Column(nullable = false)
    var quantity: Int? = null,

    var description: String? = null
) {
    override fun toString(): String {
        return "OrderItemEntity(id=$id, quantity=$quantity, description=$description)"  // Avoid printing the order reference
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderItemEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}


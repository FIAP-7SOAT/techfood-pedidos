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
    @JsonBackReference // Prevent infinite recursion during serialization
    var order: OrderEntity? = null,  // Make sure order is set

    @Column(nullable = false)
    var quantity: Int? = null,

    var description: String? = null
) {
    // Override the toString method to avoid printing the order reference
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


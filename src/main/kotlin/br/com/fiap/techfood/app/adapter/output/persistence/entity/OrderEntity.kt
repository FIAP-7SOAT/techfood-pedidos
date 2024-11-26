package br.com.fiap.techfood.app.adapter.output.persistence.entity

import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "TB_ORDERS")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID,

    @Column(nullable = false)
    var name: String? = null,

    @Column(nullable = false)
    var status: OrderStatusEnum,

    var isAnonymous: Boolean? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    var items: MutableList<OrderItemEntity>? = mutableListOf(),

    var timeToPrepare: Int? = null,

    @Column(nullable = true)
    var clientId: UUID? = null,

    @Column(nullable = false)
    var creationDate: LocalDateTime? = null,

    @Column(nullable = false)
    var lastUpdateDate: LocalDateTime? = null
){
    override fun toString(): String {
        return "OrderEntity(id=$id, name=$name, status=$status, itemsCount=${items?.size})"  // Avoid printing the items list
    }
}

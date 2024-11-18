package br.com.fiap.techfood.app.adapter.output.persistence.mapper

import br.com.fiap.techfood.app.adapter.output.persistence.entity.OrderEntity
import br.com.fiap.techfood.app.adapter.output.persistence.entity.OrderItemEntity
import br.com.fiap.techfood.app.adapter.output.persistence.entity.OrderItemPk
import br.com.fiap.techfood.core.domain.Order
import br.com.fiap.techfood.core.domain.OrderItem
import java.time.LocalDateTime
import java.util.*


fun Order.toEntity(existingEntity: OrderEntity? = null): OrderEntity {
    val orderEntity = existingEntity ?: OrderEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        isAnonymous = this.isAnonymous,
        clientId = this.clientId,
        creationDate = LocalDateTime.now(),
        lastUpdateDate = LocalDateTime.now(),
        items = mutableListOf(),
        timeToPrepare = this.timeToPrepare ?: 0
    )

    // Clear existing items and add new ones
    orderEntity.items.clear()
    orderEntity.items.addAll(
        (this.items ?: listOf()).map { item ->
            val orderItemEntity = item.toEntity(orderEntity.id)
            orderItemEntity.order = orderEntity
            orderItemEntity
        }
    )

    orderEntity.lastUpdateDate = LocalDateTime.now()
    return orderEntity
}



fun OrderItem.toEntity(orderId: UUID): OrderItemEntity {
    return OrderItemEntity(
        id = OrderItemPk(
            orderId = orderId,
            productId = this.productId
        ),
        description = this.description,
        quantity = this.quantity
    )
}


fun OrderEntity.toDomain(): Order {
    return Order(
        id = this.id!!,
        name = this.name ?: "Unknown", // Provide a default value
        status = this.status,
        isAnonymous = this.isAnonymous ?: false, // Default to false if null
        clientId = this.clientId,
        items = this.items.map { it.toDomain() },
        timeToPrepare = this.timeToPrepare
    )
}


fun OrderItemEntity.toDomain(): OrderItem {
    return OrderItem(
        quantity = this.quantity ?: 0,
        productId = this.id.productId,
        description = this.description
    )
}


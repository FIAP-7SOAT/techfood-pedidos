package br.com.fiap.techfood.app.adapter.output.persistence

import br.com.fiap.techfood.app.adapter.output.persistence.mapper.toDomain
import br.com.fiap.techfood.app.adapter.output.persistence.mapper.toEntity
import br.com.fiap.techfood.app.adapter.output.persistence.repository.OrderItemRepository
import br.com.fiap.techfood.app.adapter.output.persistence.repository.OrderRepository
import br.com.fiap.techfood.core.common.annotation.PersistenceAdapter
import br.com.fiap.techfood.core.domain.Order
import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import br.com.fiap.techfood.core.port.output.OrderOutputPort
import org.springframework.transaction.annotation.Transactional
import java.util.*

@PersistenceAdapter
@Transactional
class OrderPersistenceService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository

    ) : OrderOutputPort {

    override fun save(order: Order): Order {
        // Fetch the existing OrderEntity if it exists
        val existingOrderEntity = orderRepository.findById(order.id).orElse(null)

        if (existingOrderEntity != null) {
            // Update only the status of the entity, avoid unnecessary re-mapping
            existingOrderEntity.status = order.status
        }

        // Convert Order to OrderEntity, preserving existing items if applicable
        val orderEntity = order.toEntity(existingOrderEntity)

        // Associate each OrderItemEntity with the OrderEntity
        orderEntity.items.forEach { item ->
            item.order = orderEntity
        }

        // Save the updated OrderEntity (this cascades to the items)
        val savedOrderEntity = orderRepository.save(orderEntity)

        // Return the saved Order as a domain object
        return savedOrderEntity.toDomain()
    }



    override fun findAllByStatus(status: OrderStatusEnum): List<Order> {
        return orderRepository.findAllByStatus(status).map { it.toDomain() }
    }

    override fun deleteById(id: UUID) {
        orderRepository.deleteById(id)
    }

    override fun findById(id: UUID): Order {
        return orderRepository.findById(id).orElseThrow { Exception("Order not found") }.toDomain()
    }
}
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
    private val orderRepository: OrderRepository
    ) : OrderOutputPort {

    override fun save(order: Order): Order {
        val existingOrderEntity = orderRepository.findById(order.id).orElse(null)

        if (existingOrderEntity != null) {
            existingOrderEntity.status = order.status
        }

        val orderEntity = order.toEntity(existingOrderEntity)

        orderEntity.items?.forEach { item ->
            item.order = orderEntity
        }

        val savedOrderEntity = orderRepository.save(orderEntity)

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
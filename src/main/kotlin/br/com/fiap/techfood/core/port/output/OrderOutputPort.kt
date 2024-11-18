package br.com.fiap.techfood.core.port.output

import br.com.fiap.techfood.core.domain.Order
import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import java.util.*

interface OrderOutputPort {
    fun save(order: Order): Order
    fun findAllByStatus(status: OrderStatusEnum): List<Order>
    fun deleteById(id: UUID)
    fun findById(id: UUID): Order
}
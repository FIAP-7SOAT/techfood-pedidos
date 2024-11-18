package br.com.fiap.techfood.core.port.input

import br.com.fiap.techfood.core.domain.Order
import br.com.fiap.techfood.core.domain.DomainOrderRequest
import java.util.*

interface OrderInputPort {
    fun save(orderRequest: DomainOrderRequest): Order
    fun findAllAwaitingPayment(): List<Order>
    fun findAllApprovedOrders(): List<Order>
    fun findAllPreparedOrders(): List<Order>
    fun findAllFinishedOrders(): List<Order>
    fun deleteOrder(id: UUID)
    fun approvePayment(id: UUID)
    fun prepareOrder(id: UUID)
    fun finishOrder(id: UUID)
}
package br.com.fiap.techfood.app.adapter.output.persistence

import br.com.fiap.techfood.app.adapter.output.persistence.entity.OrderEntity
import br.com.fiap.techfood.app.adapter.output.persistence.repository.OrderItemRepository
import br.com.fiap.techfood.app.adapter.output.persistence.repository.OrderRepository
import br.com.fiap.techfood.core.domain.Order
import br.com.fiap.techfood.core.domain.OrderItem
import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class OrderPersistenceServiceTest {

    private val orderRepository: OrderRepository = mock(OrderRepository::class.java)
    private val orderPersistenceService = OrderPersistenceService(orderRepository)

    @Test
    fun `save should update existing order status`() {
        val orderId = UUID.randomUUID()
        val existingOrderEntity = OrderEntity(
            id = orderId,
            name = "Test Order",
            status = OrderStatusEnum.AWAITING_PAYMENT,
            isAnonymous = false,
            clientId = UUID.randomUUID(),
            items = mutableListOf()
        )

        val order = Order(
            id = orderId,
            name = "Test Order",
            status = OrderStatusEnum.PAYMENT_APPROVED,
            isAnonymous = false,
            clientId = UUID.randomUUID(),
            items = listOf(OrderItem( UUID.randomUUID(), 1,UUID.randomUUID(),"Test Item")),
            timeToPrepare = 10
        )

        `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrderEntity))
        `when`(orderRepository.save(any(OrderEntity::class.java))).thenReturn(existingOrderEntity)

        val result = orderPersistenceService.save(order)

        assertNotNull(result)
        assertEquals(OrderStatusEnum.PAYMENT_APPROVED, result.status)
        verify(orderRepository).findById(orderId)
        verify(orderRepository).save(any(OrderEntity::class.java))
    }

    @Test
    fun `findById should return an order when it exists`() {
        val orderId = UUID.randomUUID()
        val orderEntity = OrderEntity(
            id = orderId,
            name = "Test Order",
            status = OrderStatusEnum.PAYMENT_APPROVED,
            isAnonymous = false,
            clientId = UUID.randomUUID(),
            items = mutableListOf()
        )

        `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity))

        val result = orderPersistenceService.findById(orderId)

        assertNotNull(result)
        assertEquals(orderId, result.id)
        assertEquals(OrderStatusEnum.PAYMENT_APPROVED, result.status)
        verify(orderRepository).findById(orderId)
    }

    @Test
    fun `findById should throw exception when order does not exist`() {
        val orderId = UUID.randomUUID()

        `when`(orderRepository.findById(orderId)).thenReturn(Optional.empty())

        val exception = assertThrows(Exception::class.java) {
            orderPersistenceService.findById(orderId)
        }

        assertEquals("Order not found", exception.message)
        verify(orderRepository).findById(orderId)
    }

    @Test
    fun `findAllByStatus should return a list of orders`() {
        val orderEntity = OrderEntity(
            id = UUID.randomUUID(),
            name = "Test Order",
            status = OrderStatusEnum.AWAITING_PAYMENT,
            isAnonymous = false,
            clientId = UUID.randomUUID(),
            items = mutableListOf()
        )

        `when`(orderRepository.findAllByStatus(OrderStatusEnum.AWAITING_PAYMENT)).thenReturn(listOf(orderEntity))

        val result = orderPersistenceService.findAllByStatus(OrderStatusEnum.AWAITING_PAYMENT)

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(OrderStatusEnum.AWAITING_PAYMENT, result[0].status)
        verify(orderRepository).findAllByStatus(OrderStatusEnum.AWAITING_PAYMENT)
    }

    @Test
    fun `deleteById should delete the order when called`() {
        val orderId = UUID.randomUUID()

        doNothing().`when`(orderRepository).deleteById(orderId)

        orderPersistenceService.deleteById(orderId)

        verify(orderRepository).deleteById(orderId)
    }

    @Test
    fun `findAllByStatus should return empty list when no orders exist`() {
        `when`(orderRepository.findAllByStatus(OrderStatusEnum.AWAITING_PAYMENT)).thenReturn(emptyList())

        val result = orderPersistenceService.findAllByStatus(OrderStatusEnum.AWAITING_PAYMENT)

        assertNotNull(result)
        assertTrue(result.isEmpty())
        verify(orderRepository).findAllByStatus(OrderStatusEnum.AWAITING_PAYMENT)
    }

}

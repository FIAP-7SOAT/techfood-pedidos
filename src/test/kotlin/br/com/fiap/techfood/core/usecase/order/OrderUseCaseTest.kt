import br.com.fiap.techfood.core.domain.*
import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import br.com.fiap.techfood.core.port.output.OrderOutputPort
import br.com.fiap.techfood.core.port.output.ClientServicePort
import br.com.fiap.techfood.core.port.output.ProductServicePort
import br.com.fiap.techfood.core.usecase.order.OrderUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class OrderUseCaseTest {

    private val orderOutput: OrderOutputPort = mockk()
    private val clientService: ClientServicePort = mockk()
    private val productService: ProductServicePort = mockk()
    private val orderUseCase = OrderUseCase(orderOutput, clientService, productService)

    @Test
    fun `save should throw exception when client not found`() {
        val orderRequest = mockk<DomainOrderRequest>()

        // Mocking the clientService to return null
        every { orderRequest.clientCpf } returns "12345678900"
        every { clientService.findClientByCpf("12345678900") } returns null

        assertThrows(Exception::class.java) {
            orderUseCase.save(orderRequest)
        }
    }

    @Test
    fun `findAllAwaitingPayment should return orders`() {
        val order = mockk<Order>()

        // Mocking the orderOutput to return a list containing 'order'
        every { orderOutput.findAllByStatus(OrderStatusEnum.AWAITING_PAYMENT) } returns listOf(order)

        val result = orderUseCase.findAllAwaitingPayment()

        assertEquals(1, result.size)
        assertEquals(order, result[0])
    }


    @Test
    fun `approvePayment should update status to approved`() {
        val order = mockk<Order>(relaxed = true)  // Relaxed mock for default property behavior
        val id = UUID.randomUUID()

        // Mocking orderOutput to return 'order' when findById is called
        every { orderOutput.findById(id) } returns order

        // Capturing the updated status
        val statusSlot = slot<OrderStatusEnum>()
        every { order.status = capture(statusSlot) } answers { }  // Capture status change

        // Mocking save to accept the order and return it
        every { orderOutput.save(order) } returns order

        orderUseCase.approvePayment(id)

        // Verifying that the status was set to PAYMENT_APPROVED
        assertEquals(OrderStatusEnum.PAYMENT_APPROVED, statusSlot.captured)

        // Verifying interactions with mocks
        verify {
            order.status = OrderStatusEnum.PAYMENT_APPROVED
            orderOutput.save(order)
        }
    }




    @Test
    fun `approvePayment should throw exception if order not found`() {
        val id = UUID.randomUUID()

        // Mocking findById to throw an exception
        every { orderOutput.findById(id) } throws Exception("Order not found")

        val exception = assertThrows<Exception> {
            orderUseCase.approvePayment(id)
        }
        assertEquals("Order not found", exception.message)
    }



    @Test
    fun `save should succeed when client is found`() {
        // Mock data setup
        val clientCpf = "12345678900"
        val clientId = UUID.fromString("18e32e8d-d9b9-43d2-b520-25432408bdff") // The expected UUID
        val productId = UUID.randomUUID()
        val orderRequest = mockk<DomainOrderRequest>()
        val client = mockk<Client>()
        val product = mockk<Product>()
        val orderItem = mockk<OrderItem>()
        val savedOrder = mockk<Order>()

        // Mock input and interactions
        every { orderRequest.clientCpf } returns clientCpf
        every { clientService.findClientByCpf(clientCpf) } returns client
        every { client.id } returns clientId

        every { orderRequest.orderItems } returns listOf(orderItem)
        every { orderItem.productId } returns productId
        every { productService.findProductById(productId) } returns product
        every { product.name } returns "Mock Product Name"

        // Mock the copy behavior for OrderItem
        val enrichedOrderItem = mockk<OrderItem>()
        every { orderItem.copy(description = "Mock Product Name") } returns enrichedOrderItem

        every { orderRequest.orderName } returns "Test Order"

        // Mock saved order's clientId and status
        every { savedOrder.clientId } returns clientId
        every { savedOrder.status } returns OrderStatusEnum.AWAITING_PAYMENT
        every { savedOrder.items } returns listOf(enrichedOrderItem)
        every { orderOutput.save(any()) } returns savedOrder

        // Execute use case
        val result = orderUseCase.save(orderRequest)

        // Verify interactions and results
        verify { clientService.findClientByCpf(clientCpf) }
        verify { productService.findProductById(productId) }
        verify { orderItem.copy(description = "Mock Product Name") }
        verify { orderOutput.save(any()) }

        assertNotNull(result)
        assertEquals(clientId, result.clientId) // Compare with expected UUID
        assertEquals(OrderStatusEnum.AWAITING_PAYMENT, result.status)
    }


//    @Test
//    fun `save should throw exception when orderItems is null or empty`() {
//        val orderRequest = mockk<DomainOrderRequest>()
//
//        // Case when orderItems is null
//        every { orderRequest.clientCpf } returns "12345678900"
//        every { orderRequest.orderItems } returns null
//
//        val exception = assertThrows<IllegalArgumentException> {
//            orderUseCase.save(orderRequest)
//        }
//        assertEquals("Os itens do pedido não podem ser nulos", exception.message)
//
//        // Case when orderItems is empty
//        every { orderRequest.orderItems } returns emptyList()
//        val emptyItemsException = assertThrows<IllegalArgumentException> {
//            orderUseCase.save(orderRequest)
//        }
//        assertEquals("O pedido deve conter pelo menos um item", emptyItemsException.message)
//    }


    @Test
    fun `prepareOrder should update status and timeToPrepare`() {
        val id = UUID.randomUUID()
        val order = mockk<Order>(relaxed = true)
        val orderItems = listOf(mockk<OrderItem>()) // At least one item

        // Mocking the order service
        every { orderOutput.findById(id) } returns order
        every { order.items } returns orderItems
        every { orderOutput.save(order) } returns order

        // Executing the use case
        orderUseCase.prepareOrder(id)

        // Verifying that the status is updated and timeToPrepare is set
        verify { order.status = OrderStatusEnum.PREPARED }
        verify { order.timeToPrepare = 10 } // Based on 1 item * 10 minutes
        verify { orderOutput.save(order) }
    }


    @Test
    fun `finishOrder should update status to finished`() {
        val id = UUID.randomUUID()
        val order = mockk<Order>(relaxed = true)

        // Mocking the order service
        every { orderOutput.findById(id) } returns order
        every { orderOutput.save(order) } returns order

        // Executing the use case
        orderUseCase.finishOrder(id)

        // Verifying that the status is updated to FINISHED
        verify { order.status = OrderStatusEnum.FINISHED }
        verify { orderOutput.save(order) }
    }


    @Test
    fun `calculateTimeToPrepare should calculate time correctly`() {
        val time = orderUseCase.calculateTimeToPrepare(3)
        assertEquals(30, time) // 3 items * 10 minutes per item
    }

    @Test
    fun `save should throw exception when product is not found`() {
        val orderRequest = mockk<DomainOrderRequest>()
        val client = mockk<Client>()
        val clientCpf = "12345678900"
        val productId = UUID.randomUUID()

        // Mocking orderRequest
        every { orderRequest.clientCpf } returns clientCpf

        // Create a mock for OrderItem and mock the productId
        val orderItem = mockk<OrderItem>()
        every { orderItem.productId } returns productId

        // Mocking orderItems to return the mocked OrderItem
        every { orderRequest.orderItems } returns listOf(orderItem)

        // Mock client service
        every { clientService.findClientByCpf(clientCpf) } returns client
        every { client.id } returns UUID.randomUUID()

        // Mocking productService to return null for a non-existing product
        every { productService.findProductById(productId) } returns null

        // Expecting exception when product is not found
        val exception = assertThrows<Exception> {
            orderUseCase.save(orderRequest)
        }
        assertEquals("Produto não encontrado para ID: $productId", exception.message)
    }




}

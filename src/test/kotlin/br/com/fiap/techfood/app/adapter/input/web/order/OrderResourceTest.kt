package br.com.fiap.techfood.app.adapter.input.web.order

import br.com.fiap.techfood.app.adapter.input.web.order.dto.WebOrderRequest
import br.com.fiap.techfood.app.adapter.input.web.order.mapper.toDomainOrderRequest
import br.com.fiap.techfood.core.domain.DomainOrderRequest
import br.com.fiap.techfood.core.domain.Order
import br.com.fiap.techfood.core.domain.OrderItem
import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import br.com.fiap.techfood.core.port.input.OrderInputPort
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

class OrderResourceTest {

    private val orderInputPort: OrderInputPort = mock(OrderInputPort::class.java)
    private val orderResource = OrderResource(orderInputPort)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(orderResource).build()
    private val objectMapper = ObjectMapper()

    @Test
    fun `createOrder should return bad request for invalid input`() {
        // Invalid request with missing orderName and orderItems
        val invalidRequest = """{
            "orderName": "",
            "clientCpf": "12345678900",
            "orderItems": []
        }"""

        mockMvc.perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `createOrder should return created order for valid input`() {
        // Valid request
        val validRequest = WebOrderRequest(
            orderName = "Pedido Teste",
            clientCpf = "12345678900",
            orderItems = listOf(
                OrderItem(
                    productId = UUID.randomUUID(),
                    quantity = 2
                )
            )
        )

        // Convert WebOrderRequest to DomainOrderRequest
        val domainOrderRequest = validRequest.toDomainOrderRequest()

        // Mock domainOrder response
        val domainOrder = Order(
            id = UUID.randomUUID(),
            name = validRequest.orderName ?: "Default Name",
            items = validRequest.orderItems?.map {
                OrderItem(
                    orderId = null,
                    productId = it.productId,
                    quantity = it.quantity,
                    description = "Produto Teste"
                )
            } ?: emptyList(),
            status = OrderStatusEnum.AWAITING_PAYMENT,
            isAnonymous = false,
            clientId = UUID.randomUUID(),
            timeToPrepare = 30
        )

        // Mock the service layer to return the domainOrder
        `when`(orderInputPort.save(domainOrderRequest)).thenReturn(domainOrder)

        mockMvc.perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(validRequest.orderName))
            .andExpect(jsonPath("$.status").value(OrderStatusEnum.AWAITING_PAYMENT.name))
            .andExpect(jsonPath("$.isAnonymous").value(false))
            .andExpect(jsonPath("$.timeToPrepare").value(30))
    }


    @Test
    fun `createOrder should return bad request if order name is missing`() {
        // Invalid request with missing orderName
        val invalidRequest = """{
            "orderName": "",
            "clientCpf": "12345678900",
            "orderItems": [{
                "productId": "b9b9a1d1-98ff-4d72-9f7d-8353bb88a4b8",
                "quantity": 2
            }]
        }"""

        mockMvc.perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `createOrder should return bad request if order items are missing`() {
        // Invalid request with missing orderItems
        val invalidRequest = """{
            "orderName": "Pedido Teste",
            "clientCpf": "12345678900",
            "orderItems": []
        }"""

        mockMvc.perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest)
        )
            .andExpect(status().isBadRequest)
    }

//    @Test
//    fun `createOrder should return internal server error for unexpected exception`() {
//        val validRequest = WebOrderRequest(
//            orderName = "Pedido Teste",
//            clientCpf = "12345678900",
//            orderItems = listOf(
//                OrderItem(
//                    productId = UUID.randomUUID(),
//                    quantity = 2
//                )
//            )
//        )
//
//        // Configura o comportamento do mock para lançar uma exceção
////        whenever(orderInputPort.save(org.mockito.kotlin.any())).thenThrow(RuntimeException("Unexpected error"))
////        whenever(orderInputPort.save(any<DomainOrderRequest>())).thenThrow(RuntimeException("Unexpected error"))
////        whenever(orderInputPort.save(argThat { it != null })).thenThrow(RuntimeException("Unexpected error"))
//        whenever(orderInputPort.save(any<DomainOrderRequest>())).thenThrow(RuntimeException("Unexpected error"))
//
//        mockMvc.perform(
//            post("/api/orders")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(validRequest))
//        )
//            .andExpect(status().isInternalServerError)
//    }






}

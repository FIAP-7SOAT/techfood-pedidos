package br.com.fiap.techfood.app.adapter.input.web.order

import br.com.fiap.techfood.app.adapter.input.web.order.dto.WebOrderRequest
import br.com.fiap.techfood.app.adapter.input.web.order.dto.WebOrderResponse
import br.com.fiap.techfood.app.adapter.input.web.order.mapper.toDomainOrderRequest
import br.com.fiap.techfood.app.adapter.input.web.order.mapper.toOrderResponse
import br.com.fiap.techfood.core.port.input.OrderInputPort
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/orders")
class OrderResource(
    private val orderInput: OrderInputPort
) {

    @PostMapping
    fun createOrder(@RequestBody orderRequest: WebOrderRequest): WebOrderResponse {
        val domainOrderRequest = orderRequest.toDomainOrderRequest()
        val order = orderInput.save(domainOrderRequest)
        return order.toOrderResponse()
    }

    @GetMapping("/awaiting-payment")
    fun findAllAwaitingPayment(): List<WebOrderResponse> {
        return orderInput.findAllAwaitingPayment().map { it.toOrderResponse() }
    }

    @GetMapping("/approved")
    fun findAllApprovedOrders(): List<WebOrderResponse> {
        return orderInput.findAllApprovedOrders().map { it.toOrderResponse() }
    }

    @GetMapping("/prepared")
    fun findAllPreparedOrders(): List<WebOrderResponse> {
        return orderInput.findAllPreparedOrders().map { it.toOrderResponse() }
    }

    @GetMapping("/finished")
    fun findAllFinishedOrders(): List<WebOrderResponse> {
        return orderInput.findAllFinishedOrders().map { it.toOrderResponse() }
    }

    @DeleteMapping("/{orderId}")
    fun deleteOrder(@PathVariable orderId: UUID) {
        orderInput.deleteOrder(orderId)
    }

    @PostMapping("/{orderId}/pay")
    fun approvePayment(@PathVariable orderId: UUID) {
        orderInput.approvePayment(orderId)
    }

    @PostMapping("/{orderId}/prepare")
    fun prepareOrder(@PathVariable orderId: UUID) {
        orderInput.prepareOrder(orderId)
    }

    @PostMapping("/{orderId}/finish")
    fun finishOrder(@PathVariable orderId: UUID) {
        orderInput.finishOrder(orderId)
    }

}
package br.com.fiap.techfood.core.usecase.order

import br.com.fiap.techfood.core.common.annotation.UseCase
import br.com.fiap.techfood.core.domain.DomainOrderRequest
import br.com.fiap.techfood.core.domain.Order
import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import br.com.fiap.techfood.core.port.input.OrderInputPort
import br.com.fiap.techfood.core.port.output.OrderOutputPort
import br.com.fiap.techfood.core.port.output.ClientServicePort
import br.com.fiap.techfood.core.port.output.ProductServicePort
import java.util.*

@UseCase
class OrderUseCase(
    private val orderOutput: OrderOutputPort,
    private val clientService: ClientServicePort,
    private val productService: ProductServicePort
) : OrderInputPort {

    override fun save(orderRequest: DomainOrderRequest): Order {
        // Verificar se o CPF foi fornecido, caso contrário, o pedido será anônimo
        val clientId: UUID? = if (orderRequest.clientCpf.isNullOrBlank()) {
            null  // Se o CPF não for informado, o pedido é anônimo
        } else {
            // Se CPF for informado, buscar o cliente associado
            val client = clientService.findClientByCpf(orderRequest.clientCpf!!)
            client?.id ?: throw Exception("Cliente não encontrado para o CPF: ${orderRequest.clientCpf}")
        }

        // Validar e enriquecer os itens do pedido
        val enrichedItems = orderRequest.orderItems?.map { item ->
            val product = productService.findProductById(item.productId)
                ?: throw Exception("Produto não encontrado para ID: ${item.productId}")
            item.copy(description = product.name)
        } ?: throw IllegalArgumentException("Os itens do pedido não podem ser nulos")

        // Garantir que o pedido tenha ao menos um item
        if (enrichedItems.isEmpty()) {
            throw IllegalArgumentException("O pedido deve conter pelo menos um item")
        }

        // Criar o pedido com o status inicial, identificando se é anônimo ou não
        val order = Order(
            id = UUID.randomUUID(),
            name = orderRequest.orderName ?: "Unknown", // Default value if orderName is null
            items = enrichedItems,
            status = OrderStatusEnum.AWAITING_PAYMENT,
            isAnonymous = clientId == null,  // Se o CPF não foi informado, o pedido é anônimo
            clientId = clientId,  // O clientId será nulo para pedidos anônimos
            timeToPrepare = calculateTimeToPrepare(enrichedItems.size)
        )

        // Salvar o pedido e retornar
        return orderOutput.save(order)
    }


    override fun findAllAwaitingPayment(): List<Order> {
        return orderOutput.findAllByStatus(OrderStatusEnum.AWAITING_PAYMENT)
    }

    override fun findAllApprovedOrders(): List<Order> {
        return orderOutput.findAllByStatus(OrderStatusEnum.PAYMENT_APPROVED)
    }

    override fun findAllPreparedOrders(): List<Order> {
        return orderOutput.findAllByStatus(OrderStatusEnum.PREPARED)
    }

    override fun findAllFinishedOrders(): List<Order> {
        return orderOutput.findAllByStatus(OrderStatusEnum.FINISHED)
    }

    override fun deleteOrder(id: UUID) {
        orderOutput.deleteById(id)
    }

    override fun approvePayment(id: UUID) {
        // Fetch the existing Order domain object
        val existingOrder = orderOutput.findById(id)
            ?: throw Exception("Order not found")


        // Update only the status
        existingOrder.status = OrderStatusEnum.PAYMENT_APPROVED

        // Save the updated Order domain object
        orderOutput.save(existingOrder)
    }

    override fun prepareOrder(id: UUID) {
        val order = orderOutput.findById(id)
        order.status = OrderStatusEnum.PREPARED
        order.timeToPrepare = calculateTimeToPrepare(order.items!!.size)
        orderOutput.save(order)
    }

    fun calculateTimeToPrepare(itemCount: Int): Int {
        val timePerItem = 10
        return itemCount * timePerItem
    }

    override fun finishOrder(id: UUID) {
        val order = orderOutput.findById(id)
        order.status = OrderStatusEnum.FINISHED
        orderOutput.save(order)
    }
}
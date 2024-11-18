package br.com.fiap.techfood.app.adapter.output.persistence.repository

import br.com.fiap.techfood.app.adapter.output.persistence.entity.OrderEntity
import br.com.fiap.techfood.core.domain.enums.OrderStatusEnum
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<OrderEntity, UUID> {
    fun findAllByStatus(status: OrderStatusEnum): List<OrderEntity>
}
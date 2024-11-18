package br.com.fiap.techfood.app.adapter.output.persistence.repository

import br.com.fiap.techfood.app.adapter.output.persistence.entity.OrderItemEntity
import br.com.fiap.techfood.app.adapter.output.persistence.entity.OrderItemPk
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItemEntity, OrderItemPk>  {

}
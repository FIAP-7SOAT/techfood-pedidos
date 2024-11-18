package br.com.fiap.techfood.core.port.output

import br.com.fiap.techfood.core.domain.Product
import java.util.*

interface ProductServicePort {
    fun findProductById(productId: UUID?): Product?
    fun getAllProducts(): List<Product>
}
package br.com.fiap.techfood.app.adapter.output.externalservice

import br.com.fiap.techfood.core.domain.Product
import br.com.fiap.techfood.core.port.output.ProductServicePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class ProductServiceAdapter(
    private val restTemplate: RestTemplate,
    @Value("\${product.service.base-url}") private val baseUrl: String

) : ProductServicePort {

    override fun findProductById(productId: UUID?): Product? {
        val url = "$baseUrl/api/products/$productId"
        return restTemplate.getForObject(url, Product::class.java)
    }

    override fun getAllProducts(): List<Product> {
        val url = "$baseUrl/api/products"
        return restTemplate.getForObject(url, Array<Product>::class.java)?.toList() ?: emptyList()
    }
}
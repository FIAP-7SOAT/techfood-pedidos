package br.com.fiap.techfood.app.adapter.output.externalservice

import br.com.fiap.techfood.core.domain.Client
import br.com.fiap.techfood.core.port.output.ClientServicePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ClientServiceAdapter(
    private val restTemplate: RestTemplate,
    @Value("\${client.service.base-url}") private val baseUrl: String
) : ClientServicePort {

    override fun findClientByCpf(cpf: String): Client? {
        val url = "$baseUrl/api/clients/cpf/$cpf"
        return restTemplate.getForObject(url, Client::class.java)
    }
}

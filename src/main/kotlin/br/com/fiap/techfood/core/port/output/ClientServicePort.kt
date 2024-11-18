package br.com.fiap.techfood.core.port.output

import br.com.fiap.techfood.core.domain.Client

interface ClientServicePort {
    fun findClientByCpf(cpf: String): Client?
}
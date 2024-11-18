package br.com.fiap.techfood.core.domain.enums

enum class OrderStatusEnum(val code: Int) {
    AWAITING_PAYMENT(1),
    PAYMENT_APPROVED(2),
    REJECTED(3),
    PREPARED(4),
    FINISHED(5)
}

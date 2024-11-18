package br.com.fiap.techfood.core.domain.enums

enum class CategoryEnum(val categoryName: String, val id: Int) {
    SNACK("Lanche", 1),
    SIDE_DISH("Acompanhamento", 2),
    DRINK("Bebida", 3),
    DESSERT("Sobremesa", 4),
    ;

    companion object {
        fun toEnum(code: Int?): CategoryEnum? {
            if (code == null) {
                return null
            }

            for (x in CategoryEnum.entries) {
                if (code == x.id) {
                    return x
                }
            }
            throw IllegalArgumentException("Category code invalid.")
        }
    }

}

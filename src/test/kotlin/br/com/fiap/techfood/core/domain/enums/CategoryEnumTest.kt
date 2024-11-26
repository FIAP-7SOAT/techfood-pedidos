package br.com.fiap.techfood.core.domain.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryEnumTest {

    @Test
    fun `toEnum should return correct CategoryEnum when valid id is provided`() {
        assertEquals(CategoryEnum.SNACK, CategoryEnum.toEnum(1))
        assertEquals(CategoryEnum.SIDE_DISH, CategoryEnum.toEnum(2))
        assertEquals(CategoryEnum.DRINK, CategoryEnum.toEnum(3))
        assertEquals(CategoryEnum.DESSERT, CategoryEnum.toEnum(4))
    }

    @Test
    fun `toEnum should return null when null id is provided`() {
        assertNull(CategoryEnum.toEnum(null))
    }

    @Test
    fun `toEnum should throw IllegalArgumentException when invalid id is provided`() {
        val exception = assertThrows<IllegalArgumentException> {
            CategoryEnum.toEnum(99)
        }
        assertEquals("Category code invalid.", exception.message)
    }

    @Test
    fun `enum values should have correct category names and ids`() {
        val snack = CategoryEnum.SNACK
        assertEquals("Lanche", snack.categoryName)
        assertEquals(1, snack.id)

        val sideDish = CategoryEnum.SIDE_DISH
        assertEquals("Acompanhamento", sideDish.categoryName)
        assertEquals(2, sideDish.id)

        val drink = CategoryEnum.DRINK
        assertEquals("Bebida", drink.categoryName)
        assertEquals(3, drink.id)

        val dessert = CategoryEnum.DESSERT
        assertEquals("Sobremesa", dessert.categoryName)
        assertEquals(4, dessert.id)
    }

    @Test
    fun `toEnum should iterate through all values without match`() {
        val invalidCode = -1
        val exception = assertThrows<IllegalArgumentException> {
            CategoryEnum.toEnum(invalidCode)
        }
        assertEquals("Category code invalid.", exception.message)
    }

}

package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.IngredientRequest
import com.grig.restapirecipes.security.RecipeSecurity
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForCollection
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

// Проверяем роли
@SpringBootTest
@ActiveProfiles("test") // для H2 и data-test.sql
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class IngredientServiceSecurityTest(@Autowired private val size: SizeValidatorForCollection) {

    @Autowired
    lateinit var ingredientService: IngredientService

//    @MockBean
//    lateinit var recipeSecurity: RecipeSecurity

    private val new_updateIngredint = IngredientRequest("New_Update ingredient", "new-update unit")

//    Get All ingredients
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can get all ingredients`() {
        ingredientService.getAllingredients()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can get all ingredients`() {
        ingredientService.getAllingredients()
    }

    @Test
    fun `NOT LOGGER can all ingredients`() {
        ingredientService.getAllingredients()
    }

//    JUnit 5 гарантирует, что:
//	1.	Метод выполнится полностью
//	2.	Ни одно исключение не будет выброшено
//	3.	Если любое исключение появится → тест упадёт
    @Test
    fun `NOT LOGGER IN - The method will be executed completely`() {
        assertDoesNotThrow { ingredientService.getAllingredients() }
    }

//  Get by id
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can get ingredient by id`() {
        ingredientService.getByIdIngredient(1L)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can get ingredient by id`() {
        ingredientService.getByIdIngredient(1L)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `NOT LOGGER IN can get ingredient by id`() {
        ingredientService.getByIdIngredient(1L)
    }

//    Create ingredient
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can create ingredient`() {
        ingredientService.createIngredient(new_updateIngredint)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can not create ingredient`() {
        assertThrows<AuthorizationDeniedException> {
            ingredientService.createIngredient(new_updateIngredint)
        }
    }

    @Test
    fun `NOT LOGGER IN can not create ingredient`() {
        assertThrows<AuthenticationCredentialsNotFoundException>{
            ingredientService.createIngredient(new_updateIngredint)
        }
    }

//    Update ingredient
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can update ingredient`() {
        ingredientService.updateIngredient(1L, new_updateIngredint)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can not update ingredient`() {
        assertThrows<AuthorizationDeniedException> {
            ingredientService.updateIngredient(1L, new_updateIngredint)
        }
    }

    @Test
    fun `NOT LOGGER IN can not update ingredient`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            ingredientService.updateIngredient(1L, new_updateIngredint)
        }
    }

//    Delete ingredient
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can delete ingredient`() {
        ingredientService.deleteIngredient(1L)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can not delete ingredient`() {
        assertThrows<AuthorizationDeniedException> {
            ingredientService.deleteIngredient(1L)
        }
    }

    @Test
    fun `NOT LOGGER IN can not delete ingredient`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            ingredientService.deleteIngredient(1L)
        }
    }

}
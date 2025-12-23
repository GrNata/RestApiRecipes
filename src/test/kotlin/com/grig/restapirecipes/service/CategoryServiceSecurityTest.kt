package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.CategoryRequest
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.security.RecipeSecurity
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

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class CategoryServiceSecurityTest {

    @Autowired
    lateinit var categoryService: CategoryService

//    @MockBean
//    lateinit var recipeSecurity: RecipeSecurity

    private val new_updateCategory = CategoryRequest("new-update-category", "http;//new_update_image")

//    Get all categories
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can all categories`() {
        categoryService.getAllCategories()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can all categories`() {
        categoryService.getAllCategories()
    }

    @Test
    fun `NOT LOGGER IN can all categories`() {
        categoryService.getAllCategories()
    }

//    JUnit 5 гарантирует, что:
//	1.	Метод выполнится полностью
//	2.	Ни одно исключение не будет выброшено
//	3.	Если любое исключение появится → тест упадёт
    @Test
    fun `NOT LOGGER IN - The method will be executed completely`() {
    //    Если метод что-то возвращает, лучше делать так:
        val result = assertDoesNotThrow { categoryService.getAllCategories() }
        assertEquals(3, result.size)
//    Если метод ничего не возвращает, лучше делать так:
//    assertDoesNotThrow { categoryService.getAllCategories() }
    }

//    Get category by id
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can category by id`() {
        categoryService.getByIdCategory(1L)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can category by id`() {
        categoryService.getByIdCategory(1L)
    }

    @Test
    fun `NOT LOGGER IN can category by id`() {
        categoryService.getByIdCategory(1L)
    }

//    Create category
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can create category`() {
        categoryService.createCategory(new_updateCategory)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can not create category`() {
        assertThrows<AuthorizationDeniedException> {
            categoryService.createCategory(new_updateCategory)
        }
    }

    @Test
    fun `NOT LOGGER IN can not create category`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            categoryService.createCategory(new_updateCategory)
        }
    }

//    Update category
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can update category`() {
        categoryService.updateCategory(1L, new_updateCategory)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can not update category`() {
        assertThrows<AuthorizationDeniedException> {
            categoryService.updateCategory(1L, new_updateCategory)
        }
    }

    @Test
    fun `NOT LOGGER IN can not update category`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            categoryService.updateCategory(1L, new_updateCategory)
        }
    }

//    Delete category
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can delete category`() {
        categoryService.deleteCategory(1L)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can not delete category`() {
        assertThrows<AuthorizationDeniedException> {
            categoryService.deleteCategory(1L)
        }
    }

    @Test
    fun `NOT LOGGER IN can not delete category`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            categoryService.deleteCategory(1L)
        }
    }

//    проверка исключений
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Delete not found category by id`() {
        assertThrows<RecipeNotFoundException> { categoryService.deleteCategory(99L) }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Update not found category by id`() {
        assertThrows<RecipeNotFoundException> { categoryService.updateCategory(99L, new_updateCategory) }
    }


}
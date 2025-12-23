package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.CreateRecipeRequest
import com.grig.restapirecipes.dto.request.UpdateRecipeRequest
import com.grig.restapirecipes.dto.response.RecipeDto
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.security.RecipeSecurity
import com.grig.restapirecipes.support.TestRequest
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.test.context.ActiveProfiles
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Проверяем роли
@SpringBootTest
@ActiveProfiles("test") // для H2 и data-test.sql
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class RecipeServiceSecurityTest {

    @Autowired
    lateinit var recipeService: RecipeService

    @MockBean
    lateinit var recipeSecurity: RecipeSecurity

    private val createRecipe = CreateRecipeRequest(
        name = "New",
        description = "New",
        image = null,
        categoryIds = emptyList(),
        ingredients = emptyList(),
        steps = emptyList()
    )

    val updateRecipe = UpdateRecipeRequest(
        name = "Update",
        description = null,
        image = null,
        ingredients = emptyList(),
        categoryIds = emptyList(),
        steps = emptyList()
    )

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

//    @Test
//    fun `print recipes in H2`() {
//        val recipes = jdbcTemplate.queryForList("SELECT * FROM recipe")
//        println("МОЙ ТЕСТ: ${recipes}")
//    }

    //    Update
    @Test
    @WithMockUser(roles = ["ADMIN"], username = "Admin")
    fun `ADMIN can update recipe`() {
        recipeService.updateRecipe(
            1L,
            TestRequest.validUpdateRecipeRequest()
            )
    }

    @Test
    @WithMockUser(roles = ["USER"], username = "user@mail.ru")
    fun `USER cannot update recipe`() {
        whenever(recipeSecurity.isAuthor(1L, "user@mail.ru"))
            .thenReturn(false)
        assertThrows<AccessDeniedException> {
            recipeService.updateRecipe(
                1L,
                TestRequest.validUpdateRecipeRequest()
            )
        }
    }

    @Test
    @WithMockUser(roles = ["USER"], username = "author@mail.ru")
    fun `USER author can update recipe`() {
        whenever(recipeSecurity.isAuthor(1L, "author@mail.ru"))
            .thenReturn(true)

            recipeService.updateRecipe(
                1L,
                TestRequest.validUpdateRecipeRequest()
            )
    }

    @Test
    fun `NOT LOGGED IN cannot update recipe`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            recipeService.updateRecipe(
                1L,
                TestRequest.validUpdateRecipeRequest()
            )
        }
    }

//    Get recipe by id
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can get recipe by id`() {
        recipeService.getRecipeById(1L)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can get recipe by id`() {
        recipeService.getRecipeById(1L)
    }

    @Test
    fun `NOT LOGGER IN can get recipe by id`() {
        recipeService.getRecipeById(1L)
    }

//    Create recipe
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can create recipe`() {
        recipeService.createRecipe(
            "admin@mail.ru",
            createRecipe
        )
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER  can create recipe`() {
        recipeService.createRecipe(
            "user@mail.ru",
            createRecipe
        )
    }

    @Test
    fun `NOT LOGGER IN cannot create recipe`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            recipeService.createRecipe(
                "notLogger@mail.ru",
                createRecipe
            )
        }
    }

//    Delete recipe
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can delete recipe`() {
        recipeService.deleteRecipe(1L)
    }

    @Test
    @WithMockUser(roles = ["USER"], username = "user@mail.ru")
    fun `USER cannot delete recipe`() {
        whenever(recipeSecurity.isAuthor(1L, "user@mail.ru"))
            .thenReturn(false)
        assertThrows<AuthorizationDeniedException> {
            recipeService.deleteRecipe(1L)
        }
    }

    @Test
    @WithMockUser(roles = ["USER"], username = "author@mail.ru")
    fun `USER is author can delete recipe`() {
        whenever(recipeSecurity.isAuthor(1L, "author@mail.ru"))
            .thenReturn(true)
        recipeService.deleteRecipe(1L)
    }

    @Test
    fun `NOT LOGGER IN cannot delete recipe`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            recipeService.deleteRecipe(1L)
        }
    }

//    Get All recipe
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can get all recipes`() {
        recipeService.getAllRecipes()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can get all recipes`() {
        recipeService.getAllRecipes()
    }

    @Test
    fun `NOT LOGGER IN can get all recipes`() {
        recipeService.getAllRecipes()
    }

    //    JUnit 5 гарантирует, что:
//	1.	Метод выполнится полностью
//	2.	Ни одно исключение не будет выброшено
//	3.	Если любое исключение появится → тест упадёт
    @Test
    fun `NOT LOGGER IN - The method will be executed completely`() {
        assertDoesNotThrow { recipeService.getAllRecipes() }
    }


    //    Search recipes with Pagination
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can search recipes with pagination`() {
        recipeService.searchRecipes(
            name = null,
            ingredient = null,
            page = 0,
            size = 10
        )
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can search recipes with pagination`() {
        recipeService.searchRecipes(
            name = null,
            ingredient = null,
            page = 0,
            size = 10
        )
    }

    @Test
    fun `NOT LOGGER IN can search recipes with pagination`() {
        recipeService.searchRecipes(
            name = null,
            ingredient = null,
            page = 0,
            size = 10
        )
    }

//    Search - комбинированный
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can search combi name or ingredient of recipe or all recipes`() {
        recipeService.search(null, null)
        recipeService.search("борщ", null)
        recipeService.search("борщ", "свекла")
        recipeService.search(null, "свекла")
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `USER can search combi name or ingredient of recipe or all recipes`() {
        recipeService.search(null, null)
        recipeService.search("борщ", null)
        recipeService.search("борщ", "свекла")
        recipeService.search(null, "свекла")
    }

    @Test
    fun `NOT LOGGER IN can search combi name or ingredient of recipe or all recipes`() {
        recipeService.search(null, null)
        recipeService.search("борщ", null)
        recipeService.search("борщ", "свекла")
        recipeService.search(null, "свекла")
    }

//    b) Некорректные данные
//	•	Отрицательные ID или несуществующие ID:
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `get recipe by non-exist ID throw RecipeNotFoundException`() {
        val exception = assertThrows<RecipeNotFoundException> {
            recipeService.getRecipeById(999L)
        }
        assertEquals("Recipe id = 999 not found", exception.message)

        val exception2 = assertThrows<RecipeNotFoundException> {
            recipeService.getRecipeById(-2L)
        }
        assertEquals("Recipe id = -2 not found", exception2.message)
    }

//    	•	Длинные строки (например, >255 символов для name):
//    @Test
//    @WithMockUser(roles = ["ADMIN"])
//    fun `create recipe with too long name throws exception`() {
//        val longName = "x".repeat(300)
//        val recipe = CreateRecipeRequest(longName, "desc", "img", emptyList(), emptyList(), emptyList())
//
//        val exception = assertThrows<DataIntegrityViolationException> {
//            recipeService.createRecipe("admin@mail.ru", recipe)
//        }
//        assertEquals("Recipe name is too long", exception.message)
//    }

//    Проверка на null или пустых значений
//    не надо валидация - @field:NotBlank name
//    @Test
//    @WithMockUser(roles = ["ADMIN"])
//fun `create recipe with null name throw exception`() {
//    val invalidRecipe = CreateRecipeRequest(
//        name = "",        //  стоит вадидация
//        description = "test",
//        image = "url",
//        categoryIds = emptyList(),
//        ingredients = emptyList(),
//        steps = emptyList()
//    )
//
//    val exception = assertThrows<ConstraintViolationException> {
//        recipeService.createRecipe("admin@mail.ru", invalidRecipe)
//        }
//    assertTrue(exception.message!!.contains("must not be blank"))
//    }

//    проверка на выброс исключений - IllegalArgumentException, RecipeNotFoundException, UserNotFoundException
    @Test
    fun `NOT found recipe by id`() {
        assertThrows<RecipeNotFoundException> { recipeService.getRecipeById(100L) }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Create not found user's email`() {
        assertThrows<IllegalArgumentException> { recipeService.createRecipe("wrong@mail.com", createRecipe) }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Update recipe not found by id`() {
        assertThrows<RecipeNotFoundException> { recipeService.updateRecipe(99L, updateRecipe) }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Delete recipe not found by id`() {
        assertThrows<RecipeNotFoundException> { recipeService.deleteRecipe(99L) }
    }


//    Проверка транзакций - Spring автоматически откатывает транзакции при RuntimeException в методах с @Transactional
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `create transaction rollback when user not found`() {
//        val invalidRecipe = RecipeDto("Test", "desc", "img", emptyList(), emptyList(), emptyList())
        assertThrows<kotlin.IllegalArgumentException> { recipeService.createRecipe("wronf@mail.com", createRecipe)  }


    // Проверяем, что рецепт не сохранился в БД
        val recipes = jdbcTemplate.queryForList("SELECT * FROM recipe")
//        assertTrue(recipes.isEmpty())
        assertEquals(recipeService.getAllRecipes().size, 3)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `update transaction rollback when recipe not found`() {
        assertThrows<RecipeNotFoundException> { recipeService.updateRecipe(99L, updateRecipe)  }

        // Проверяем, что рецепт не сохранился в БД
        assertThrows<RecipeNotFoundException> { recipeService.getRecipeById(99L)  }
    }

}
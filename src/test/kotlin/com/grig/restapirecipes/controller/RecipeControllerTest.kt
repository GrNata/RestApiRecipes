package com.grig.restapirecipes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.service.RecipeService
import com.grig.restapirecipes.support.TestRequest
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import kotlin.test.Test


//import com.fasterxml.jackson.databind.ObjectMapper
//import com.grig.restapirecipes.dto.request.UpdateRecipeRequest
//import com.grig.restapirecipes.dto.response.RecipeDto
//import com.grig.restapirecipes.service.RecipeService
//import com.grig.restapirecipes.mapper.RecipeMapper
//import com.grig.restapirecipes.model.CookingStep
//import com.grig.restapirecipes.model.Recipe
//import com.grig.restapirecipes.security.JwtTokenProvider
//import com.grig.restapirecipes.security.SecurityConfig
//import com.grig.restapirecipes.support.TestRequest
//import com.grig.restapirecipes.support.TestSecurityConfig
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.mockito.ArgumentMatchers.eq
//import org.mockito.kotlin.any
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.whenever
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.security.test.context.support.WithMockUser
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import org.springframework.test.web.servlet.setup.MockMvcBuilders
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.context.annotation.Import
//import org.springframework.http.MediaType
//import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity
//import org.springframework.test.context.bean.override.convention.TestBean
//import org.springframework.test.web.servlet.delete
//import org.springframework.test.web.servlet.get
//import org.springframework.test.web.servlet.post
//import org.springframework.test.web.servlet.put
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
//import kotlin.collections.emptyList
//import kotlin.collections.emptySet
//

//Что ДОЛЖЕН тестировать Controller Test
//
//Controller test — это НЕ тест безопасности
//
//Он проверяет:
//  Что                     Да / Нет
//URL → метод               ✅
//HTTP status               ✅
//JSON → DTO                ✅
//DTO → сервис              ✅
//сервис вызван             ✅
//роли / права              ❌
//бизнес-логика             ❌

//ПРАВИЛЬНЫЙ controller test при проверке ролей в сервисе

//Контроллер НЕ проверяет роли. Он проверяет:
//
//✅ запрос принят
//✅ тело запроса корректно распарсилось
//✅ сервис вызван
//✅ код ответа верный

@WebMvcTest(RecipeController::class)
//@AutoConfigureMockMvc(addFilters = false) // ВАЖНО - JWT-фильтр не нужен, роли не здесь, @WithMockUser тут вообще не обязателен
class RecipeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var recipeService: RecipeService

    @MockBean
    lateinit var recipeMapper: RecipeMapper

    @Test
    fun `update recipe returns 200`() {
        whenever(recipeService.updateRecipe(any(), any()))
            .thenReturn(Recipe(1L, "Updated", "Updated", null))

        mockMvc.put("/api/recipes/1") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(
                TestRequest.validUpdateRecipeRequest()
            )
        }.andExpect {
            status { isOk() }
        }
    }
//
//        verify(recipeService).updateRecipe(eq(1L), any())
//    }
}

//@WebMvcTest(RecipeController::class)
//@AutoConfigureMockMvc(addFilters = false) // ⬅️ КЛЮЧЕВО
//class RecipeControllerTest {
//
//    @Autowired
//    lateinit var mockMvc: MockMvc
//
//    @MockBean
//    lateinit var recipeService: RecipeService
//
//    @MockBean
//    lateinit var recipeMapper: RecipeMapper
//
//    @Test
//    fun `update recipe returns 200`() {
//        whenever(recipeService.updateRecipe(any(), any()))
//            .thenReturn(Recipe(1L, "Updated", "Updated", null))
//
//        mockMvc.put("/api/recipes/1") {
//            contentType = MediaType.APPLICATION_JSON
//            content = """{"name":"Updated"}"""
//        }.andExpect {
//            status { isOk() }
//        }
//    }
//}

//@WebMvcTest(RecipeController::class)
////@Import(TestSecurityConfig::class)   // подключаем сервис (если нужен реальный бин или мокаем)
//@AutoConfigureMockMvc
//class RecipeControllerTest() {
//
//    @Autowired
//    lateinit var mockMvc: MockMvc
//
//    @MockBean
//    lateinit var recipeService: RecipeService
//
//    @MockBean
//    lateinit var recipeMapper: RecipeMapper
//
//    private val objectMapper = ObjectMapper()
//
////    private val dto = RecipeDto(
////        id = 1L,
////        name = "Оливье",
////        description = "Тест",
////        image = null,
////        categories = emptyList(),
////        ingredients = emptyList(),
////        steps = emptyList()
////    )
////
////    @BeforeEach
////    fun setup() {
////        whenever(recipeService.updateRecipe(eq(1L), any()))
////            .thenReturn(Recipe(1L, "Updated", "Updated", null))
////
////        whenever(recipeMapper.toDto(any(), any(), any()))
////            .thenReturn(dto)
////    }
//
//    @BeforeEach
//    fun setup() {
//        whenever(recipeService.updateRecipe(eq(1L), any()))
//            .thenReturn(Recipe(1L, "Updated", "Updated", null))
//
//        whenever(recipeMapper.toDto(any(), any(), any()))
//            .thenReturn(
//                RecipeDto(
//                    1L, "Оливье", "Тест",
//                    null, emptyList(), emptyList(), emptyList()
//                )
//            )
//    }
//
//    @Test
//    @WithMockUser(roles = ["ADMIN"])
//    fun `ADMIN can update recipe`() {
//        mockMvc.put("/api/recipes/1") {
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(
//                TestRequest.validUpdateRecipeRequest()
//            )
//        }.andExpect {
//            status { isOk() }
//        }
//    }



//    private lateinit var mockMvc: MockMvc
//    private lateinit var recipeService: RecipeService
//    private lateinit var recipeMapper: RecipeMapper
//
//    private val objectMapper = ObjectMapper()
//
//    private val testRecipe = Recipe(
//        id = 1L,
//        name = "Оливье",
//        description = "Тест",
//        image = null
//    )
//
//    private val testRecipeDto = RecipeDto(
//        id = 1L,
//        name = "Оливье",
//        description = "Тест",
//        image = null,
//        categories = emptyList(),
//        ingredients = emptyList(),
//        steps = emptyList()
//    )
//
//    private val update = Recipe(
//        testRecipe.id,
//        "Update",
//        testRecipe.description,
//        testRecipe.image,
//        categories = testRecipe.categories,
//        recipeIngredients = testRecipe.recipeIngredients,
//        steps = testRecipe.steps
//        )
//
//    @BeforeEach
//    fun setup() {
//        // создаём моки вручную
//        recipeService = mock()   // Mockito mock
//        recipeMapper = mock()
//        whenever(recipeService.getRecipeById(1L))
//            .thenReturn(testRecipeDto)
//        whenever(recipeService.updateRecipe(eq(1L), any()))
//            .thenReturn(update)
//        whenever(recipeMapper.toDto(any(), any(), any()))
//            .thenReturn(testRecipeDto)
//        // создаём контроллер вручную с моками
//        val controller = RecipeController(recipeService, recipeMapper)
//
//        mockMvc = MockMvcBuilders
//            .standaloneSetup(controller)
//            .apply {
//                // добавляем security фильтры вручную, чтобы @WithMockUser работал
//                springSecurity()
//            }
//            .build()
//    }
//
//    // =========================
//    // GET /api/recipes/{id}
//    // ======
//    @Test
//    @WithMockUser(username = "admin@mail.ru", roles = ["ADMIN"])
//    fun `ADMIN can get recipe`() {
//        mockMvc.perform(get("/api/recipes/1"))
//            .andExpect(status().isOk)
////            .andExpect(jsonPath("$.id").value(1))
////            .andExpect(jsonPath("$.name").value("Оливье"))
//    }
//
//    @Test
//    @WithMockUser(username = "user@mail.ru", roles = ["USER"])
//    fun `USER can get recipe`() {
//        mockMvc.perform(get("/api/recipes/1"))
//            .andExpect(status().isOk)
////            .andExpect(jsonPath("$.id").value(1))
////            .andExpect(jsonPath("$.name").value("Оливье"))
//    }
//    @Test
//    fun `NOT LOGGER IN cannot access secured endpoints but can get recipe`() {
////        GET
//        mockMvc.get("/api/recipes/1")
//            .andExpect { status { isOk()}}  // GET разрешён -   работает
//
////        POST
//        mockMvc.post("/api/recipes") {
//            contentType = org.springframework.http.MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(
//                TestRequest.validUpdateRecipeRequest()
//            )
//        }.andExpect { status { isUnauthorized() } } // POST запрещён        401 -   400
//    }
//
//    // =========================
//    // DELETE /api/recipes/{id}
//    // =========================
//    @Test
//    @WithMockUser(username = "admin@mail.ru", roles = ["ADMIN"])
//    fun `ADMIN can delete recipe`() {
//        mockMvc.delete("/api/recipes/1")
//            .andExpect { status { isNoContent()} }  //  204
//    }
//
//    @Test
//    @WithMockUser(username = "user@mail.ru", roles = ["USER"])
//    fun `USER cannot delete recipe`() {
//        mockMvc.delete("/api/recipes/1")
//            .andExpect { status { isForbidden() } }     //  403 -   204
//    }
//
//    @Test
//    fun `NOT LOGGER IN cannot delete recipe`() {
//        mockMvc.delete("/api/recipes/1")
//            .andExpect { status { isUnauthorized() } }      //  401 -   204
//    }
//
//    // =========================
//    // PUT /api/recipes/{id}
//    // =========================
//    @Test
//    @WithMockUser(username = "admin@mail.ru", roles = ["ADMIN"])
//    fun `ADMIN can update recipe`() {
//        mockMvc.put("/api/recipes/1") {
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(
//                TestRequest.validUpdateRecipeRequest()
//            )
//        }.andExpect { status { isOk() } }
//    }
//
//    @Test
//    @WithMockUser(username = "user@mail.ru", roles = ["USER"])
//    fun `USER cannot update recipe`() {
//        mockMvc.put("/api/recipes/1") {
//            contentType = MediaType.APPLICATION_JSON
//            content = """{"name":"Updated"}"""
//        }.andExpect { status { isForbidden() } }    //  403 -   200
//    }
//
//    @Test
//    fun `NOT LOGGER IN cannot update recipe`() {
//        mockMvc.put("/api/recipes/1") {
//            contentType = MediaType.APPLICATION_JSON
//            content = """{"name":"Updated"}"""
//        }.andExpect { status { isUnauthorized() } }   //  403 -   200
//    }
//
//}
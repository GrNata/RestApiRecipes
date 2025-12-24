package com.grig.restapirecipes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.grig.restapirecipes.dto.request.CreateRecipeIngredientRequest
import com.grig.restapirecipes.dto.request.CreateRecipeRequest
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.service.RecipeService
import com.grig.restapirecipes.support.TestRequest
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.post

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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecipeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var recipeService: RecipeService

    @MockBean
    lateinit var recipeMapper: RecipeMapper

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @WithMockUser(roles = ["ADMIN"])
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

//    тестирование DataIntegrityViolationException
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `create recipe with too long name returns 409`() {
//        Заставляем сервис бросить исключение
        whenever(recipeService.createRecipe(any(), any()))
            .thenThrow(DataIntegrityViolationException("Recipe name is too long"))
    val longName = "x".repeat(300)
    val request = CreateRecipeRequest(longName, "desc", "img", listOf(1L), listOf(CreateRecipeIngredientRequest(1L, "3", 1L)), listOf("Step-1"))

    val json = objectMapper.writeValueAsString(request)
    mockMvc.post("/api/recipes") {
        contentType = MediaType.APPLICATION_JSON
        content = json
    }
        .andExpect {
            status { isConflict() }
            jsonPath("$.error").value("DATA_INTEGRITY_VIOLATION")
            jsonPath("$.status").value(409)
        }
    }
}

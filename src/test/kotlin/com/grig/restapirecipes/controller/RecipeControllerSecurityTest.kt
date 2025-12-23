package com.grig.restapirecipes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.grig.restapirecipes.dto.response.RecipeDto
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.security.CustomUserDetailsService
import com.grig.restapirecipes.security.JwtTokenProvider
import com.grig.restapirecipes.service.RecipeService
import com.grig.restapirecipes.support.TestRequest
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.Test

@WebMvcTest(RecipeController::class)
@AutoConfigureMockMvc
class RecipeControllerSecurityTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var recipeService: RecipeService

    @MockBean
    lateinit var recipeMapper: RecipeMapper

    @MockBean
    lateinit var jwtTokenProvider: JwtTokenProvider

    @MockBean
    lateinit var customUserDetailsService: CustomUserDetailsService

    private val objectMapper = ObjectMapper()

    private val testRecipeDto = RecipeDto(
        id = 1L,
        name = "Оливье",
        description = "Тест",
        image = null,
        categories = emptyList(),
        ingredients = emptyList(),
        steps = emptyList()
    )

    @BeforeEach
    fun setup() {
        recipeService = mock()
        recipeMapper = mock()
        whenever(recipeService.updateRecipe(eq(1L), any()))
            .thenReturn(Recipe(1L, "Updated", "Updated", null))
        whenever(recipeMapper.toDto(any(), any(), any()))
            .thenReturn(testRecipeDto)

        val controller = RecipeController(recipeService, recipeMapper)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .apply { springSecurity() } // подключаем фильтры Security
            .build()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `ADMIN can update recipe`() {
        mockMvc.put("/api/recipes/1") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(TestRequest.validUpdateRecipeRequest())
        }.andExpect {
            status { isOk() }
        }
    }


//    @Test
//    @WithMockUser(roles = ["USER"], username = "user@mail.ru")
//    fun `USER cannot update recipe`() {
////        whenever(recipeService.updateRecipe(eq(2L), any()))
////            .thenThrow(AccessDeniedException("Access is denied"))
//
//        val json = objectMapper.writeValueAsString(TestRequest.validUpdateRecipeRequest())
//
//        mockMvc.put("/api/recipes/2") {
//            contentType = MediaType.APPLICATION_JSON
//            content = json
//        }.andExpect {
//            status { isForbidden() }    //  403
//        }
//    }

    @Test
    fun `NOT LOGGED IN cannot update recipe`() {
        mockMvc.put("/api/recipes/1") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(TestRequest.validUpdateRecipeRequest())
        }.andExpect {
            status { isUnauthorized() }
        }
    }


}
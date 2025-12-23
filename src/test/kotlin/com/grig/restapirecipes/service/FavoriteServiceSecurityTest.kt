package com.grig.restapirecipes.service

import com.grig.restapirecipes.security.RecipeSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.access.AccessDeniedException

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class FavoriteServiceSecurityTest {

    @Autowired
    lateinit var favoriteService: FavoriteService

//    @MockBean
//    lateinit var recipeSecurity: RecipeSecurity

//    Add favorite
    @Test
    @WithMockUser(roles = ["ADMIN"], username = "admin@mail.ru")
    fun `ADMIN can add favorite`() {
        favoriteService.addFavorite("admin@mail.ru", 1L)
    }

    @Test
    @WithMockUser(roles = ["USER"], username = "user@mail.ru")
    fun `USER can add favorite`() {
        favoriteService.addFavorite("user@mail.ru", 1L)
    }

    @Test
    fun `NOT LOGGER IN can add favorite without email`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            favoriteService.addFavorite("", 1L)
        }
    }

    @Test
    fun `NOT LOGGER IN can add favorite with email`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            favoriteService.addFavorite("notLogger@mail.ru", 1L)
        }
    }

//    Delete favorite
    @Test
    @WithMockUser(roles = ["ADMIN"], username = "admin@mail.ru")
    fun `ADMIN can delete own favorite`() {
        assertDoesNotThrow {
            favoriteService.removeFavorite("admin@mail.ru", 1L)
        }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"], username = "admin@mail.ru")
    fun `ADMIN can delete not his own favorite`() {
        assertThrows<AccessDeniedException> {
            favoriteService.removeFavorite("user@mail.ru", 2L)
        }
    }

    @Test
    @WithMockUser(roles = ["USER"], username = "user@mail.ru")
    fun `USER can delete own favorite`() {
        assertDoesNotThrow {
            favoriteService.removeFavorite("user@mail.ru", 2L)
        }
    }

// ????????????
    @Test
    @WithMockUser(roles = ["USER"], username = "user@mail.ru")
    fun `USER can not delete not his own favorite`() {
        assertThrows<AccessDeniedException> {
            favoriteService.removeFavorite("admin@mail.ru", 1L)
        }
    }

    @Test
    fun `NOT LOGGER IN can not delete favorite`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            favoriteService.removeFavorite("admin@mail.ru", 1L)
        }
    }

    //  List favorites
    @Test
    @WithMockUser(roles = ["ADMIN"], username = "admin@mail.ru")
    fun `ADMIN can get list own favorites`() {
        favoriteService.listFavoritesByUser("admin@mail.ru")
    }

    @Test
    @WithMockUser(roles = ["USER"], username = "user@mail.ru")
    fun `USER can get list own favorites`() {
        favoriteService.listFavoritesByUser("user@mail.ru")
    }

    @Test
    fun `NOT LOGGER IN can not get list without email favorites`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            favoriteService.listFavoritesByUser("")
        }
    }

    @Test
    fun `NOT LOGGER IN can not get list with email_admin favorites`() {
        assertThrows<AuthenticationCredentialsNotFoundException> {
            favoriteService.listFavoritesByUser("admin@mail.ru")
        }
    }

}
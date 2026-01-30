package com.grig.restapirecipes.controller.admin

import com.grig.restapirecipes.user.dto.UserDto
import com.grig.restapirecipes.user.model.Role
import com.grig.restapirecipes.user.model.User
import com.grig.restapirecipes.user.request.BlockUserRequest
import com.grig.restapirecipes.user.request.UpdateUserRoleRequest
import com.grig.restapirecipes.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Users", description = "ADMIN - Методы для работы с users")
class UserAdminController(
    val userService: UserService
) {

    @GetMapping
    fun getAllUsers(): List<UserDto> = userService.getAllUsers()

    @PutMapping("/{id}/block")
    fun updateBlockedUser(@PathVariable id: Long, @RequestBody request: BlockUserRequest) =
        userService.blockUser(id, request.blocked)

    @PutMapping("/{id}/roles")
    fun updateRolesUser(@PathVariable id: Long, @RequestBody request: UpdateUserRoleRequest) =
        userService.updateRoles(id, request.roles)
}
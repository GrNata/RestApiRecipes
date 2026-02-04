package com.grig.restapirecipes.user.request

data class UpdateUserRoleRequest(
    val roles: Set<String>  //  ADMIN, USER
)

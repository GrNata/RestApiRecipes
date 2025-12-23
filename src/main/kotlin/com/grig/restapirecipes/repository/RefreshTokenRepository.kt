package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {

    fun findByToken(token: String) : RefreshToken?

//    fun save(token: RefreshToken)
}
package edu.example.learner_kotlin.token.repository

import edu.example.learner_kotlin.token.entity.RefreshEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : CrudRepository<RefreshEntity, String> {
    fun existsRefreshEntityBy(token: String): Boolean
}
package edu.example.learner_kotlin.token.repository

import edu.example.learner_kotlin.token.entity.RefreshEntity
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : KeyValueRepository<RefreshEntity, String> {
    fun existsRefreshEntityBy(token: String): Boolean
}
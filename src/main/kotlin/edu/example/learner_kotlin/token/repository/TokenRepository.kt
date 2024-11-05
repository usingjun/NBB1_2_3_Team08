package edu.example.learner_kotlin.token.repository

import edu.example.learner_kotlin.token.entity.RefreshEntity
import jakarta.transaction.Transactional
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface TokenRepository :  CrudRepository<RefreshEntity,Long>{
    fun existsRefreshEntityBy(token: String): Boolean

    @Transactional
    fun deleteRefreshEntityBy(token: String): Unit
}
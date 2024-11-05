package edu.example.learner_kotlin.token.util
import edu.example.learner_kotlin.token.entity.RefreshEntity
import edu.example.learner_kotlin.token.repository.TokenRepository
import org.springframework.stereotype.Component

@Component
class TokenUtil(private val tokenRepository: TokenRepository) {

    fun addRefreshEntity(username: String, refreshToken: String) {
        val refreshEntity = RefreshEntity(username = username, refreshToken = refreshToken)
        tokenRepository.save(refreshEntity)
    }
}

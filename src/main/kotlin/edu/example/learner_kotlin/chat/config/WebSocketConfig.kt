package edu.example.learner_kotlin.chat.config

import edu.example.learner_kotlin.chat.interceptor.StompHandler
import edu.example.learner_kotlin.log
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig (private val stompHandler: StompHandler) : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("http://localhost:3000")  // React 앱의 도메인만 허용
            .withSockJS() // ws으로 접속하면 SockJS를 사용하도록 설정
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/pub") // 클라이언트에서 메시지를 보낼 때 /pub으로 시작하는 메시지가 메시지 매핑에 사용
        registry.enableSimpleBroker("/sub") // /sub으로 시작하는 메시지가 메시지 브로커로 라우팅
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(stompHandler)
    }
}
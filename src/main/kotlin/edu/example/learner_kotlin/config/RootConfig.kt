package edu.example.learner_kotlin.config

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RootConfig {
    @Bean
    fun modelMapper() : ModelMapper {
        return ModelMapper().apply {
            with(configuration) {
//                setFieldMatchingEnabled(true)           // 자바에서의 boolean 타입 get/ set 규칙 -> is / set  ModelMapper는 자바로 이루어져있음  코틀린으로 선언시 get/set으로 인식되어있는 것만 가능
                isFieldMatchingEnabled = true             // 이렇게 변경 가능
                fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE
                matchingStrategy = MatchingStrategies.LOOSE
            }
        }
    }
}
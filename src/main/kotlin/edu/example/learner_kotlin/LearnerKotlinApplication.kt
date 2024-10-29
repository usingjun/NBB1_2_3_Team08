package edu.example.learner_kotlin

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LearnerKotlinApplication

// 모든 클래스에서 log4j2 확장
inline val <reified T> T.log : Logger
    get() = LogManager.getLogger()

fun main(args: Array<String>) {
    runApplication<LearnerKotlinApplication>(*args)
}

package edu.example.learner

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class LearnerApplication

// 모든 클래스에서 log4j2 확장
inline val <reified T> T.log : Logger
    get() = LogManager.getLogger()

fun main(args: Array<String>) {
    SpringApplication.run(LearnerApplication::class.java, *args)
}
package edu.example.learner_kotlin.advice

import edu.example.learner_kotlin.courseabout.coursereview.exception.ReviewTaskException
import edu.example.learner_kotlin.courseabout.exception.HeartNewsAlreadyExistsException
import edu.example.learner_kotlin.courseabout.exception.NotFoundException
import edu.example.learner_kotlin.courseabout.order.exception.OrderTaskException
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.exception.LoginTaskException
import edu.example.learner_kotlin.member.exception.MemberTaskException
import edu.example.learner_kotlin.token.exception.JWTTaskException
import edu.example.learner_kotlin.courseabout.course.exception.CourseTaskException
import edu.example.learner_kotlin.courseabout.course.exception.VideoTaskException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class APIControllerAdvice {
    // validation 예외처리
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException?): ResponseEntity<*> {
        val errMap: MutableMap<String, Any> = HashMap()
        errMap["error"] = "Type Mismatched."

        return ResponseEntity<Map<String, Any>>(errMap, HttpStatus.BAD_REQUEST)
    }

    // member 예외처리
    @ExceptionHandler(MemberTaskException::class)
    fun handleMemberException(e: MemberTaskException): ResponseEntity<*> {
        log.info("--- MemberTaskException")
        log.info("--- e.getMessage() : " + e.message)

        val errMap: Map<String, String?> = java.util.Map.of("error", e.message)

        return ResponseEntity.status(e.statusCode).body<Map<String, String?>>(errMap)
    }

    // Login 예외처리
    @ExceptionHandler(LoginTaskException::class)
    fun handleLoginException(e: LoginTaskException): ResponseEntity<*> {
        log.info("--- LoginTaskException")
        log.info("--- e.getMessage() : " + e.message)

        val errMap: Map<String, String?> = java.util.Map.of("error", e.message)

        return ResponseEntity.status(e.statusCode).body<Map<String, String?>>(errMap)
    }

    // JWT 예외처리
    @ExceptionHandler(JWTTaskException::class)
    fun handleJWTException(e: JWTTaskException): ResponseEntity<*> {
        log.info("--- JWTTaskException")
        log.info("--- e.getMessage() : " + e.message)

        val errMap: Map<String, String?> = java.util.Map.of("error", e.message)

        return ResponseEntity.status(e.statusCode).body<Map<String, String?>>(errMap)
    }

    // ReviewTaskException 처리
    @ExceptionHandler(ReviewTaskException::class)
    fun handleReviewException(e: ReviewTaskException): ResponseEntity<Map<String, String?>> {
        log.info("--- ReviewTaskException occurred")
        log.info("--- Exception message: ${e.message}")

        // 예외 메시지를 담은 에러 응답 생성
        val errMap = mapOf("error" to e.message)

        // 예외의 상태 코드와 메시지로 응답 반환
        return ResponseEntity.status(e.statusCode).body(errMap)
    }

    // CourseTaskException 처리
    @ExceptionHandler(CourseTaskException::class)
    fun handleCourseTaskException(e: CourseTaskException): ResponseEntity<Map<String, String?>> {
        log.info("--- CourseTaskException occurred")
        log.info("--- Exception message: ${e.message}")

        // 예외 메시지를 담은 에러 응답 생성
        val errMap = mapOf("error" to e.message)

        // 예외의 상태 코드와 메시지로 응답 반환
        return ResponseEntity.status(e.statusCode).body(errMap)
    }

    // OrderTaskException 처리 (추가된 부분)
    @ExceptionHandler(OrderTaskException::class)
    fun handleOrderTaskException(e: OrderTaskException): ResponseEntity<Map<String, String?>> {
        log.info("--- OrderTaskException occurred")
        log.info("--- Exception message: ${e.message}")

        // 예외 메시지를 담은 에러 응답 생성
        val errMap = mapOf("error" to e.message)

        // 예외의 상태 코드와 메시지로 응답 반환
        return ResponseEntity.status(e.statusCode).body(errMap)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body<String>(ex.message) // 예외 메시지를 그대로 전송
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body<String>(ex.message)
    }

    @ExceptionHandler(HeartNewsAlreadyExistsException::class)
    fun handleHeartNewsAlreadyExistsException(ex: HeartNewsAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }
    @ExceptionHandler(VideoTaskException::class)
    fun handleVideoTaskException(e: VideoTaskException): ResponseEntity<Map<String, String?>> {
        log.info("--- VideoTaskException occurred")
        log.info("--- Exception message: ${e.message}")

        // 예외 메시지를 담은 에러 응답 생성
        val errMap = mapOf("error" to e.message)

        // 예외의 상태 코드와 메시지로 응답 반환
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errMap)
    }




}

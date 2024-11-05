package edu.example.learner_kotlin.config

import edu.example.learner_kotlin.member.service.CustomOauth2UserService
import edu.example.learner_kotlin.security.JWTCheckFilter
import edu.example.learner_kotlin.security.JWTUtil
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val jwtUtil: JWTUtil,
    private val customSuccessHandler: CustomSuccessHandler,
    private val customOauth2UserService: CustomOauth2UserService
){

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        http: HttpSecurity,
        customClientRegistrationRepo: CustomClientRegistrationRepo
    ): SecurityFilterChain {
        //csrf disable
        http.csrf{it.disable()}
        //From 로그인 방식 disable
            .formLogin{it.disable()}
        //HTTP Basic 인증 방식 disable
            .httpBasic{it.disable()}

        //JWTFilter 추가
        http
            .addFilterBefore(JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)

        //oauth2
        http
            .oauth2Login { oauth2 ->
                oauth2
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOauth2UserService)
                    }
                    .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                    .successHandler(customSuccessHandler)
            }
            .exceptionHandling {
                it.authenticationEntryPoint { request, response, _ ->
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "SC_BAD_REQUEST") // 로그인 페이지로 리다이렉트하지 않음
                }
            }

        //경로별 인가 작업
        http.authorizeHttpRequests {
            it.requestMatchers(HttpMethod.POST,"notifications/**").permitAll()
            it.requestMatchers(HttpMethod.GET,"notifications/**").permitAll()
            it.requestMatchers(HttpMethod.GET,"notifications/connect").permitAll()
            it.requestMatchers(HttpMethod.GET,"alarm/connect").permitAll()
                it.requestMatchers(HttpMethod.POST,"/alarm/count").permitAll()
            it.requestMatchers(HttpMethod.POST,"alarm/notify").permitAll()
                // 리뷰 권한 설정
                it.requestMatchers(HttpMethod.GET, "/course/{courseId}/reviews/list")
                    .permitAll() // GET 요청 reviews 권한 설정
                it.requestMatchers(HttpMethod.GET, "/course/{courseId}/reviews/{reviewId}")
                    .permitAll() // GET 요청 course 모두 허용
                it.requestMatchers(HttpMethod.DELETE, "/course/{courseId}/reviews/{reviewId}")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // DELETE 요청 reviews 권한 설정
                it.requestMatchers(HttpMethod.PUT, "/course/{courseId}/reviews/{reviewId}")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // PUT 요청 reviews 권한 설정
                it.requestMatchers(HttpMethod.POST, "/course/{courseId}/reviews/create")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // POST 요청 reviews 권한 설정

                // 강의 문의 권한 설정
                it.requestMatchers(HttpMethod.GET, "/course-inquiry/**").permitAll() // GET 요청 course 모두 허용
                it.requestMatchers(HttpMethod.POST, "/course-inquiry/**")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // POST 요청 course 권한 설정
                it.requestMatchers(HttpMethod.POST, "/course/{courseId}/course-inquiry")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // POST 요청 course 권한 설정
                it.requestMatchers(HttpMethod.DELETE, "/course-inquiry/**")
                    .hasAnyRole("INSTRUCTOR", "ADMIN") // DELETE 요청 course 권한 설정

                // 주문 권한 설정
                it.requestMatchers("/order/**").hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // 주문 관련 모두 허용
                it.requestMatchers(HttpMethod.GET, "order/list/admin").hasRole("ADMIN") // 주문 목록 조회 권한 설정

                // 비디오 권한 설정
                it.requestMatchers(HttpMethod.POST, "video/**").hasAnyRole("INSTRUCTOR", "ADMIN") // POST video 권한 설정
                it.requestMatchers(HttpMethod.PUT, "video/**").hasAnyRole("INSTRUCTOR", "ADMIN") // PUT video 권한 설정
                it.requestMatchers(HttpMethod.DELETE, "video/**")
                    .hasAnyRole("INSTRUCTOR", "ADMIN") // DELETE video 권한 설정

                // 새소식 권한 설정
                it.requestMatchers(HttpMethod.GET, "/news/**").permitAll() // GET 요청 news 모두 허용
                it.requestMatchers(HttpMethod.PUT, "/news/**").hasAnyRole("INSTRUCTOR", "ADMIN") // PUT 요청 news 권한 설정
                it.requestMatchers(HttpMethod.POST, "/news/**").hasAnyRole("INSTRUCTOR", "ADMIN") // POST 요청 news 권한 설정
                it.requestMatchers(HttpMethod.DELETE, "/news/**")
                    .hasAnyRole("INSTRUCTOR", "ADMIN") // DELETE 요청 news 권한 설정

                // 좋아요
                it.requestMatchers(HttpMethod.GET, "/like/**").permitAll() // 좋아요 요청 모두 허용
                it.requestMatchers("/like/**").hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // 문의 권한 설정

                // 문의 권한 설정
                it.requestMatchers(HttpMethod.GET, "/inquiries/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/inquiries/**").hasAnyRole("USER", "INSTRUCTOR", "ADMIN")
                it.requestMatchers(HttpMethod.PUT).hasAnyRole("USER", "INSTRUCTOR", "ADMIN")
                it.requestMatchers(HttpMethod.DELETE).hasAnyRole("USER", "INSTRUCTOR", "ADMIN")

                // 문의 답변 권한 설정
                it.requestMatchers(HttpMethod.GET, "/answers/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/answers/**").hasAnyRole("ADMIN")
                it.requestMatchers(HttpMethod.PUT, "/answers/**").hasAnyRole("ADMIN")
                it.requestMatchers(HttpMethod.DELETE, "/answers/**").hasAnyRole("ADMIN")

                // 스터디 테이블 권한 설정
                it.requestMatchers(HttpMethod.GET, "/study-tables/{memberId}/weekly-summary")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN")
                it.requestMatchers(HttpMethod.GET, "/study-tables/{memberId}/yearly-summary")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN")

                // 회원 권한 설정
                it.requestMatchers("/members/{id}/other").permitAll()
                it.requestMatchers("/members/other/{nickname}").permitAll() // 다른 회원 프로필 보기
                it.requestMatchers(HttpMethod.GET, "/members/instructor/*").permitAll()
                it.requestMatchers(HttpMethod.GET, "/members/instructor/{nickname}/reviews/list").permitAll()
                it.requestMatchers("/members/nickname").permitAll() // 강사 프로필 보기
                it.requestMatchers("/members/{memberId}")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // 로그인된 사용자만 회원정보 수정 가능
                it.requestMatchers(HttpMethod.GET, "/members/list").hasRole("ADMIN") // 회원 목록 조회 권한 설정
                it.requestMatchers(HttpMethod.GET, "/members/instructor/**").permitAll() // 강사 관련 프로필 GET 요청 허용
                it.requestMatchers(HttpMethod.GET, "/members/{memberId}/courses")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN")

                // 강의 권한 설정
                it.requestMatchers(HttpMethod.GET, "/course/**").permitAll() // GET 요청 course 모두 허용
                it.requestMatchers(HttpMethod.GET, "/course/list").permitAll() // GET 요청 course 모두 허용
                it.requestMatchers(HttpMethod.POST, "/course").hasAnyRole("INSTRUCTOR", "ADMIN") // POST 요청 course 권한 설정
                it.requestMatchers(HttpMethod.DELETE, "/course/**")
                    .hasAnyRole("INSTRUCTOR", "ADMIN") // DELETE 요청 course 권한 설정
                it.requestMatchers(HttpMethod.PUT, "/course/**")
                    .hasAnyRole("INSTRUCTOR", "ADMIN") // PUT 요청 course 권한 설정
                it.requestMatchers(HttpMethod.GET, "/course/{id}/list")
                    .hasAnyRole("USER", "INSTRUCTOR", "ADMIN") // 본인 수강 강의 조회

                // 정적 리소스 허용
                it.requestMatchers("/images/**").permitAll() // images 폴더에 있는 리소스 허용
                it.requestMatchers("/css/**").permitAll() // css 폴더에 있는 리소스 허용
                it.requestMatchers("/js/**").permitAll() // js 폴더에 있는 리소스 허용

                // Open API 허용
                it.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger 관련 리소스 모두 허용

                // 로그인 권한 설정
                it.requestMatchers("/login").permitAll()
                it.requestMatchers("/join/login").permitAll() // 로그인 및 회원가입 모두 허용
                it.requestMatchers("/members/find/**").permitAll() // 비밀번호 찾기 및 아이디 찾기 모두 허용

                it.anyRequest().authenticated()
        }

        //세션 설정 : STATELESS
        http.sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        // CORS 설정
        http.cors { cors ->
            cors.configurationSource(CorsConfigurationSource {
                CorsConfiguration().apply {
                    // 허용된 origin 설정
                    allowedOrigins = (listOf("http://localhost:3000", "http://localhost:8080"))
                    // 모든 HTTP 메소드 허용
                    allowedMethods = (listOf("*"))
                    // 인증 관련 쿠키 전송을 허용
                    allowCredentials = true
                    // 모든 헤더 허용
                    allowedHeaders = (listOf("*"))
                    // CORS 캐시 시간 설정 (1시간)
                    maxAge = (3600L)

                    // 노출될 헤더 설정 (여러 개 추가하려면 add로 해야 함)
                    addExposedHeader("Authorization")
                    addExposedHeader("Set-Cookie")
                }
            })
        }


        return http.build()
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { request, response, accessDeniedException ->
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.writer.write("Access Denied: ${accessDeniedException.message}")
            response.writer.flush()
        }
    }
}
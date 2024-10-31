package edu.example.learner_kotlin

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.order.entity.Order
import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import edu.example.learner_kotlin.courseabout.order.entity.OrderStatus
import edu.example.learner_kotlin.courseabout.order.repository.OrderItemRepository
import edu.example.learner_kotlin.courseabout.order.repository.OrderRepository
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.Role
import edu.example.learner_kotlin.member.repository.MemberRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import java.time.LocalDateTime
import kotlin.random.Random

@TestConfiguration
@Profile("test")
class TestDataConfig {

    @Bean
    fun testDataInitializer(
        memberRepository: MemberRepository,
        courseRepository: CourseRepository,
        orderRepository: OrderRepository,
        orderItemRepository: OrderItemRepository
    ) = TestDataInitializer(memberRepository, courseRepository, orderRepository, orderItemRepository)
}

class TestDataInitializer(
    private val memberRepository: MemberRepository,
    private val courseRepository: CourseRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
) {

    fun initialize() {
        val random = Random(System.currentTimeMillis())

        // Create Members
        val members = (1..10).map { i ->
            Member(
                email = "user$i@example.com",
                password = "password$i",
                nickname = "User$i",
                phoneNumber = "0101234${5670 + i}",
                profileImage = ByteArray(0),
                imageType = "image/jpeg",
                role = if (i == 1) Role.ADMIN else Role.USER,
                introduction = "I am user number $i",
                createDate = LocalDateTime.now().minusDays(random.nextLong(1, 100))
            )
        }.map { memberRepository.save(it) }

        // Create Courses
        val courses = (1..20).map { i ->
            Course(
                courseName = "Course $i",
                courseDescription = "Description for Course $i",
                courseAttribute = CourseAttribute.values()[random.nextInt(CourseAttribute.values().size)],
                coursePrice = random.nextLong(10000, 100000),
                courseLevel = random.nextInt(1, 6),
                sale = random.nextBoolean(),
                courseCreatedDate = LocalDateTime.now().minusDays(random.nextLong(1, 100))
            )
        }.map { courseRepository.save(it) }

        // Create Orders and OrderItems
        members.forEach { member ->
            val orderCount = random.nextInt(0, 5)
            repeat(orderCount) {
                val order = Order(
                    member = member,
                    orderStatus = OrderStatus.values()[random.nextInt(OrderStatus.values().size)],
                    createdAt = LocalDateTime.now().minusDays(random.nextLong(1, 30))
                )
                val savedOrder = orderRepository.save(order)

                val orderItemCount = random.nextInt(1, 4)
                val selectedCourses = courses.shuffled().take(orderItemCount)
                selectedCourses.forEach { course ->
                    val orderItem = OrderItem(
                        course = course,
                        order = savedOrder,
                        courseAttribute = course.courseAttribute,
                        price = course.coursePrice,
                        createdAt = savedOrder.createdAt
                    )
                    orderItemRepository.save(orderItem)
                    savedOrder.orderItems.add(orderItem)
                }
                savedOrder.totalPrice = savedOrder.orderItems.sumOf { it.price ?: 0 }
                orderRepository.save(savedOrder)
            }
        }

        println("Test data initialization completed.")
        println("Created ${memberRepository.count()} members")
        println("Created ${courseRepository.count()} courses")
        println("Created ${orderRepository.count()} orders")
        println("Created ${orderItemRepository.count()} order items")
    }
}
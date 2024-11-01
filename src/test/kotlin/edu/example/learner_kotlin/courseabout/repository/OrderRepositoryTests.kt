package edu.example.learner_kotlin.courseabout.repository


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
import edu.leranermig.order.exception.OrderException
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
@ActiveProfiles("test")
@Transactional
class OrderRepositoryTests {
    @Autowired private lateinit var orderRepository: OrderRepository
    @Autowired private lateinit var courseRepository: CourseRepository
    @Autowired private lateinit var orderItemRepository: OrderItemRepository
    @Autowired private lateinit var modelMapper: ModelMapper
    @Autowired private lateinit var memberRepository: MemberRepository

    lateinit var testMember: Member
    lateinit var testCourse: Course
    lateinit var testOrder: Order

    @BeforeEach
    fun setupTestData() {
        // 기존 데이터 삭제
        orderItemRepository.deleteAll()
        orderRepository.deleteAll()
        courseRepository.deleteAll()
        memberRepository.deleteAll()

        // Member 생성
        testMember = Member(
            email = "test@example.com",
            password = "password123",
            nickname = "TestUser",
            phoneNumber = "01012345678",
            profileImage = ByteArray(0),
            imageType = "image/jpeg",
            role = Role.USER,
            introduction = "Test user for order system"
        )
        testMember = memberRepository.save(testMember)

        // Course 생성
        testCourse = Course(
            courseName = "Test Course",
            courseDescription = "This is a test course",
            courseAttribute = CourseAttribute.C,
            coursePrice = 50000L,
            courseLevel = 3,
            sale = true
        )
        testCourse = courseRepository.save(testCourse)

        // Order 생성
        testOrder = Order(
            member = testMember,
            orderStatus = OrderStatus.PROCESSING,
            totalPrice = 50000L
        )
        testOrder = orderRepository.save(testOrder)

        // OrderItem 생성
        val orderItem = OrderItem(
            course = testCourse,
            order = testOrder,
            courseAttribute = testCourse.courseAttribute,
            price = testCourse.coursePrice
        )
        orderItemRepository.save(orderItem)

        // Order에 OrderItem 추가
        testOrder.orderItems.add(orderItem)
        orderRepository.save(testOrder)

        // 생성된 데이터 확인
        println("Test Member ID: ${testMember.memberId}")
        println("Test Course ID: ${testCourse.courseId}")
        println("Test Order ID: ${testOrder.orderId}")
    }

    @Test
    fun add() {

        val newOrder = Order(
            member = testMember,
            orderStatus = OrderStatus.CANCELED
        )
        val savedOrder = orderRepository.save(newOrder)

        val newOrderItem = OrderItem(
            order = savedOrder,
            course = testCourse,
            price = 10000L,
            courseAttribute = testCourse.courseAttribute
        )
        orderItemRepository.save(newOrderItem)
        savedOrder.orderItems.add(newOrderItem)

        val updatedOrder = orderRepository.save(savedOrder)

        assertEquals(1, updatedOrder.orderItems.size)
        assertEquals(OrderStatus.CANCELED, updatedOrder.orderStatus)
    }

    @Test
    fun read() {
        val order = orderRepository.findById(testOrder.orderId!!).orElseThrow { OrderException.ORDER_NOT_FOUND.get() }!!

        assertEquals(1, order.orderItems.size)
        assertEquals(OrderStatus.PROCESSING, order.orderStatus)
    }

    @Test
    fun update() {
        val order = orderRepository.findById(testOrder.orderId!!).orElseThrow()!!
        order.orderStatus = OrderStatus.FAILED
        order.orderItems.clear()

        val newOrderItem = OrderItem(
            order = order,
            course = testCourse,
            price = testCourse.coursePrice,
            courseAttribute = testCourse.courseAttribute
        )
        order.orderItems.add(newOrderItem)

        val updatedOrder = orderRepository.save(order)

        assertEquals(OrderStatus.FAILED, updatedOrder.orderStatus)
        assertEquals(1, updatedOrder.orderItems.size)
    }

    @Test
    fun delete() {
        orderRepository.deleteById(testOrder.orderId!!)
        val deletedOrder = orderRepository.findById(testOrder.orderId!!)
        assert(deletedOrder.isEmpty)
    }
}
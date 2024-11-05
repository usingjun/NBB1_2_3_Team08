package edu.example.learner_kotlin.courseabout.order.service

import edu.example.learner_kotlin.alarm.service.AlarmService
import edu.example.learner_kotlin.alarm.service.SseService
import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.MemberCourse
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.course.repository.MemberCourseRepository
import edu.example.learner_kotlin.courseabout.exception.CourseException
import edu.example.learner_kotlin.courseabout.order.dto.OrderCreateDTO
import edu.example.learner_kotlin.courseabout.order.dto.OrderDTO
import edu.leranermig.order.dto.OrderItemDTO
import edu.example.learner_kotlin.courseabout.order.entity.Order
import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import edu.example.learner_kotlin.courseabout.order.entity.OrderStatus
import edu.example.learner_kotlin.courseabout.order.exception.OrderException
import edu.example.learner_kotlin.courseabout.order.exception.OrderTaskException
import edu.example.learner_kotlin.courseabout.order.repository.OrderItemRepository
import edu.example.learner_kotlin.courseabout.order.repository.OrderRepository
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.Exception
import kotlin.Long
import kotlin.RuntimeException


@Service
@Transactional
class OrderServiceImpl(
    val orderRepository: OrderRepository,
    val courseRepository: CourseRepository,
    val orderItemRepository: OrderItemRepository,
    val memberRepository: MemberRepository,
    val memberCourseRepository: MemberCourseRepository,
    val alarmService: AlarmService,
    val sseService: SseService
) : OrderService {
    
    // 주문 총 가격계산과 구매 정보저장 OCP 위반
    override fun purchaseOrderItems(orderId: Long, memberId: Long): OrderDTO {
        log.info("구매 로직 시작")
        // 주문 찾기
        val order = orderRepository.findById(orderId)
            .orElseThrow<RuntimeException>(OrderException.ORDER_NOT_FOUND::get)
        val findmember = memberRepository.findById(memberId).orElseThrow()
        log.info("결제할 장바구니{$order}")
        // 주문자 확인
        if (order!!.member!!.memberId != memberId){
            log.info("에러발생")
            throw OrderException.ORDER_NOT_FOUND.get()
        }

        // 주문의 아이템 리스트를 통해 총 가격 계산
        var totalPrice = 0L

        for (orderItem in order.orderItems) {
            totalPrice += orderItem.price!!
        }
        log.info("계산 완료")
        // 주문 상태 업데이트 (예: 결제 완료 상태로 변경)
        order.orderStatus = OrderStatus.COMPLETED
        order.totalPrice=totalPrice // 총 가격 설정

        // 구매 정보 저장 (멤버-강의 연결)
        for (orderItem in order.orderItems) {
            val memberCourse = MemberCourse().apply {
                member = findmember
                course = courseRepository.findById(orderItem.course!!.courseId!!).get()
            }

            memberCourseRepository.save(memberCourse)
            log.info("저장 전 확인 {$memberCourse}")
        }

        // 주문 정보 저장
        orderRepository.save<Order>(order)
        log.info("저장완료")

        // 주문 DTO 반환
        return OrderDTO(order)
    }


    override fun add(createDTO: OrderCreateDTO, memberId: Long): OrderCreateDTO {
        log.info("add test {$createDTO}")
        createDTO.memberId = memberId

        // 1. Order 엔티티를 먼저 저장합니다.
        var orderEntity = OrderMapper.toEntity(createDTO)
        orderEntity = orderRepository.save(orderEntity) // Order 저장

        // 2. OrderItem 엔티티를 생성합니다.
        val orderItems = createDTO.orderItemDTOList!!.map { orderItemDTO ->
            val course = courseRepository.findById(orderItemDTO.courseId!!).get()
            OrderItemMapper.toEntity(orderItemDTO, orderEntity, course) // 저장된 Order를 전달
        }

        // 3. OrderItem을 저장합니다.
        orderItems.forEach { orderItemRepository.save(it) }

        // 4. 총금액을 계산합니다.
        orderEntity.totalPrice=OrderMapper.totalPriceCir(orderItems)
        val alarmContent = "주문이 생성되었습니다. {${orderEntity.orderId}}"
        alarmService.createAlarm(memberId,alarmContent, "주문 알림")
        return createDTO
    }

    override fun read(orderId: Long): OrderDTO {
        log.info("read Order By orderId {}", orderId)
        try {
            val order: Order =
                orderRepository.findById(orderId).orElseThrow<RuntimeException>(OrderException.ORDER_NOT_FOUND::get)!!

            val orderItemDTOS =order.orderItems.map {
                val course = courseRepository.findByIdOrNull(it.course?.courseId!!)?:throw OrderException.ORDER_NOT_FOUND.get()
                OrderItemMapper.toDTO(it, course)
            }.toMutableList()

            val orderDTO =OrderDTO(order)
            orderDTO.orderItemDTOList=orderItemDTOS
            log.info("read order {}", orderDTO)
            return orderDTO
        } catch (e: Exception) {
            log.error(e)
            throw OrderException.ORDER_NOT_FOUND.get()
        }
    }

    @Transactional
    override fun update(orderDTO: OrderDTO, orderId: Long): OrderDTO {
        val foundOrder: Order = orderRepository.findByIdOrNull(orderId)?: throw OrderException.ORDER_NOT_FOUND.get()

        val existingItems: MutableList<OrderItem> = foundOrder.orderItems
        // 새로운 아이템 추가 및 업데이트
        orderDTO.orderItemDTOList?.forEach { dto ->
            val existingItemOpt = existingItems.find { it.course?.courseId == dto.courseId }

            if (existingItemOpt != null) {
                // 아이템이 이미 존재하는 경우, 업데이트
                orderItemRepository.save(existingItemOpt)
            } else {
                // 새 아이템 추가
                addNewItem(dto, orderId, foundOrder)
            }
        }
        // 삭제할 아이템 찾기
        val updatedCourseIds = orderDTO.orderItemDTOList?.map(OrderItemDTO::courseId) ?: emptyList()
        existingItems.removeIf { it.course?.courseId !in updatedCourseIds }

        // 총 금액 계산
        foundOrder.totalPrice = calculateTotalPrice(foundOrder.orderItems)

        return OrderDTO(foundOrder)
    }

    private fun addNewItem(dto: OrderItemDTO, orderId: Long, foundOrder: Order) {
        val findCourse = courseRepository.findById(dto.courseId!!)
            .orElseThrow { CourseException.COURSE_NOT_FOUND.courseException }

        dto.courseAttribute = findCourse.courseAttribute.toString()
        dto.orderId = orderId

        val newItem = OrderItem().apply {
            order = foundOrder // 이미 저장된 Order를 사용
            course = Course(findCourse.courseId)
            courseAttribute = findCourse.courseAttribute
            price = findCourse.coursePrice
        }

        orderItemRepository.save(newItem) // 새로운 아이템 저장
        foundOrder.orderItems.add(newItem)
    }

    private fun calculateTotalPrice(orderItems: List<OrderItem>): Long {
        return orderItems.sumOf { it.price ?: 0L } // 각 아이템의 가격을 가져와서 총합 계산
    }

    override fun delete(orderId: Long) {
        try {
            if (!orderRepository.existsById(orderId)) {
                throw OrderTaskException("주문이 존재하지 않습니다.", 404)
            }
            orderRepository.deleteById(orderId)
        } catch (e: Exception) {
            log.error(e)
            throw OrderException.NOT_DELETED.get()
        }
    }

    //    @Override
    //    public List<OrderDTO> readAll()x {
    //        List<Order> orders = orderRepository.findAll();
    //        List<OrderDTO> orderDTOList = new ArrayList<>();
    //        for (int i = 0; i <orders.size(); i++) {
    //            orderDTOList.add(new OrderDTO(orders.get(i)));
    //            for (OrderItem orderItem : orders.get(i).getOrderItems()) {
    //                orderDTOList.get(i).getOrderItemDTOList().add(new OrderItemDTO(orderItem));
    //            }
    //        }
    //        return orderDTOList;
    //    }

    override fun readAll(): List<OrderDTO> {
        val orders = orderRepository.findAll()
        return OrderMapper.entityListToDTO(orders)
    }

    override fun getOrdersById(memberId: Long): List<OrderDTO> {
        val member: Member = memberRepository.getReferenceById(memberId)
        val orders: MutableList<Order?> = orderRepository.findByMember(member).toMutableList()
        return OrderMapper.entityListToDTO(orders)
    }

    override fun deleteAll() {
        orderRepository.deleteAll()
    }
}

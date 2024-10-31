package edu.example.learner_kotlin.courseabout.order.service

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.MemberCourse
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.course.repository.MemberCourseRepository
import edu.example.learner_kotlin.courseabout.exception.CourseException

import edu.example.learner_kotlin.courseabout.order.dto.OrderDTO
import edu.leranermig.order.dto.OrderItemDTO
import edu.example.learner_kotlin.courseabout.order.entity.Order
import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import edu.example.learner_kotlin.courseabout.order.entity.OrderStatus
import edu.leranermig.order.exception.OrderException
import edu.leranermig.order.exception.OrderTaskException
import edu.example.learner_kotlin.courseabout.order.repository.OrderItemRepository
import edu.example.learner_kotlin.courseabout.order.repository.OrderRepository
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.repository.MemberRepository
import jakarta.transaction.Transactional

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.lang.String
import java.util.*
import kotlin.Exception
import kotlin.Long
import kotlin.RuntimeException
import kotlin.collections.ArrayList
import kotlin.math.log


@Service
@Transactional
class OrderServiceImpl(
    val orderRepository: OrderRepository,
    val courseRepository: CourseRepository,
    val orderItemRepository: OrderItemRepository,
    val modelMapper: ModelMapper,
    val memberRepository: MemberRepository,
    val memberCourseRepository: MemberCourseRepository,
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


    override fun add(orderDTO: OrderDTO, memberId: Long): OrderDTO {
        log.info("add test {$orderDTO}")
        orderDTO.memberId=memberId

        var orderEntity= orderDTO.toEntity(orderDTO)
        orderEntity.orderStatus=OrderStatus.PENDING
        orderEntity = orderRepository.save(orderEntity) // Order 저장
        var totalPrice = 0L

        // orderDTO안에 orderItemDTOList를 orderItemList로 변환
        // DTO안에 있는 강의 번호를 조회해 orderItemlist

        for (orderItemDTO in orderDTO.orderItemDTOList!!) {
            log.info("orderItem {$orderItemDTO} ")
            //OrderItem 의 강의 찾기
            var findCourse = courseRepository.findById(orderItemDTO.courseId!!).orElseThrow()
            log.info("course {$findCourse} ")
            val orderItem = OrderItem().apply {
                order = orderEntity
                course = Course(courseId = findCourse.courseId)
                price =findCourse.coursePrice
                courseAttribute = findCourse.courseAttribute
                totalPrice+= findCourse.coursePrice!!
            }
            orderItemRepository.save(orderItem)
        }
        log.info("orderItem save success")
        orderEntity.totalPrice=totalPrice

        orderEntity = orderRepository.save(orderEntity)
        orderDTO.totalPrice=totalPrice
        return orderDTO
    }

    override fun read(orderId: Long): OrderDTO {
        log.info("read Order By orderId {}", orderId)
        try {
            val order: Order =
                orderRepository.findById(orderId).orElseThrow<RuntimeException>(OrderException.ORDER_NOT_FOUND::get)!!

            val orderItemDTOS: MutableList<OrderItemDTO> = ArrayList<OrderItemDTO>()
            for (orderItem in order.orderItems) {
                val orderItemDTO: OrderItemDTO = OrderItemDTO()
                orderItemDTO.price=orderItem.course!!.coursePrice
                orderItemDTOS.add(OrderItemDTO(orderItem))
            }

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
        try {
            //given
            val foundOrder: Order = orderRepository.findById(orderId)
                .orElseThrow<RuntimeException>(OrderException.ORDER_NOT_FOUND::get)!!


            // 기존 주문 아이템
            val existingItems: MutableList<OrderItem> = foundOrder.orderItems
            // when
            // 새 아이템을 추가 및 업데이트
            for (dto in orderDTO.orderItemDTOList!!) {
                // 기존 아이템 중에서 해당 아이템을 찾습니다.
                val existingItemOpt: Optional<OrderItem> = existingItems.stream()
                    .filter { item: OrderItem ->
                        item.course!!.courseId!! == dto.courseId
                    }
                    .findFirst()

                if (existingItemOpt.isPresent) {
                    // 아이템이 이미 존재하는 경우, 업데이트
                    val existingItem: OrderItem = existingItemOpt.get()
                    // 가격 업데이트 (필요시 추가 로직)
                    orderItemRepository.save(existingItem) // 변경사항 저장
                } else {
                    // 새 아이템 추가
                    val findCourse = courseRepository.findById(dto.courseId!!).orElseThrow(CourseException.COURSE_NOT_FOUND::courseException)
                    dto.courseAttribute=String.valueOf(findCourse.courseAttribute)
                    dto.orderId=orderId
                    val newItem: OrderItem = OrderItem().apply {
                        order =Order(orderId)
                        course =Course(findCourse.courseId)
                        courseAttribute = findCourse.courseAttribute
                        price = findCourse.coursePrice
                    }
                    orderItemRepository.save(newItem) // 새로운 아이템 저장
                    foundOrder.orderItems.add(newItem)
                }
            }

            // 삭제할 아이템 찾기
            val updatedCourseIds: MutableList<Long?>? = orderDTO.orderItemDTOList!!.stream()
                .map(OrderItemDTO::courseId)
                .toList()

            // 기존 아이템 중 삭제할 아이템 제거
            existingItems.removeIf { existingItem: OrderItem ->
                !updatedCourseIds!!.contains(
                    existingItem.course!!.courseId
                )
            }

            // 총 금액 계산
            val totalPrice: Long = foundOrder.orderItems.stream()
                .mapToLong { it.price!! } // 각 아이템의 가격을 가져와서
                .sum() // 총합 계산
            foundOrder.totalPrice=totalPrice // 총 금액을 주문에 설정
            return OrderDTO(foundOrder)
        } catch (e: Exception) {
            log.error("수정 오류," + e.message)
            throw OrderException.NOT_MODIFIED.get()
        }
    }


    override fun delete(orderId: Long) {
        try {
            if (!orderRepository!!.existsById(orderId)) {
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
        val orders: MutableList<Order?> = orderRepository.findAll()
        val orderDTOList: MutableList<OrderDTO> = ArrayList<OrderDTO>()

        for (order in orders) {
            val orderDTO: OrderDTO = OrderDTO().apply {

            }
            orderDTO.orderItemDTOList=ArrayList()

            for (orderItem in order!!.orderItems) {
                (orderDTO.orderItemDTOList as ArrayList<OrderItemDTO>).add(OrderItemDTO(orderItem))
            }
            orderDTOList.add(orderDTO)
        }

        return orderDTOList
    }

    override fun getOrdersById(memberId: Long): List<OrderDTO> {
        val member: Member = memberRepository.getReferenceById(memberId)
        val orders: List<Order> = orderRepository.findByMember(member)
        val orderDTOListByMember: MutableList<OrderDTO> = ArrayList()

        for (order in orders) {
            val orderDTO = OrderDTO(order)
            orderDTO.orderItemDTOList=ArrayList()

            for (orderItem in order.orderItems) {
                orderDTO.orderItemDTOList!!.add(OrderItemDTO(orderItem))
            }
            orderDTOListByMember.add(orderDTO)
        }
        return orderDTOListByMember
    }


    override fun deleteAll() {
        orderRepository.deleteAll()
    }
}

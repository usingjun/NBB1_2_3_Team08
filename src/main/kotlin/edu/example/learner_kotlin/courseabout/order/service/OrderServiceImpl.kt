package edu.example.learner_kotlin.courseabout.order.service

import edu.example.learner_kotlin.courseabout.course.entity.MemberCourse
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.course.repository.MemberCourseRepository

import edu.leranermig.order.dto.OrderDTO
import edu.leranermig.order.dto.OrderItemDTO
import edu.leranermig.order.dto.OrderUpdateDTO
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
import java.util.function.Predicate
import kotlin.Exception
import kotlin.Long
import kotlin.RuntimeException
import kotlin.collections.ArrayList


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
        // 주문 찾기
        val order = orderRepository.findById(orderId)
            .orElseThrow<RuntimeException>(OrderException.ORDER_NOT_FOUND::get)

        // 주문자 확인
        if (order!!.member!!.memberId==memberId){
            throw OrderException.ORDER_NOT_FOUND.get()
        }

        // 주문의 아이템 리스트를 통해 총 가격 계산
        var totalPrice = 0L

        for (orderItem in order.orderItems) {
            totalPrice += orderItem.price!!
        }

        // 주문 상태 업데이트 (예: 결제 완료 상태로 변경)
        order.orderStatus = OrderStatus.COMPLETED
        order.totalPrice=totalPrice // 총 가격 설정

        // 구매 정보 저장 (멤버-강의 연결)
        for (orderItem in order.orderItems) {
            val memberCourse = MemberCourse().apply {
                member!!.memberId = memberId
                course.apply {
                    orderItem.course
                }
                purchaseDate = Date()
            }

            memberCourseRepository.save(memberCourse)
        }

        // 주문 정보 저장
        orderRepository.save<Order>(order)
        log.info("저장완료")

        // 주문 DTO 반환
        return modelMapper.map(order, OrderDTO::class.java)
    }


    override fun add(orderDTO: OrderDTO, memberId: Long): OrderDTO {
        orderDTO.memberId=memberId
        var order= modelMapper.map(orderDTO, Order::class.java)
        order = orderRepository.save(order) // Order 저장
        var totalPrice = 0L

        // orderDTO안에 orderItemDTOList를 orderItemList로 변환
        // DTO안에 있는 강의 번호를 조회해 orderItemlist

        for (orderItemDTO in orderDTO.orderItemDTOList!!) {
            log.info("orderItem {$orderItemDTO} ")
            val course = courseRepository.findById(orderItemDTO.courseId!!).orElseThrow()
            orderItemDTO.apply {
                price=course.coursePrice
                courseAttribute= course.courseAttribute.toString()
                val orderItem: OrderItem = modelMapper.map(orderItemDTO, OrderItem::class.java)
                order.orderItems.add(orderItem)
                totalPrice+=orderItem.price!!
            }
        }
        order.totalPrice=totalPrice
        order = orderRepository.save(order)
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

            val orderDTO: OrderDTO = modelMapper.map(order,OrderDTO::class.java)
            orderDTO.orderItemDTOList=orderItemDTOS
            log.info("read order {}", orderDTO)
            return orderDTO
        } catch (e: Exception) {
            log.error(e)
            throw OrderException.ORDER_NOT_FOUND.get()
        }
    }

    @Transactional
    override fun update(orderUpdateDTO: OrderUpdateDTO, orderId: Long): OrderUpdateDTO {
        try {
            //given
            val foundOrder: Order = orderRepository.findById(orderId)
                .orElseThrow<RuntimeException>(OrderException.ORDER_NOT_FOUND::get)!!

            // 주문 상태 업데이트
            foundOrder.orderStatus= OrderStatus.valueOf(orderUpdateDTO.orderStatus!!)

            // 기존 주문 아이템
            val existingItems: MutableList<OrderItem> = foundOrder.orderItems
            // when
            // 새 아이템을 추가 및 업데이트
            for (dto in orderUpdateDTO.orderItemDTOList) {
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
                    val course = courseRepository.findById(dto.courseId!!)
                    dto.courseAttribute=String.valueOf(course.get().courseAttribute)
                    dto.orderId=orderId
                    val newItem: OrderItem = modelMapper.map(dto, OrderItem::class.java)
                    orderItemRepository.save(newItem) // 새로운 아이템 저장
                    foundOrder.orderItems.add(newItem)
                }
            }

            // 삭제할 아이템 찾기
            val updatedCourseIds: MutableList<Long?>? = orderUpdateDTO.orderItemDTOList.stream()
                .map(OrderItemDTO::courseId)
                .toList()

            // 기존 아이템 중 삭제할 아이템 제거
            existingItems.removeIf(
                Predicate<OrderItem> { existingItem: OrderItem ->
                    !updatedCourseIds!!.contains(
                        existingItem.course!!.courseId
                    )
                }
            )

            // 총 금액 계산
            val totalPrice: Long = foundOrder.orderItems.stream()
                .mapToLong { it.price!! } // 각 아이템의 가격을 가져와서
                .sum() // 총합 계산

            foundOrder.totalPrice=totalPrice // 총 금액을 주문에 설정

            return modelMapper.map(foundOrder, OrderUpdateDTO::class.java)
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
    //    public List<OrderDTO> readAll() {
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

//    override fun getOrdersById(memberId: Long): List<OrderDTO> {
//        val member: Member = memberRepository.getReferenceById(memberId)
//        val orders: List<Order> = orderRepository.findByMember(member)
//        val orderDTOListByMember: MutableList<OrderDTO> = ArrayList()
//
//        for (order in orders) {
//            val orderDTO: OrderDTO = OrderDTO(order)
//            orderDTO.setOrderItemDTOList(ArrayList<E>()) // 리스트 초기화
//
//            for (orderItem in order.getOrderItems()) {
//                orderDTO.getOrderItemDTOList().add(OrderItemDTO(orderItem))
//            }
//            orderDTOListByMember.add(orderDTO)
//        }
//        return orderDTOListByMember
//    }


    override fun deleteAll() {
        orderRepository.deleteAll()
    }
}

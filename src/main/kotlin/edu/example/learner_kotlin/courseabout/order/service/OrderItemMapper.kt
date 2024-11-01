package edu.example.learner_kotlin.courseabout.order.service

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.order.dto.OrderDTO
import edu.example.learner_kotlin.courseabout.order.entity.Order
import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import edu.example.learner_kotlin.log
import edu.leranermig.order.dto.OrderItemDTO
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull

object OrderItemMapper {
    fun toEntity(dto: OrderItemDTO, order: Order, course: Course): OrderItem {
        return OrderItem().apply {
            this.order = order // 이미 저장된 Order를 사용
            this.course = course
            price = course.coursePrice
            courseAttribute = course.courseAttribute
        }
    }

//    fun listToEntity(dto: MutableList<OrderItemDTO>, order: Order): List<OrderItem> {
//        return dto.map { toEntity(it, order) }
//    }

//    fun entityListToDTO(entityList: MutableList<OrderItem>): List<OrderItemDTO> {
//        return entityList.map { toDTO(it) }
//    }
    fun toDTO(orderItem: OrderItem, course: Course): OrderItemDTO {
        return OrderItemDTO().apply {
            orderId = orderItem.order!!.orderId
            courseId = course.courseId
            courseName= course.courseName
            price = course.coursePrice
        }
    }
}
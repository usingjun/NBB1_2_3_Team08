package edu.example.learner_kotlin.courseabout.order.repository

import edu.example.learner_kotlin.courseabout.order.entity.Order
import edu.example.learner_kotlin.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository


interface OrderRepository : JpaRepository<Order?, Long?>{
    fun findByMember(member: Member): List<Order>
}

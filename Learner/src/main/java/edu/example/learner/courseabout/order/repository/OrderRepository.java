package edu.example.learner.courseabout.order.repository;

import edu.example.learner.courseabout.order.entity.Order;
import edu.example.learner.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMember(Member member);

}

package edu.example.learner.courseabout.repository;

import edu.example.learner.courseabout.course.dto.CourseDTO;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.CourseAttribute;
import edu.example.learner.courseabout.course.repository.CourseRepository;
import edu.example.learner.courseabout.order.dto.OrderDTO;
import edu.example.learner.courseabout.order.dto.OrderItemDTO;

import edu.example.learner.courseabout.exception.CourseException;
import edu.example.learner.courseabout.order.exception.OrderException;

import edu.example.learner.courseabout.order.entity.Order;
import edu.example.learner.courseabout.order.entity.OrderItem;
import edu.example.learner.courseabout.order.entity.OrderStatus;
import edu.example.learner.courseabout.order.repository.OrderItemRepository;
import edu.example.learner.courseabout.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Log4j2
@RequiredArgsConstructor
@Commit
@Rollback(false)
public class OrderRepositoryTests {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;



    @BeforeEach
    @Transactional
    public void registerOrderData(){
        //given
        // Course 테스트 데이터
        Course course = new Course();
        course.changeCourseName("새 강의");
        course.changeCourseDescription("새 강의 설명");
        course.changePrice(10000L);
        course.changeCourseLevel(2);
        course.changeCourseStatus(CourseAttribute.C);
        course.changeSale(true);

        Course saveCourse = courseRepository.save(course);

        // OrderItem 데이터 등록을 위해 먼저 Order 설정
        Order order = new Order();
        order.changeOrderStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);


        //OrderItem 등록
        OrderItem orderItem = OrderItem.builder().order(order)
                .course(course)
                .price(course.getCoursePrice())
                .courseAttribute(course.getCourseAttribute())
                .build();
        order.getOrderItems().add(orderItem);

        orderRepository.save(order);

    }

    @Test
    @Transactional
    @Commit
    public void add() {

        OrderDTO orderDTO = OrderDTO.builder().orderStatus(String.valueOf(OrderStatus.CANCELED))
                .build();
        Order order = orderRepository.save(orderDTO.toEntity(orderDTO));
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()

                .courseId(1L)
                .orderId(order.getOrderId())
                .price(10000L)
                .courseAttribute(String.valueOf(CourseAttribute.C))
                .build();

        orderItemDTOS.add(orderItemDTO);
        orderItemDTOS.add(orderItemDTO);


        for (OrderItemDTO dto : orderItemDTOS) {
            Course course = courseRepository.save(courseRepository.findById(dto.getCourseId()).orElseThrow());
            log.info("course -----" + course);

            OrderItem orderItem = orderItemRepository.save(dto.toEntity(dto,order));

            order.getOrderItems().add(orderItem);
        }



        System.out.println("Added Order with courses size: " + order.getOrderItems().size());
        System.out.println("Read Order with courses size: " + order.getOrderItems().get(0));
    }

    @Test
    @Transactional
    public void read() {

        //given
        Long orderId = 1L;
        Order order = orderRepository.findById(orderId).orElseThrow(OrderException.ORDER_NOT_FOUND::get);
        //when
        List<OrderItemDTO> orderItemDTOS=new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemDTO orderItemDTO=new OrderItemDTO();
            orderItemDTO.setPrice(orderItem.getCourse().getCoursePrice());
            orderItemDTOS.add(new OrderItemDTO(orderItem));
        }

        OrderDTO orderDTO = new OrderDTO(order);

        // then
        if (!orderItemDTOS.isEmpty()) {
            for (OrderItemDTO orderItemDTO : orderItemDTOS) {
                System.out.println("orderItemDTO = " + orderItemDTO);
            }
        }


    }

    @Test
    @Transactional
    @Commit
    public void update(){
        Long orderId = 1L;
        Long courseId = 1L;
        List<CourseDTO> courseDTOList = new ArrayList<>();
        Course foundCourse = courseRepository.findById(courseId).orElseThrow(CourseException.COURSE_NOT_FOUND::get);
        // update 테스트 데이터

        OrderDTO orderDTO =new OrderDTO();
        orderDTO.setOrderStatus(String.valueOf(OrderStatus.FAILED));

        orderDTO.setOrderId(orderId);
        orderDTO.getOrderItemDTOList().clear();

        OrderItemDTO orderItemDTO = OrderItemDTO.builder().orderId(orderId)
                .courseId(foundCourse.getCourseId())
                .price(foundCourse.getCoursePrice())
                .courseName(foundCourse.getCourseName()).build();

        orderDTO.getOrderItemDTOList().add(orderItemDTO);
        orderDTO.getOrderItemDTOList().add(orderItemDTO);


        Order order = orderRepository.findById(orderDTO.getOrderId()).orElseThrow();
        order.changeOrderStatus(OrderStatus.valueOf(orderDTO.getOrderStatus()));
        order.getOrderItems().clear();

        for (OrderItemDTO dto : orderDTO.getOrderItemDTOList()) {
            Course course = courseRepository.save(courseRepository.findById(dto.getCourseId()).orElseThrow());
            log.info("course -----" + course);
            OrderItem orderItem = OrderItem.builder().course(course)
                    .price(course.getCoursePrice())
                    .build();
            order.getOrderItems().add(orderItem);
        }
        Assertions.assertEquals(order.getOrderStatus(), OrderStatus.FAILED);
    }
    @Test
    @Transactional
    public void delete() {
        Long orderId = 1L;
        Order order = orderRepository.findById(orderId).orElseThrow();

    }
}

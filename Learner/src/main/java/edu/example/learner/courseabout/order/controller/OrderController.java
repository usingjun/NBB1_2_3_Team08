package edu.example.learner.courseabout.order.controller;

import edu.example.learner.courseabout.order.dto.OrderDTO;
import edu.example.learner.courseabout.order.dto.OrderUpdateDTO;
import edu.example.learner.courseabout.course.service.CourseService;
import edu.example.learner.courseabout.order.dto.PurchaseRequest;
import edu.example.learner.courseabout.order.service.OrderService;
import edu.example.learner.security.auth.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/order")
@Tag(name = "주문 컨트롤러", description = "주문 CRUD를 담당하는 컨트롤러입니다.")
public class OrderController {
    private final OrderService orderService;
    private final CourseService courseService;

    @PostMapping("/{memberId}")
    @Operation(summary = "주문 생성", description = "회원 ID로 새로운 주문을 생성합니다.")
    public ResponseEntity<OrderDTO> createOrder(
            @Parameter(description = "주문 데이터") @RequestBody OrderDTO orderDTO,
            @Parameter(description = "회원 ID") @PathVariable Long memberId) {
        log.info("회원 ID: {}의 주문 생성 데이터: {}", memberId, orderDTO);
        return ResponseEntity.ok(orderService.add(orderDTO, memberId));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 조회", description = "주문 ID로 특정 주문의 세부 정보를 조회합니다.")
    public ResponseEntity<OrderDTO> readOrder(
            @Parameter(description = "조회할 주문 ID") @PathVariable("orderId") Long orderId) {
        log.info("주문 조회: {}", orderId);
        return ResponseEntity.ok(orderService.read(orderId));
    }

    @GetMapping("/list/{memberId}")
    @Operation(summary = "회원의 주문 목록 조회", description = "회원 ID로 모든 주문 목록을 조회합니다.")
    public ResponseEntity<List<OrderDTO>> getOrders(
            @Parameter(description = "회원 ID") @PathVariable Long memberId) {
        List<OrderDTO> orderDTOList = orderService.getOrdersById(memberId);
        return ResponseEntity.ok(orderDTOList);
    }

    @GetMapping("/list/admin")
    @Operation(summary = "관리자 주문 목록 조회", description = "관리자가 모든 주문을 조회합니다.")
    public ResponseEntity<List<OrderDTO>> readAllOrders() {
        log.info("모든 주문 조회");
        return ResponseEntity.ok(orderService.readAll());
    }

    @PutMapping("/{orderId}/")
    @Operation(summary = "주문 수정", description = "주문 ID로 특정 주문의 세부 정보를 수정합니다.")
    public ResponseEntity<OrderUpdateDTO> updateOrder(
            @Parameter(description = "주문 수정 데이터") @RequestBody OrderUpdateDTO orderUpdateDTO,
            @Parameter(description = "수정할 주문 ID") @PathVariable("orderId") Long orderId) {
        log.info("주문 수정: {}", orderUpdateDTO);
        return ResponseEntity.ok(orderService.update(orderUpdateDTO, orderId));
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 삭제", description = "주문 ID로 특정 주문을 삭제합니다.")
    public ResponseEntity<?> deleteOrder(
            @Parameter(description = "삭제할 주문 ID") @PathVariable Long orderId) {
        log.info("주문 삭제: {}", orderId);
        orderService.delete(orderId);
        return ResponseEntity.ok(Map.of("success", "ok"));
    }

    @PostMapping("/purchase/{orderId}")
    @Operation(summary = "주문 결제" ,description = "주문 ID에 있는 아이템들을 결제합니다")
    public ResponseEntity<OrderDTO> purchaseOrderItems(@PathVariable Long orderId, @RequestBody PurchaseRequest purchaseRequest) {
        Long memberId = purchaseRequest.getMemberId();
        OrderDTO orderDTO = orderService.purchaseOrderItems(orderId, memberId);
        return ResponseEntity.ok(orderDTO);
    }


    // 사용자 정보를 가져올 수 있는 경우 주석 해제 후 사용할 수 있음
    /*
    @GetMapping("/list/")
    public ResponseEntity<List<OrderDTO>> getOrders(@AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        Long memberId = userPrincipal.getUsername(); // 현재 로그인된 사용자의 memberId를 가져옴
        List<OrderDTO> orderDTOList = orderService.getOrdersById(memberId);
        return ResponseEntity.ok(orderDTOList);
    }

    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        Long memberId = userPrincipal.getId(); // 현재 로그인된 사용자의 memberId를 가져옴
        log.info("회원 ID: {}의 주문 생성 데이터: {}", memberId, orderDTO);
        return ResponseEntity.ok(orderService.add(orderDTO, memberId));
    }
    */
}

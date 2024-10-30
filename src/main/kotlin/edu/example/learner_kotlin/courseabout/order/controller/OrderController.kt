package edu.example.learner_kotlin.courseabout.order.controller

import edu.example.learner_kotlin.courseabout.course.service.CourseService
import edu.example.learner_kotlin.courseabout.order.service.OrderService
import edu.example.learner_kotlin.log
import edu.leranermig.order.dto.OrderDTO
import edu.leranermig.order.dto.OrderUpdateDTO
import edu.leranermig.order.dto.PurchaseRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Map


@RestController
@RequestMapping("/order")
@Tag(name = "주문 컨트롤러", description = "주문 CRUD를 담당하는 컨트롤러입니다.")
class OrderController (
    private val orderService: OrderService,
    private val courseService: CourseService
){


    @PostMapping("/{memberId}")
    @Operation(summary = "주문 생성", description = "회원 ID로 새로운 주문을 생성합니다.")
    fun createOrder(
        @Parameter(description = "주문 데이터") @RequestBody orderDTO: OrderDTO,
        @Parameter(description = "회원 ID") @PathVariable memberId: Long
    ): ResponseEntity<OrderDTO> {
        log.info("회원 ID: {}의 주문 생성 데이터: {}", memberId, orderDTO)
        return ResponseEntity.ok(orderService.add(orderDTO, memberId))
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 조회", description = "주문 ID로 특정 주문의 세부 정보를 조회합니다.")
    fun readOrder(
        @Parameter(description = "조회할 주문 ID") @PathVariable("orderId") orderId: Long
    ): ResponseEntity<OrderDTO> {
        log.info("주문 조회: {}", orderId)
        return ResponseEntity.ok(orderService.read(orderId))
    }

    @GetMapping("/list/{memberId}")
    @Operation(summary = "회원의 주문 목록 조회", description = "회원 ID로 모든 주문 목록을 조회합니다.")
    fun getOrders(
        @Parameter(description = "회원 ID") @PathVariable memberId: Long
    ): ResponseEntity<List<OrderDTO>> {
        val orderDTOList: List<OrderDTO> = orderService.getOrdersById(memberId)
        return ResponseEntity.ok<List<OrderDTO>>(orderDTOList)
    }

    @GetMapping("/list/admin")
    @Operation(summary = "관리자 주문 목록 조회", description = "관리자가 모든 주문을 조회합니다.")
    fun readAllOrders(): ResponseEntity<List<OrderDTO>> {
        log.info("모든 주문 조회")
        return ResponseEntity.ok(orderService.readAll())
    }

    @PutMapping("/{orderId}/")
    @Operation(summary = "주문 수정", description = "주문 ID로 특정 주문의 세부 정보를 수정합니다.")
    fun updateOrder(
        @Parameter(description = "주문 수정 데이터") @RequestBody orderUpdateDTO: OrderUpdateDTO,
        @Parameter(description = "수정할 주문 ID") @PathVariable("orderId") orderId: Long
    ): ResponseEntity<OrderUpdateDTO> {
        log.info("주문 수정: {}", orderUpdateDTO)
        return ResponseEntity.ok(orderService.update(orderUpdateDTO, orderId))
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 삭제", description = "주문 ID로 특정 주문을 삭제합니다.")
    fun deleteOrder(
        @Parameter(description = "삭제할 주문 ID") @PathVariable orderId: Long
    ): ResponseEntity<*> {
        log.info("주문 삭제: {}", orderId)
        orderService!!.delete(orderId)
        return ResponseEntity.ok(Map.of("success", "ok"))
    }

    @PostMapping("/purchase/{orderId}")
    @Operation(summary = "주문 결제", description = "주문 ID에 있는 아이템들을 결제합니다")
    fun purchaseOrderItems(
        @PathVariable orderId: Long,
        @RequestBody purchaseRequest: PurchaseRequest
    ): ResponseEntity<OrderDTO> {
        val memberId: Long = purchaseRequest.memberId!!
        val orderDTO: OrderDTO = orderService.purchaseOrderItems(orderId, memberId)
        return ResponseEntity.ok<OrderDTO>(orderDTO)
    } // 사용자 정보를 가져올 수 있는 경우 주석 해제 후 사용할 수 있음
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

package edu.example.learner_kotlin.courseabout.course.controller



import edu.example.learner_kotlin.courseabout.course.dto.CourseDTO
//import edu.example.learner_kotlin.courseabout.course.dto.MemberCourseDTO
import edu.example.learner_kotlin.courseabout.course.service.CourseService
//import edu.example.learner_kotlin.courseabout.order.service.OrderService
import edu.example.learner_kotlin.courseabout.video.dto.VideoDTO
import edu.example.learner_kotlin.courseabout.video.service.VideoService
import edu.example.learner_kotlin.log
//import edu.leranermig.order.dto.OrderDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course")
@Tag(name = "강의 관리", description = "강의 CRUD 및 관련 작업을 수행합니다.")
class CourseController(
    private var  courseService: CourseService,
//    private val orderService: OrderService,
    private val videoService: VideoService,
) {


    @PostMapping
    @Operation(summary = "강의 생성", description = "새로운 강의를 생성합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "강의가 성공적으로 생성되었습니다."
        ), ApiResponse(
            responseCode = "400",
            description = "잘못된 요청"
        )]
    )

    fun createCourse(@RequestBody courseDTO: CourseDTO): ResponseEntity<CourseDTO> {
        log.info("Creating course {}", courseDTO)
        return ResponseEntity.ok(courseService.addCourse(courseDTO))
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "강의 조회", description = "강의 ID로 특정 강의를 조회합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "강의가 성공적으로 조회되었습니다."
        ), ApiResponse(
            responseCode = "404",
            description = "강의를 찾을 수 없습니다."
        )]
    )
    fun readCourse(@PathVariable courseId: Long): ResponseEntity<CourseDTO> {
        log.info("Reading course {}", courseId)
        val courseDTO: CourseDTO = courseService.read(courseId)
        return ResponseEntity.ok(courseDTO)
    }

    @GetMapping("/list")
    @Operation(summary = "모든 강의 목록 조회", description = "모든 강의의 목록을 조회합니다.")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "모든 강의 목록이 성공적으로 조회되었습니다.")])
    fun readCourseList(): ResponseEntity<List<CourseDTO>> {
        log.info("Reading all course list")
        return ResponseEntity.ok(courseService.readAll())
    }


    @GetMapping("/video/{courseId}")
    @Operation(summary = "강의의 비디오 목록 조회", description = "강의 ID로 해당 강의에 포함된 비디오 목록을 조회합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "비디오 목록이 성공적으로 조회되었습니다."
        ), ApiResponse(
            responseCode = "404",
            description = "강의를 찾을 수 없습니다."
        )]
    )
    fun getVideosByCourseId(@PathVariable courseId: Long?): ResponseEntity<List<VideoDTO>> {
        val videos: List<VideoDTO> = videoService.getVideosByCourseId(courseId)
        return ResponseEntity.ok<List<VideoDTO>>(videos)
    }

    @PutMapping("{courseId}")
    @Operation(summary = "강의 수정", description = "기존 강의를 수정합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "강의가 성공적으로 수정되었습니다."
        ), ApiResponse(
            responseCode = "404",
            description = "강의를 찾을 수 없습니다."
        )]
    )
    fun updateCourse(
        @PathVariable courseId: Long,
        @RequestBody courseDTO: CourseDTO
    ): ResponseEntity<CourseDTO> {
        courseDTO.courseId = courseId

        log.info("Updating course {}", courseDTO)
        return ResponseEntity.ok<CourseDTO>(courseService.updateCourse(courseDTO))
    }

//    @DeleteMapping("/{courseId}")
//    @Operation(summary = "강의 삭제", description = "강의 ID로 특정 강의를 삭제합니다.")
//    @ApiResponses(
//        value = [ApiResponse(
//            responseCode = "200",
//            description = "강의가 성공적으로 삭제되었습니다."
//        ), ApiResponse(
//            responseCode = "404",
//            description = "강의를 찾을 수 없습니다."
//        )]
//    )
//    fun deleteCourse(@PathVariable courseId: Long): ResponseEntity<*> {
//        // 강좌 삭제
//        courseService.deleteCourse(courseId)
//
//        // 모든 주문 조회
//        val orders: List<OrderDTO> = orderService.readAll()
//
//        // 주문이 비어있으면 삭제
//        for (order in orders) {
//            if (order.orderItemDTOList!!.isEmpty()) {
//                orderService.delete(order.orderId!!)
//            }
//        }
//        return ResponseEntity.ok<Map<String, String>>(java.util.Map.of<String, String>("success", "강좌가 삭제되었습니다."))
//    }

//    @GetMapping("/{memberId}/list")
//    @Operation(summary = "내 수강 정보 조회", description = "회원 ID로 해당 회원의 수강 정보를 조회합니다.")
//    @ApiResponses(
//        value = [ApiResponse(
//            responseCode = "200",
//            description = "수강 정보가 성공적으로 조회되었습니다."
//        ), ApiResponse(
//            responseCode = "404",
//            description = "회원 정보를 찾을 수 없습니다."
//        )]
//    )
//    fun readCourseListByMemberId(@PathVariable memberId: Long): ResponseEntity<List<MemberCourseDTO>> {
//        log.info("Reading course list for member {}", memberId)
//        return ResponseEntity.ok(courseService.getMemberCoursesByMemberId(memberId))
//    }

    @GetMapping("/{courseId}/member-nickname")
    @Operation(summary = "코스의 강사 닉네임 조회", description = "코스의 강사 닉네임을 조회합니다.")
    fun getCourseInstructorNickname(@PathVariable courseId: Long): ResponseEntity<String> {
        val instructorNickname: String = courseService.read(courseId).memberNickname!!

        return ResponseEntity.ok<String>(instructorNickname)
    }

//    @GetMapping("/instruct/list/{nickname}")
//    @Operation(summary = "강사의 본인 강의 조회", description = "강사가 본인의 강의를 조회")
//    fun readInstructList(@PathVariable nickname: String): ResponseEntity<List<CourseDTO>> {
//        log.info("Reading course instruct list {}", nickname)
//
//        return ResponseEntity.ok<List<CourseDTO>>(courseService.getCoursesByNickname(nickname))
//    }
}

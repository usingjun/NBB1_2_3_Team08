//package edu.example.learner_kotlin.courseabout.course.dto
//
//import edu.example.learner_kotlin.courseabout.course.entity.MemberCourse
//
//import java.util.*
//
//
//data class MemberCourseDTO(
//    var courseId: Long? = null,
//    var courseName: String? = null,
//    var instructor: String? = null,
//    var purchaseDate: Date? = Date()
//) {
//    constructor(memberCourse: MemberCourse): this() {
//        this.courseId= memberCourse.course!!.courseId
//        this.courseName = memberCourse.course!!.courseName
//        this.instructor = memberCourse.member!!.nickname
//        this.purchaseDate = memberCourse.purchaseDate
//    }
//}
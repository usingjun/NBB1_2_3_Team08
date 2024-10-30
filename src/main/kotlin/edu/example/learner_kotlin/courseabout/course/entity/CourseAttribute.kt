package edu.example.learner_kotlin.courseabout.course.entity

import edu.example.learner_kotlin.courseabout.exception.CourseException

enum class CourseAttribute {
    JAVA,
    C,
    JAVASCRIPT,
    PYHTON,
    ETC;
    companion object{
        fun fromString(str: String): CourseAttribute {
            return when(str){
                "JAVA" -> JAVA
                "C" -> C
                "JAVASCRIPT" -> JAVASCRIPT
                "PYHTON" -> PYHTON
                "ETC" -> ETC
                else -> {throw CourseException.COURSE_NOT_FOUND.courseException}
            }
        }
    }
}

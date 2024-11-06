import edu.example.learner_kotlin.courseabout.course.exception.VideoTaskException

enum class VideoException(val message: String, val code: Int) {
    VIDEO_NOT_FOUND("VIDEO NOT FOUND", 404),
    VIDEO_ADD_FAILED("VIDEO ADD FAILED", 412),
    VIDEO_NOT_MODIFIED("VIDEO NOT MODIFIED", 422),
    VIDEO_NOT_DELETED("VIDEO NOT DELETED", 423);

    // VideoTaskException을 반환하는 메소드
    fun get(): VideoTaskException {
        return VideoTaskException(message, code)
    }
}

package edu.example.learner_kotlin.courseabout.video.exception

enum class VideoException(message: String, code: Int) {
    VIDEO_NOT_FOUND("VIDEO NOT FOUND", 404),
    VIDEO_ADD_FAILED("VIDEO ADD FAILED", 412),
    VIDEO_NOT_MODIFIED("VIDEO NOT MODIFIED", 422),
    VIDEO_NOT_DELETED("VIDEO NOT DELETED", 423);

    private val videoTaskException = VideoTaskException(message, code)

    fun get(): VideoTaskException {
        return videoTaskException
    }
}

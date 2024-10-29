package edu.example.learner.courseabout.exception;

public enum VideoException {
    VIDEO_NOT_FOUND("VIDEO NOT FOUND", 404),
    VIDEO_ADD_FAILED("VIDEO ADD FAILED", 412),
    VIDEO_NOT_MODIFIED("VIDEO NOT MODIFIED", 422),
    VIDEO_NOT_DELETED("VIDEO NOT DELETED", 423);

    private VideoTaskException videoTaskException;

    VideoException(String message, int code) {
        videoTaskException = new VideoTaskException(message, code);
    }

    public VideoTaskException get() {
        return videoTaskException;
    }
}

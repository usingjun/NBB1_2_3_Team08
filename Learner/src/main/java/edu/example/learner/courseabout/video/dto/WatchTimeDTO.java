package edu.example.learner.courseabout.video.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WatchTimeDTO {
    private float currentTime;
    private float duration;

    // Getters and Setters
    public float getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(float currentTime) {
        this.currentTime = currentTime;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}

package edu.example.learner.alarm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AlarmTaskException extends RuntimeException {
    public AlarmTaskException(String message) {
        super(message);
    }
}

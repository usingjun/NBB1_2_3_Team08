package edu.example.learner.alarm.exception;

public enum AlarmException {


    ALARM_NOT_FOUND("ALARM NOT FOUND",404),
    ALARM_ADD_FAILED("ALARM ADD FALIED", 412),
    ALARM_NOT_MODIFIED("ALARM NOT MODIFIED", 422),
    ALARM_NOT_DELETED("ALARM NOT DELETED", 423);

    private AlarmTaskException alarmTaskException;
    AlarmException(String message, int code){
        
        alarmTaskException = new AlarmTaskException();
    };
    public AlarmTaskException get(){
        return alarmTaskException;
    }
}

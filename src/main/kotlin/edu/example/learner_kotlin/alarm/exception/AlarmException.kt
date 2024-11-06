package edu.example.learner_kotlin.alarm.exception

enum class AlarmException(message: String, code: Int) {
    ALARM_NOT_FOUND("ALARM NOT FOUND", 404),
    ALARM_ADD_FAILED("ALARM ADD FALIED", 412),
    ALARM_NOT_MODIFIED("ALARM NOT MODIFIED", 422),
    ALARM_NOT_DELETED("ALARM NOT DELETED", 423);

    private val alarmTaskException: AlarmTaskException = AlarmTaskException(message,code)

    fun get(): AlarmTaskException {
        return alarmTaskException
    }
}
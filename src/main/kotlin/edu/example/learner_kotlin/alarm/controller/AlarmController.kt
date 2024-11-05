package edu.example.learner_kotlin.alarm.controller

import edu.example.learner_kotlin.alarm.dto.AlarmDTO
import edu.example.learner_kotlin.alarm.service.AlarmService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/alarm")
class AlarmController(private val alarmService: AlarmService) {

    @PostMapping
    fun createAlarm(
        @RequestParam memberId: Long,
        @RequestParam alarmContent: String,
        @RequestParam alarmTitle: String
    ): ResponseEntity<AlarmDTO> {
        val alarmDTO = alarmService.createAlarm(memberId, alarmContent, alarmTitle)
        return ResponseEntity(alarmDTO, HttpStatus.CREATED)
    }

    @GetMapping("/{alarmId}")
    fun getAlarmById(@PathVariable alarmId: Long): ResponseEntity<AlarmDTO> {
        val alarmDTO = alarmService.getAlarmById(alarmId)
        return ResponseEntity(alarmDTO, HttpStatus.OK)
    }

    @GetMapping("/member/{memberId}")
    fun getAlarmsByMember(@PathVariable memberId: Long): ResponseEntity<List<AlarmDTO>> {
        val alarms = alarmService.getAlarmsByMember(memberId)
        return ResponseEntity(alarms, HttpStatus.OK)
    }

    @PutMapping("/{alarmId}")
    fun updateAlarm(
        @PathVariable alarmId: Long,
        @RequestBody alarmDTO: AlarmDTO
    ): ResponseEntity<AlarmDTO> {
        val updatedAlarm = alarmService.updateAlarm(alarmId, alarmDTO)
        return ResponseEntity(updatedAlarm, HttpStatus.OK)
    }

    @DeleteMapping("/{alarmId}")
    fun deleteAlarm(@PathVariable alarmId: Long): ResponseEntity<Void> {
        alarmService.deleteAlarm(alarmId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/send")
    fun createAlarmSendToUser(
        @RequestParam alarmContent: String,
        @RequestParam alarmTitle: String
    ): ResponseEntity<Void> {
        alarmService.createAlarmSendToUser(alarmContent, alarmTitle)
        return ResponseEntity(HttpStatus.OK)
    }
}

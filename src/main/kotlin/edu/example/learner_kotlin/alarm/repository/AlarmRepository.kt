package edu.example.learner_kotlin.alarm.repository

import edu.example.learner_kotlin.alarm.entity.Alarm
import edu.example.learner_kotlin.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface AlarmRepository : JpaRepository<Alarm, Long> {
    fun findByMember(member: Member): List<Alarm>//
// fun findMyMember(member: Member) : List<Alarm>
}
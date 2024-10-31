package edu.example.learner_kotlin.studytable.dto

import edu.example.learner_kotlin.studytable.entity.StudyTable
import java.time.LocalDate

data class StudyTableDTO(
    var studyTableId: Long? = null,

    var studyDate: LocalDate? = null,

    var studyTime: Int = 0,

    var completed: Int = 0,

    var memberId: Long? = null,
) {
    constructor(studyTable: StudyTable) : this(
        studyTable.studyTableId,
        studyTable.studyDate,
        studyTable.studyTime,
        studyTable.completed,
        studyTable.member?.memberId,
    )
}
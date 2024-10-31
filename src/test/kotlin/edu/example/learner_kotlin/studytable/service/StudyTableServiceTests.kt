package edu.example.learner_kotlin.studytable.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class StudyTableServiceTests {
    @Autowired
    private lateinit var studyTableService: StudyTableService

//    @Test
//    fun testRead() {
//        val studyTableId = 1L
//
//        studyTableService.read(studyTableId).run {
//            assertEquals(studyTableId, this.studyTableId)
//        }
//    }
}
package edu.example.learner.studytable.service;

import edu.example.learner.studytable.dto.StudyTableDTO;

import java.time.LocalDate;
import java.util.List;

public interface StudyTableService {

    StudyTableDTO read(Long studyTableId);

    boolean readByDate(Long memberId);

    StudyTableDTO readDTOByDate(Long memberId);

    StudyTableDTO register(StudyTableDTO studyTableDTO);

    StudyTableDTO update(StudyTableDTO studyTableDTO);

    StudyTableDTO delete(Long studyTableId);

    int getWeeklyStudyTime(Long memberId, LocalDate localDate);

    int getYearlyStudyTime(Long memberId, int year);

    int getWeeklyCompleted(Long memberID, LocalDate localDate);

    int getMonthlyCompleted(Long memberID, int month);

    int getYearlyCompleted(Long memberID, int year);

    List<Object[]> getWeeklySummary(Long memberId, LocalDate localDate);

    List<Object[]> getYearlySummary(Long memberId, int year);
}

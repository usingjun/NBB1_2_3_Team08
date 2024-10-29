package edu.example.learner.studytable.service;

import edu.example.learner.member.exception.LearnerException;
import edu.example.learner.studytable.dto.StudyTableDTO;
import edu.example.learner.studytable.entity.StudyTable;
import edu.example.learner.studytable.repository.StudyTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class StudyTableServiceImpl implements StudyTableService {
    private final StudyTableRepository studyTableRepository;

    @Override
    public StudyTableDTO read(Long studyTableId) {
        return new StudyTableDTO(studyTableRepository.findById(studyTableId).orElseThrow(LearnerException.NOT_FOUND_EXCEPTION::getTaskException));
    }

    @Override
    public boolean readByDate(Long memberId) {
        return studyTableRepository.findByDate(LocalDate.now(), memberId).isPresent();
    }

    @Override
    public StudyTableDTO readDTOByDate(Long memberId) {
        return new StudyTableDTO(studyTableRepository.findByDate(LocalDate.now(), memberId).orElseThrow(LearnerException.NOT_FOUND_EXCEPTION::getTaskException));
    }

    @Override
    public StudyTableDTO register(StudyTableDTO studyTableDTO) {
        try {
            StudyTable studyTable = studyTableRepository.save(studyTableDTO.toEntity());
            return new StudyTableDTO(studyTable);
        } catch (Exception e) {
            log.error(e);
            throw LearnerException.NOT_REGISTERED_EXCEPTION.getTaskException();
        }
    }

    @Override
    public StudyTableDTO update(StudyTableDTO studyTableDTO) {
        StudyTable studyTable = studyTableRepository.findById(studyTableDTO.getStudyTableId()).orElseThrow(LearnerException.NOT_FOUND_EXCEPTION::getTaskException);
        try {
            studyTable.changeCompleted(studyTable.getCompleted() + studyTableDTO.getCompleted());
            studyTable.changeStudyTime(studyTable.getStudyTime() + studyTableDTO.getStudyTime());
            return new StudyTableDTO(studyTable);
        } catch (Exception e) {
            log.error(e);
            throw LearnerException.NOT_MODIFIED_EXCEPTION.getTaskException();
        }
    }

    @Override
    public StudyTableDTO delete(Long studyTableId) {
        studyTableRepository.findById(studyTableId).orElseThrow(LearnerException.NOT_FOUND_EXCEPTION::getTaskException);
        try {
            studyTableRepository.deleteById(studyTableId);
        } catch (Exception e) {
            log.error(e);
            throw LearnerException.NOT_REMOVED_EXCEPTION.getTaskException();
        }
        return null;
    }

    @Override
    public int getWeeklyStudyTime(Long memberId, LocalDate localDate) {
        LocalDate startDate = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        log.info("--- startDate : " + startDate + " endDate : " + endDate);
        return studyTableRepository.getWeeklyStudyTime(memberId, startDate, endDate);
    }

    @Override
    public int getYearlyStudyTime(Long memberId, int year) {
        return studyTableRepository.getYearlyStudyTime(memberId, year);
    }

    @Override
    public int getWeeklyCompleted(Long memberID, LocalDate localDate) {
        LocalDate startDate = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        return studyTableRepository.getWeeklyCompleted(memberID, startDate, endDate);
    }

    @Override
    public int getMonthlyCompleted(Long memberID, int month) {
        return studyTableRepository.getMonthlyCompleted(memberID, month);
    }

    @Override
    public int getYearlyCompleted(Long memberID, int year) {
        return studyTableRepository.getYearlyCompleted(memberID, year);
    }

    @Override
    public List<Object[]> getWeeklySummary(Long memberId, LocalDate localDate) {
        LocalDate startDate = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        return studyTableRepository.getWeeklySummary(memberId, startDate, endDate);
    }

    @Override
    public List<Object[]> getYearlySummary(Long memberId, int year) {
        return studyTableRepository.getYearlySummary(memberId, year);
    }
}

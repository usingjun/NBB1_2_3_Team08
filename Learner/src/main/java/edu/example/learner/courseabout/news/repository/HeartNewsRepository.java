package edu.example.learner.courseabout.news.repository;

import edu.example.learner.courseabout.news.entity.HeartNews;
import edu.example.learner.courseabout.news.entity.NewsEntity;
import edu.example.learner.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartNewsRepository extends JpaRepository<HeartNews, Long> {
    Optional<HeartNews> findByMemberAndNewsEntity(Member member, NewsEntity newsEntity);
}

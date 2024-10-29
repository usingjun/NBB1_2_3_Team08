package edu.example.learner.courseabout.news.entity;

import edu.example.learner.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "heart")
public class HeartNews {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "heart_news_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false) // member_id는 필수
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "news_id")
    private NewsEntity newsEntity;

    @Builder
    public HeartNews(Member member, NewsEntity newsEntity) {
        this.member = member;
        this.newsEntity = newsEntity;
    }
}

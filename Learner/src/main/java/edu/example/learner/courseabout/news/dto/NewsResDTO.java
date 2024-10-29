package edu.example.learner.courseabout.news.dto;

import edu.example.learner.courseabout.news.entity.NewsEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsResDTO {
    private Long newsId;
    private String newsName;
    private String newsDescription;
    private LocalDateTime newsDate;
    private LocalDateTime lastModifiedDate;
    private int viewCount;
    private int likeCount;

    public static NewsResDTO fromEntity(NewsEntity entity) {
        return NewsResDTO.builder()
                .newsId(entity.getNewsId())
                .newsName(entity.getNewsName())
                .newsDescription(entity.getNewsDescription())
                .newsDate(entity.getNewsDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .viewCount(entity.getViewCount())
                .likeCount(entity.getLikeCount())
                .build();
    }
}

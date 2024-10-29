package edu.example.learner.courseabout.news.dto;

import edu.example.learner.courseabout.news.entity.NewsEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
//@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsRqDTO {
//    private Long newsId;

    @NotBlank(message = "새소식을 적어주세요.")
    private String newsName;

    private String newsDescription;

    public NewsEntity toEntity() {
        return NewsEntity.builder()
//                .newsId(newsId)
                .newsName(newsName)
                .newsDescription(newsDescription)
                .build();
    }
}

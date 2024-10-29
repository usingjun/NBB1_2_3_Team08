//package edu.example.learner.courseabout.video;
//
//import edu.example.learner.courseabout.video.entity.Video;
//import edu.example.learner.courseabout.video.repository.VideoRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class VideoInitializer {
//    private final VideoRepository videoRepository;
//
//    @PostConstruct
//    public void init() {
//        insertVideos();
//    }
//
//    private void insertVideos() {
//        // 예시로 추가할 비디오 데이터
//        Video video1 = Video.builder()
//                .title("첫 번째 동영상")
//                .url("https://www.youtube.com/watch?v=cJ9xdW_hqR4&list=PLlV7zJmoG4XJfK8vVL2E2NX8ej73vjNlh")
//                .description("첫 번째 동영상 설명")
//                .totalDuration(300L) // 5분
//                .currentTime(0L) // 시작 시 현재 시간은 0초
//                .build();
//
//        Video video2 = Video.builder()
//                .title("두 번째 동영상")
//                .url("https://www.youtube.com/watch?v=4-iyppqNCyg&list=PLlV7zJmoG4XJfK8vVL2E2NX8ej73vjNlh&index=2")
//                .description("두 번째 동영상 설명")
//                .totalDuration(600L) // 10분
//                .currentTime(0L) // 시작 시 현재 시간은 0초
//                .build();
//        videoRepository.save(video1);
//        videoRepository.save(video2);
//    }
//}

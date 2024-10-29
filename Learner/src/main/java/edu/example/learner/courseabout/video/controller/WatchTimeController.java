package edu.example.learner.courseabout.video.controller;


import edu.example.learner.courseabout.video.dto.WatchTimeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WatchTimeController {

    @PostMapping("/test/watch")
    public ResponseEntity<Void> receiveWatchTime(@RequestBody WatchTimeDTO watchTimeDTO) {
        // 시청 시간 처리 로직 (DB 저장 등)
        System.out.println("Current Time: " + watchTimeDTO.getCurrentTime());
        System.out.println("Duration: " + watchTimeDTO.getDuration());

        // 응답 반환
        return ResponseEntity.ok().build();

    }
}
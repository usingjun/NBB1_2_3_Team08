package edu.example.learner.courseabout.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.example.learner.courseabout.video.entity.Video;
import edu.example.learner.courseabout.video.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
@SpringBootTest
@Transactional
@Slf4j
@Controller
class VideoServiceTest {

    @Autowired
    private VideoRepository videoRepository;

    private Video video;

    @BeforeEach
    void setUp() {
        video = new Video();
        video.setTitle("Test Video");
        video.setUrl("http://example.com/test-video.mp4");
        video.setDescription("This is a test video.");
        // 비디오 저장
        videoRepository.save(video);
    }

    @Test
    @Transactional
    void testFindById() {
        Optional<Video> foundVideo = videoRepository.findById(video.getVideo_Id());
        assertTrue(foundVideo.isPresent());
        assertEquals(video.getTitle(), foundVideo.get().getTitle());
    }

    @Test
    void testUpdateVideo() {
        video.setTitle("Updated Video");
        videoRepository.save(video);

        Video updatedVideo = videoRepository.findById(video.getVideo_Id()).get();
        assertEquals("Updated Video", updatedVideo.getTitle());
    }

    @Test
    void testDeleteVideo() {
        Long videoId = video.getVideo_Id();
        videoRepository.delete(video);
        Optional<Video> deletedVideo = videoRepository.findById(videoId);
        assertFalse(deletedVideo.isPresent());
    }

    @Test
    void testFetchAllVideos() {
        List<Video> videos = videoRepository.findAll();
        assertFalse(videos.isEmpty());
        assertEquals(1, videos.size());
    }
    @Test

    void test (){

    }
}
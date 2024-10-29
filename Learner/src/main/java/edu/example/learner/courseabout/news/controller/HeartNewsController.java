//package edu.example.learner.course.controller;
//
//import edu.example.learner.course.news.dto.HeartNewsReqDTO;
//import edu.example.learner.course.news.entity.HeartNews;
//import edu.example.learner.course.news.service.HeartNewsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("news/heart")
//public class HeartNewsController {
//    private final HeartNewsService heartNewsService;
//
//    @PostMapping
//    public ResponseEntity<?> insert(@RequestBody @Validated HeartNewsReqDTO heartNewsReqDTO) throws Exception {
//        heartNewsService.insert(heartNewsReqDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping
//    public ResponseEntity<?> delete(@RequestBody @Validated HeartNewsReqDTO heartNewsReqDTO) {
//        heartNewsService.delete(heartNewsReqDTO);
//        return ResponseEntity.ok().build();
//    }
//
//}

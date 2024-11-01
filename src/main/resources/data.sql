-- 초기 멤버 3명 삽입
INSERT IGNORE INTO member (member_id, email, password, nickname, role,  phone_number, introduction, create_date)
                VALUES (1, 'ADMIN@gmail.com', '$2a$10$LCxJ1iaCREr0B7tWaNtIT.acyqGUZirHEiHdBMt0DzsOaH.ZYnz02', '관리자', 'ADMIN', '010-1234-5678', '안녕하세요 관리자입니다.', NOW());

INSERT IGNORE INTO member (member_id, email, password, nickname, role,  phone_number, introduction, create_date)
                VALUES (2, 'customer@gmail.com', '$2a$10$XT9BcCo4dRRXdZAo9aQFRuYR3nGg2cUjGSX9Mgb0nHcWWemSaetI6', '학생', 'USER', '010-1111-2222', '안녕하세요 학생입니다.', NOW());

INSERT IGNORE INTO member (member_id, email, password, nickname, role,  phone_number, introduction, create_date)
                VALUES (3, 'instructor@gmail.com', '$2a$10$eYec.uEZMP5yrBt8DtsifuQn14svVZOiD2qKq7sg4RjJT1c2OG0ra', '김영한', 'INSTRUCTOR', '010-3333-4444', '안녕하세요 김영한입니다.', NOW());

INSERT IGNORE INTO member (member_id, email, password, nickname, role,  phone_number, introduction, create_date)
        VALUES (4, 'instructor2@gmail.com', '$2a$10$eYec.uEZMP5yrBt8DtsifuQn14svVZOiD2qKq7sg4RjJT1c2OG0ra', '동빈나', 'INSTRUCTOR', '010-5555-6666', '안녕하세요 동빈나입니다.', NOW());

INSERT IGNORE INTO member (member_id, email, password, nickname, role,  phone_number, introduction, create_date)
        VALUES (5, 'instructor3@gmail.com', '$2a$10$eYec.uEZMP5yrBt8DtsifuQn14svVZOiD2qKq7sg4RjJT1c2OG0ra', '조코딩', 'INSTRUCTOR', '010-7777-8888', '안녕하세요 조코딩입니다.', NOW());

-- 초기 강의 데이터  삽입.
INSERT IGNORE INTO course (course_id, course_attribute, course_name,course_description, course_level, course_price, member_nickname, sale, course_created_date)
                   VALUES (1, 'JAVA', 'JPA 강의', 'JPA 학습', 5, 63000, '김영한', false, NOW());

INSERT IGNORE INTO course (course_id, course_attribute, course_name,course_description, course_level, course_price, member_nickname, sale, course_created_date)
                   VALUES (2, 'JAVA', '스프링 입문', '코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술', 3, 35000, '김영한', false, NOW());

INSERT IGNORE INTO course (course_id, course_attribute, course_name,course_description, course_level, course_price, member_nickname, sale, course_created_date)
                   VALUES (3, 'C', 'C 입문', '코드로 배우는 C언어', 4, 20000, '동빈나', false, NOW());

INSERT IGNORE INTO course (course_id, course_attribute, course_name,course_description, course_level, course_price, member_nickname, sale, course_created_date)
           VALUES (4, 'JAVASCRIPT', '자바스크립트 입문', '자바스크립트 기초 입문 강의 30분 완성', 1, 0, '조코딩', false, NOW());

INSERT IGNORE INTO course (course_id, course_attribute, course_name,course_description, course_level, course_price, member_nickname, sale, course_created_date)
                   VALUES (5, 'PYHTON', '파이썬 입문', '최신 파이썬  코딩 강의', 2, 5000, '조코딩', false, NOW());

-- 초기 동영상 데이터  삽입 --
INSERT IGNORE INTO videos (video_id , course_id, description, title, url, current_video_time, total_video_duration)
                   VALUES (1, 1, 'SQL 중심적인 개발의 문제점' , 'JPA', 'https://www.youtube.com/watch?v=_tMJPysViNU&list=PLumVmq_uRGHgP87Jf1-up391q_es9KiZ9&index=4' , 0.0, 1554);

INSERT IGNORE INTO videos (video_id , course_id, description, title, url, current_video_time, total_video_duration)
                   VALUES (2, 1, 'JPA 소개' , 'JPA', 'https://www.youtube.com/watch?v=nNNVb4l8tiA&list=PLumVmq_uRGHgP87Jf1-up391q_es9KiZ9&index=3' , 0.0, 1721);

INSERT IGNORE INTO videos (video_id , course_id, description, title, url, current_video_time, total_video_duration)
                   VALUES (3, 2, '스프링 입문 강의 소개' , '스프링', 'https://www.youtube.com/watch?v=qyGjLVQ0Hog&list=PLumVmq_uRGHgBrimIp2-7MCnoPUskVMnd&index=1' , 0.0, 301);

INSERT IGNORE INTO videos (video_id , course_id, description, title, url, current_video_time, total_video_duration)
                   VALUES (4, 2, '프로젝트생성' , '스프링', 'https://www.youtube.com/watch?v=NNClHeXzIBA&list=PLumVmq_uRGHgBrimIp2-7MCnoPUskVMnd&index=2' , 0.0, 990);

INSERT IGNORE INTO videos (video_id , course_id, description, title, url, current_video_time, total_video_duration)
                   VALUES (5, 3, 'C언어 Hello Word' , 'C언어', 'https://www.youtube.com/watch?v=dh4hdtZ00EU&t=1s' , 0.0, 645);

INSERT IGNORE INTO videos (video_id , course_id, description, title, url, current_video_time, total_video_duration)
                   VALUES (6, 3, 'C언어 변수' , 'C언어', 'https://www.youtube.com/watch?v=V7TXlm7kpaE' , 0.0, 915);

INSERT IGNORE INTO videos (video_id , course_id, description, title, url, current_video_time, total_video_duration)
                   VALUES (7, 4, '자바스크립트 기초' , '자바스크립트', 'https://www.youtube.com/watch?v=E-PzX2mKGUQ&t=485s' , 0.0, 1889);

INSERT IGNORE INTO videos (video_id , course_id, description, title, url, current_video_time, total_video_duration)
                   VALUES (8, 5, '파이썬 기초' , '파이썬', 'https://www.youtube.com/watch?v=7ttbyGI5igA' , 0.0, 556);
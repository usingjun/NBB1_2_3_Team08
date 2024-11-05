package edu.example.learner_kotlin.member_video.repository

import edu.example.learner_kotlin.member_video.entity.MemberVideo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberVideoRepository: JpaRepository<MemberVideo, Long> {
    @Query("select mv from MemberVideo mv where mv.member.memberId = :memberId and mv.video.videoId = :videoId")
    fun findByMemberIdAndVideoId(@Param("memberId") memberId: Long, @Param("videoId") videoId: Long): MemberVideo?

    @Query("select avg(mv.studyTime )from MemberVideo mv where mv.video.videoId = :videoId")
    fun averageByVideoId(@Param("videoId") videoId: Long): Double?
}
package edu.example.learner_kotlin.member_video.service

import edu.example.learner_kotlin.member_video.dto.MemberVideoDTO

interface MemberVideoService {
    fun read(memberVideoId: Long): MemberVideoDTO
    fun create(memberVideoDTO: MemberVideoDTO): MemberVideoDTO
    fun update(memberVideoDTO: MemberVideoDTO): MemberVideoDTO
    fun delete(memberVideoId: Long)
    fun readByMemberIdAndVideoId(memberId: Long, videoId: Long): MemberVideoDTO
    fun getAverageByVideoId(videoId: Long): Double
    fun isPresentByMemberIdAndVideoId(memberId: Long, videoId: Long): Boolean
}
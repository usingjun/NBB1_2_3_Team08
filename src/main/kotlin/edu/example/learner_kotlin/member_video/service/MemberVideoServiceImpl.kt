package edu.example.learner_kotlin.member_video.service

import edu.example.learner_kotlin.courseabout.video.entity.Video
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member_video.dto.MemberVideoDTO
import edu.example.learner_kotlin.member_video.entity.MemberVideo
import edu.example.learner_kotlin.member_video.repository.MemberVideoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberVideoServiceImpl(private val memberVideoRepository: MemberVideoRepository) : MemberVideoService {
    override fun read(memberVideoId: Long): MemberVideoDTO =
        MemberVideoDTO(
            memberVideoRepository.findByIdOrNull(memberVideoId)
                ?: throw NoSuchElementException("MemberVideo $memberVideoId not found")
        )

    override fun create(memberVideoDTO: MemberVideoDTO): MemberVideoDTO = run {
        val foundMemberVideo =
            memberVideoRepository.findByMemberIdAndVideoId(memberVideoDTO.memberId!!, memberVideoDTO.videoId!!)
        if (foundMemberVideo.isNull()) {
            MemberVideoDTO(memberVideoRepository.save(MemberVideo().apply {
                this.watched = memberVideoDTO.watched
                this.studyTime = memberVideoDTO.studyTime
                this.member = Member().apply { memberId = memberVideoDTO.memberId }
                this.video = Video().apply { videoId = memberVideoDTO.videoId }
            }))
        } else {
            MemberVideoDTO(foundMemberVideo!!)
        }
    }

    @Transactional
    override fun update(memberVideoDTO: MemberVideoDTO): MemberVideoDTO = run {
        val foundMemberVideo =
            memberVideoRepository.findByMemberIdAndVideoId(memberVideoDTO.memberId!!, memberVideoDTO.videoId!!)
                ?: throw NoSuchElementException("MemberVideo not found")
        with(foundMemberVideo) {
            this.watched = if (this.watched!!) this.watched else memberVideoDTO.watched
            this.studyTime = this.studyTime!! + memberVideoDTO.studyTime!!
        }
        MemberVideoDTO(foundMemberVideo)
    }

    override fun delete(memberVideoId: Long) =
        memberVideoRepository.delete(
            memberVideoRepository.findByIdOrNull(memberVideoId)
                ?: throw NoSuchElementException("MemberVideo $memberVideoId not found")
        )

    override fun readByMemberIdAndVideoId(memberId: Long, videoId: Long): MemberVideoDTO =
        MemberVideoDTO(
            memberVideoRepository.findByMemberIdAndVideoId(memberId, videoId)
                ?: throw NoSuchElementException("MemberVideo Not found")
        )

    override fun getAverageByVideoId(videoId: Long): Double =
        memberVideoRepository.averageByVideoId(videoId) ?: 0.0

    override fun isPresentByMemberIdAndVideoId(memberId: Long, videoId: Long): Boolean =
        memberVideoRepository.findByMemberIdAndVideoId(memberId, videoId).isNotNull()

    private fun MemberVideo?.isNull(): Boolean = this == null
    private fun MemberVideo?.isNotNull(): Boolean = this != null
}
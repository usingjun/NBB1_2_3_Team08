//package edu.example.learner_kotlin.member.service
//
//import edu.example.learner_kotlin.member.dto.FollowDTO
//import edu.example.learner_kotlin.member.entity.Follow
//import edu.example.learner_kotlin.member.entity.FollowStatus
//import edu.example.learner_kotlin.member.entity.Member
//import edu.example.learner_kotlin.member.repository.FollowRepository
//import edu.example.learner_kotlin.member.repository.MemberRepository
//import org.springframework.stereotype.Service
//
//@Service
//class FollowService(
//    private val memberRepository: MemberRepository,
//    private val followRepository: FollowRepository
//) {
//
//    fun followUser(followerUsername: String?, followingUsername: String?): String {
//        val follower: Member? = memberRepository.findByUsername(followerUsername).orElseThrow {
//            RuntimeException("Follower not found")
//        }
//        val following: Member? = memberRepository.findByUsername(followingUsername).orElseThrow {
//            RuntimeException("Following not found")
//        }
//
//        if (followRepository!!.existsByFollowerAndFollowing(follower, following)) {
//            return "이미 팔로우 중입니다."
//        }
//
//        val follow = Follow()
//        follow.follower = follower
//        follow.following = following
//        followRepository.save(follow)
//
//        return "팔로우 성공!"
//    }
//
//    fun unfollowUser(followerUsername: String?, followingUsername: String?) {
//        val follower: Member = memberRepository.findByUsername(followerUsername).orElseThrow {
//            RuntimeException("Follower not found")
//        }
//        val following: Member = memberRepository.findByUsername(followingUsername).orElseThrow {
//            RuntimeException("Follower not found")
//        }
//
//        try {
//            if (!followRepository!!.existsByFollowerAndFollowing(follower, following)) {
//                return
//            }
//
//            followRepository.deleteByFollowerAndFollowing(follower, following)
//        } catch (e: Exception) {
//            log.info(e.message)
//            throw RuntimeException("")
//        }
//    }
//
//    fun followingList(username: String?, requestUserId: Long): List<FollowDTO> {
//        val user: Member = memberRepository.findByUsername(username).orElseThrow {
//            RuntimeException(
//                "User not found"
//            )
//        }
//        val requestUser = memberRepository!!.findById(requestUserId).orElseThrow {
//            RuntimeException(
//                "User not found"
//            )
//        }
//
//        val findList = followRepository!!.findByFollowing(user)
//        val followingList: MutableList<FollowDTO> = ArrayList()
//        for ((_, member) in findList) {
//            val status: FollowStatus? = findStatus(member, requestUser)
//
//            val followDTO = FollowDTO(member!!, status)
//            followingList.add(followDTO)
//        }
//
//        return followingList
//    }
//
//    fun followerList(username: String?, requestUserId: Long): List<FollowDTO> {
//        val user: Member = memberRepository.findByUsername(username).orElseThrow {
//            RuntimeException(
//                "User not found"
//            )
//        }
//        val requestUser = memberRepository!!.findById(requestUserId).orElseThrow {
//            RuntimeException(
//                "User not found"
//            )
//        }
//
//        val findList = followRepository!!.findByFollower(user)
//        val followerList: MutableList<FollowDTO> = ArrayList()
//        for ((_, member) in findList) {
//            val status: FollowStatus? = findStatus(requestUser, member)
//
//            val followDTO = FollowDTO(member!!, status)
//            followerList.add(followDTO)
//        }
//
//        return followerList
//    }
//
//    private fun findStatus(following: Member?, follower: Member?): FollowStatus? {
//        if (following === follower) {
//            return null
//        } else if (!followRepository!!.existsByFollowerAndFollowing(follower, following)) {
//            return null
//        }
//        return FollowStatus.FOLLOWING
//    }
//}

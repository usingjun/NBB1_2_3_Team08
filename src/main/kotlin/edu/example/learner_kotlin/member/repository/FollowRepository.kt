package edu.example.learner_kotlin.member.repository

import edu.example.learner_kotlin.member.entity.Follow
import edu.example.learner_kotlin.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository : JpaRepository<Follow?, Long?> {
    fun existsByFollowerAndFollowing(follower: Member?, following: Member?): Boolean

    fun deleteByFollowerAndFollowing(follower: Member?, following: Member?)

    fun findByFollower(follower: Member?): List<Follow?>?

    fun findByFollowing(following: Member?): List<Follow?>?
    fun countByFollowing(following: Member): Long
    fun countByFollower(follower: Member): Long

}

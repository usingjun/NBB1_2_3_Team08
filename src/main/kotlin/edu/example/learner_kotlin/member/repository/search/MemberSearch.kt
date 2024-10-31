package edu.example.learner_kotlin.member.repository.search

import edu.example.learner_kotlin.member.entity.Member

interface MemberSearch {
    fun getMemberInfo(id: Long?): Member?

    val allMembers: List<Member?>?

    fun getMemberByEmail(email: String?): Member?

    fun getMemberByNickName(nickName: String?): Member?
}
package edu.example.learner_kotlin.member.entity

import jakarta.persistence.*

@jakarta.persistence.Entity
data class Follow(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var followId: Long? = null,
    @ManyToOne
    @JoinColumn(name = "follower_id")
    var follower: Member? = null,

    @ManyToOne
    @JoinColumn(name = "following_id")
    var following: Member? = null

) {


}
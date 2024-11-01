package edu.example.learner_kotlin.member.repository;

import edu.example.learner_kotlin.member.entity.Follow;
import edu.example.learner_kotlin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(Member follower, Member following);

    void deleteByFollowerAndFollowing(Member follower, Member following);

    List<Follow> findByFollower(Member follower);

    List<Follow> findByFollowing(Member following);
}

package edu.example.learner.courseabout.order.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {
    private Long memberId;

    // Getter와 Setter
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}

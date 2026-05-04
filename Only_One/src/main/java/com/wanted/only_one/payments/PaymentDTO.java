package com.wanted.only_one.payments;

import com.wanted.only_one.course.dto.CourseDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PaymentDTO {
    private Long pay_id;
    private Long member_id;
    private Timestamp payed_at;
    private Long price;
    private Timestamp expire;

    public PaymentDTO(Long pay_id, Long member_id, Timestamp payed_at, Long price) {

        this.pay_id = pay_id;
        this.member_id = member_id;
        this.payed_at = payed_at;
        this.price = price;
        this.expire = expire;
    }

    public PaymentDTO() {}

    public PaymentDTO(Timestamp payed_at, Long price, Timestamp expire) {
        this.payed_at = payed_at;
        this.price = price;
        this.expire = expire;
    }

    public Long getPay_id() {
        return pay_id;
    }

    public void setPay_id(Long pay_id) {
        this.pay_id = pay_id;
    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public Timestamp getPayed_at() {
        return payed_at;
    }

    public void setPayed_at(Timestamp payed_at) {
        this.payed_at = payed_at;
    }

    public Timestamp getExpireDate() {
        return expire;
    }

    public void setExpireDate() {
        this.expire = expire;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "pay_id=" + pay_id +
                ", member_id=" + member_id +
                ", payed_at=" + payed_at +
                ", price=" + price +
                ", expireDate=" + expire +
                '}';
    }
}

package com.wanted.only_one.study.dto;

public class LectureHistoryDTO {

    private Long history_id;
    private Long member_id;
    private Long lecture_id;
    private String status;

    public LectureHistoryDTO() {
    }

    public LectureHistoryDTO(Long history_id, Long member_id, Long lecture_id, String status) {
        this.history_id = history_id;
        this.member_id = member_id;
        this.lecture_id = lecture_id;
        this.status = status;
    }

    public Long getHistory_id() {
        return history_id;
    }

    public void setHistory_id(Long history_id) {
        this.history_id = history_id;
    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public Long getLecture_id() {
        return lecture_id;
    }

    public void setLecture_id(Long lecture_id) {
        this.lecture_id = lecture_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LectureHistoryDTO{" +
                "history_id=" + history_id +
                ", member_id=" + member_id +
                ", lecture_id=" + lecture_id +
                ", status='" + status + '\'' +
                '}';
    }
}

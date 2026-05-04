package com.wanted.only_one.study.dto;

public class StudyingDTO {
    private Long studying_id;
    private Long course_id;
    private Long member_id;
    private String status;

    public StudyingDTO() {
    }

    public StudyingDTO(Long studying_id, Long course_id, Long member_id, String status) {
        this.studying_id = studying_id;
        this.course_id = course_id;
        this.member_id = member_id;
        this.status = status;
    }

    public StudyingDTO(long courseId) {
        this.course_id = courseId;
    }

    public Long getStudying_id() {
        return studying_id;
    }

    public void setStudying_id(Long studying_id) {
        this.studying_id = studying_id;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StudyingDTO{" +
                "studying_id=" + studying_id +
                ", course_id=" + course_id +
                ", member_id=" + member_id +
                ", status='" + status + '\'' +
                '}';
    }
}

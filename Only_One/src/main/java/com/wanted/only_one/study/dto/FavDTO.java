package com.wanted.only_one.study.dto;

public class FavDTO {

    private Long fav_id;
    private Long member_id;
    private Long course_id;
    private String member_name;
    private String course_title;

    public FavDTO(String title, String name) {
        this.member_name=name;
        this.course_title=title;
    }

    public FavDTO(Long fav_id, Long course_id, Long member_id) {
        this.fav_id = fav_id;
        this.course_id = course_id;
        this.member_id = member_id;
    }

    public FavDTO(long courseId, long memberId) {
        this.course_id = courseId;
        this.member_id = memberId;
    }

    public Long getFav_id() {
        return fav_id;
    }

    public void setFav_id(Long fav_id) {
        this.fav_id = fav_id;
    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    @Override
    public String toString() {
        return "FavDTO{" +
                "fav_id=" + fav_id +
                ", member_id=" + member_id +
                ", course_id=" + course_id +
                ", member_name='" + member_name + '\'' +
                ", course_title='" + course_title + '\'' +
                '}';
    }
}

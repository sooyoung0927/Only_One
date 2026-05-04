package com.wanted.only_one.study.dto;

public class ReviewDTO {

    private Long review_id;
    private Long course_id;
    private Long member_id;
    private String contents;
    private String title;
    private String teacher_name;
    private String review_writer;
    private Double rating;

    public ReviewDTO() {
    }

    public ReviewDTO(Long review_id, Long course_id, Long member_id, String contents, Double rating) {
        this.review_id = review_id;
        this.course_id = course_id;
        this.member_id = member_id;
        this.contents = contents;
        this.rating = rating;
    }

    public ReviewDTO(String title, String contents, double rating, String teacher_name, String review_writer) {
        this.title = title;
        this.contents = contents;
        this.rating = rating;
        this.teacher_name = teacher_name;
        this.review_writer = review_writer;
    }

    public ReviewDTO(String title, String contents, Double rating) {
        this.title = title;
        this.contents = contents;
        this.rating = rating;
    }

    public ReviewDTO(String title, String contents, double rating, String review_writer) {
        this.title = title;
        this.contents = contents;
        this.rating = rating;
        this.review_writer = review_writer;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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

    public Long getReview_id() {
        return review_id;
    }

    public void setReview_id(Long review_id) {
        this.review_id = review_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getReview_writer() {
        return review_writer;
    }

    public void setReview_writer(String review_writer) {
        this.review_writer = review_writer;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "review_id=" + review_id +
                ", course_id=" + course_id +
                ", member_id=" + member_id +
                ", contents='" + contents + '\'' +
                ", title='" + title + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                ", review_writer='" + review_writer + '\'' +
                ", rating=" + rating +
                '}';
    }
}

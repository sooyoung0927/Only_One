package com.wanted.only_one.course.dto;

public class LectureDTO {

    private long lectureId;
    private long courseId;
    private String title;

    public LectureDTO() {}

    public LectureDTO(long courseId, String title) {
        this.courseId = courseId;
        this.title = title;
    }

    public LectureDTO(long lectureId, long courseId, String title) {
        this.lectureId = lectureId;
        this.courseId = courseId;
        this.title = title;
    }

    public long getLectureId() { return lectureId; }
    public long getCourseId()  { return courseId; }
    public String getTitle()   { return title; }

    public void setLectureId(long lectureId) { this.lectureId = lectureId; }
    public void setCourseId(long courseId)   { this.courseId = courseId; }
    public void setTitle(String title)       { this.title = title; }

    @Override
    public String toString() {
        return "LectureDTO{lectureId=" + lectureId + ", courseId=" + courseId + ", title='" + title + "'}";
    }
}

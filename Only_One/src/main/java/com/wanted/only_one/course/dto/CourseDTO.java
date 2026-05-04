package com.wanted.only_one.course.dto;

public class CourseDTO {

    private long courseId;
    private String title;
    private long memberId;
    private String teacherName;
    private double avgRating;
    private int reviewCount;

    public CourseDTO() {}

    public CourseDTO(String title, long memberId) {
        this.title = title;
        this.memberId = memberId;
    }

    public CourseDTO(long courseId, String title, long memberId) {
        this.courseId = courseId;
        this.title = title;
        this.memberId = memberId;
    }

    public long getCourseId()      { return courseId; }
    public String getTitle()       { return title; }
    public long getMemberId()      { return memberId; }
    public String getTeacherName() { return teacherName; }
    public double getAvgRating()   { return avgRating; }
    public int getReviewCount()    { return reviewCount; }

    public void setCourseId(long courseId)         { this.courseId = courseId; }
    public void setTitle(String title)             { this.title = title; }
    public void setMemberId(long memberId)         { this.memberId = memberId; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public void setAvgRating(double avgRating)     { this.avgRating = avgRating; }
    public void setReviewCount(int reviewCount)    { this.reviewCount = reviewCount; }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "courseId=" + courseId +
                ", title='" + title + '\'' +
                ", memberId=" + memberId +
                ", teacherName='" + teacherName + '\'' +
                ", avgRating=" + avgRating +
                ", reviewCount=" + reviewCount +
                '}';
    }
}

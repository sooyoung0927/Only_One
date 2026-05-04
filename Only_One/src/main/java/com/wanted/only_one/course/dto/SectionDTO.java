package com.wanted.only_one.course.dto;

import java.util.List;

public class SectionDTO {

    private long courseId;
    private String courseTitle;
    private long memberId;
    private List<LectureDTO> lectures;

    public SectionDTO() {}

    public SectionDTO(long courseId, String courseTitle, long memberId, List<LectureDTO> lectures) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.memberId = memberId;
        this.lectures = lectures;
    }

    public long getCourseId()             { return courseId; }
    public String getCourseTitle()        { return courseTitle; }
    public long getMemberId()             { return memberId; }
    public List<LectureDTO> getLectures() { return lectures; }

    public void setCourseId(long courseId)             { this.courseId = courseId; }
    public void setCourseTitle(String courseTitle)     { this.courseTitle = courseTitle; }
    public void setMemberId(long memberId)             { this.memberId = memberId; }
    public void setLectures(List<LectureDTO> lectures) { this.lectures = lectures; }

    @Override
    public String toString() {
        return "SectionDTO{courseId=" + courseId + ", courseTitle='" + courseTitle + "', lectures=" + lectures + "}";
    }
}

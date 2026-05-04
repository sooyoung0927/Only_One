package com.wanted.only_one.course.controller;

import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.course.dto.LectureDTO;
import com.wanted.only_one.course.dto.SectionDTO;
import com.wanted.only_one.course.service.CourseService;
import com.wanted.only_one.course.service.LectureService;
import com.wanted.only_one.global.config.JDBCTemplate;
import com.wanted.only_one.member.dto.MemberDTO;
import com.wanted.only_one.study.controller.StudyController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CourseController {

    private CourseService   courseService   = new CourseService();
    private LectureService  lectureService  = new LectureService();
    private StudyController studyController;

    public CourseController(StudyController studyController) {
        this.studyController = studyController;
    }

    // ── 학생용 ───────────────────────────────────────

    public List<CourseDTO> showAllCourses() throws SQLException {
        return courseService.getAllCourses();
    }

    public List<CourseDTO> searchCourse(String keyword) throws SQLException {
        return courseService.searchCourse(keyword);
    }

    public List<CourseDTO> searchCourseWithRating(String keyword) throws SQLException {
        return courseService.searchCourseWithRating(keyword);
    }

    public SectionDTO findJoin(long courseId) throws SQLException {
        return courseService.findCourseWithSections(courseId);
    }

    public void completeLecture(long memberId, long lectureId, long courseId) {
        studyController.completeLecture(memberId, lectureId, courseId);
    }

    public void enrollCourse(long memberId, long courseId) {
        studyController.enrollCourse(memberId, courseId);
    }

    public void updateCourseStatus(long memberId, long courseId) {
        studyController.updateCourseStatus(memberId, courseId);
    }

    // study 팀 통해서 강의 상태 조회
    public Map<Long, String> getLectureStatusMap(long memberId, long courseId) {
        return studyController.getLectureStatusMap(memberId, courseId);
    }

    // study 팀 통해서 전체 완료 여부 확인
    public boolean isAllLecturesComplete(long memberId, long courseId) {
        return studyController.isAllLecturesComplete(memberId, courseId);
    }

    // 결제 여부 확인
    public boolean hasPaid(long memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM payments WHERE member_id = ?";
        Connection conn = JDBCTemplate.getConnection();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, memberId);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    // ── 강사용 ───────────────────────────────────────

    public List<CourseDTO> T_showAllCourses(long memberId) throws SQLException {
        return courseService.getT_AllCourses(memberId);
    }

    public List<SectionDTO> T_findAllWithSections(long memberId) throws SQLException {
        return courseService.findAllWithSections(memberId);
    }

    public long addCourse(String title, long memberId) throws SQLException {
        return courseService.addCourse(title, memberId);
    }

    public boolean addLecture(long courseId, String title) throws SQLException {
        return lectureService.addLecture(courseId, title);
    }

    public List<LectureDTO> getLectures(long courseId) throws SQLException {
        return lectureService.getLectures(courseId);
    }

    public boolean updateCourse(long courseId, String newTitle, long memberId) throws SQLException {
        return courseService.updateCourse(courseId, newTitle, memberId);
    }

    public boolean deleteCourse(long courseId) throws SQLException {
        return courseService.deleteCourse(courseId);
    }

    public List<MemberDTO> getEnrolledStudents(long courseId) throws SQLException {
        return courseService.getEnrolledStudents(courseId);
    }
}

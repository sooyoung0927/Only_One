package com.wanted.only_one.course.service;

import com.wanted.only_one.course.dao.CourseDAO;
import com.wanted.only_one.course.dao.CourseSectionDAO;
import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.course.dto.SectionDTO;
import com.wanted.only_one.global.config.JDBCTemplate;
import com.wanted.only_one.member.dto.MemberDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseService {

    private CourseDAO courseDAO               = new CourseDAO();
    private CourseSectionDAO courseSectionDAO = new CourseSectionDAO();

    // 학생 - 전체 강좌 조회
    public List<CourseDTO> getAllCourses() throws SQLException {
        return courseDAO.findAll();
    }

    // 강사 - 본인 강좌 조회
    public List<CourseDTO> getT_AllCourses(long memberId) throws SQLException {
        return courseDAO.findByMemberId(memberId);
    }

    // 강좌 + 강의 목록 JOIN 조회
    public SectionDTO findCourseWithSections(long courseId) throws SQLException {
        return courseSectionDAO.findCourseWithLectures(courseId);
    }

    // 강사 본인 강좌 전체 + 강의 JOIN 조회
    public List<SectionDTO> findAllWithSections(long memberId) throws SQLException {
        return courseSectionDAO.findAllWithLecturesByMemberId(memberId);
    }

    // 강좌 등록
    public long addCourse(String title, long memberId) throws SQLException {
        return courseDAO.insert(new CourseDTO(title, memberId));
    }

    // 강좌 수정
    public boolean updateCourse(long courseId, String newTitle, long memberId) throws SQLException {
        return courseDAO.update(new CourseDTO(courseId, newTitle, memberId));
    }

    // 강좌 삭제
    public boolean deleteCourse(long courseId) throws SQLException {
        return courseDAO.delete(courseId);
    }

    // 강좌 검색 (기본)
    public List<CourseDTO> searchCourse(String keyword) throws SQLException {
        return courseDAO.searchByTitle(keyword);
    }

    // 강좌 검색 + 별점 (별점 높은 순)
    public List<CourseDTO> searchCourseWithRating(String keyword) throws SQLException {
        return courseDAO.searchByTitleWithRating(keyword);
    }

    public List<MemberDTO> getEnrolledStudents(long courseId) throws SQLException {
        // 1. 에러 원인 해결: 테이블명을 영어(members, studying_courses)로 수정
        // 2. 에러 원인 해결: 존재하지 않는 enrolled_at 컬럼 제거
        String sql = "SELECT m.member_id, m.name, m.email, m.role, m.acc_count " +
                "FROM members m " +
                "JOIN studying_courses s ON m.member_id = s.member_id " +
                "WHERE s.course_id = ?";

        List<MemberDTO> students = new ArrayList<>();

        // try-with-resources로 자원 해제 확실히 처리
        try (Connection conn = JDBCTemplate.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // 3. 에러 원인 해결: 생성자 파라미터 개수와 타입을 MemberDTO 정의에 맞게 수정
                    // password는 조회하지 않으므로 null 처리, 가입일은 제거
                    MemberDTO student = new MemberDTO();
                    student.setMemberId(rs.getLong("member_id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setRole(rs.getString("role"));
                    student.setAccCount(rs.getInt("acc_count"));

                    students.add(student);
                }
            }
        }
        return students;
    }
}

package com.wanted.only_one.course.dao;

import com.wanted.only_one.global.config.JDBCTemplate;
import com.wanted.only_one.course.dto.LectureDTO;
import com.wanted.only_one.course.dto.SectionDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseSectionDAO {

    private Connection conn;

    public CourseSectionDAO() {
        try {
            this.conn = JDBCTemplate.getConnection();
        } catch (SQLException e) {
            System.out.println("[CourseSectionDAO] DB 연결 실패: " + e.getMessage());
        }
    }

    // course.findWithLectures - 강좌 단건 + 강의 목록 JOIN
    public SectionDTO findCourseWithLectures(long courseId) throws SQLException {
        String sql =
                "SELECT c.course_id, c.title AS course_title, c.member_id, " +
                        "       l.lecture_id, l.title AS lecture_title " +
                        "FROM courses c " +
                        "LEFT JOIN lectures l ON c.course_id = l.course_id " +
                        "WHERE c.course_id = ?";

        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, courseId);
            ResultSet rs = p.executeQuery();

            SectionDTO section = null;
            List<LectureDTO> lectures = new ArrayList<>();

            while (rs.next()) {
                if (section == null) {
                    section = new SectionDTO(
                            rs.getLong("course_id"),
                            rs.getString("course_title"),
                            rs.getLong("member_id"),
                            lectures
                    );
                }
                long lectureId = rs.getLong("lecture_id");
                if (lectureId != 0) {
                    lectures.add(new LectureDTO(
                            lectureId,
                            rs.getLong("course_id"),
                            rs.getString("lecture_title")
                    ));
                }
            }
            return section;
        }
    }

    // course.findAllWithLecturesByMemberId - 강사 본인 강좌 전체 + 강의 JOIN
    public List<SectionDTO> findAllWithLecturesByMemberId(long memberId) throws SQLException {
        String sql =
                "SELECT c.course_id, c.title AS course_title, c.member_id, " +
                        "       l.lecture_id, l.title AS lecture_title " +
                        "FROM courses c " +
                        "LEFT JOIN lectures l ON c.course_id = l.course_id " +
                        "WHERE c.member_id = ? " +
                        "ORDER BY c.course_id";

        List<SectionDTO> result = new ArrayList<>();
        Map<Long, SectionDTO> map = new LinkedHashMap<>();

        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, memberId);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                long courseId = rs.getLong("course_id");

                if (!map.containsKey(courseId)) {
                    List<LectureDTO> lectures = new ArrayList<>();
                    SectionDTO section = new SectionDTO(
                            courseId,
                            rs.getString("course_title"),
                            rs.getLong("member_id"),
                            lectures
                    );
                    map.put(courseId, section);
                }

                long lectureId = rs.getLong("lecture_id");
                if (lectureId != 0) {
                    map.get(courseId).getLectures().add(new LectureDTO(
                            lectureId,
                            courseId,
                            rs.getString("lecture_title")
                    ));
                }
            }
        }
        result.addAll(map.values());
        return result;
    }
}
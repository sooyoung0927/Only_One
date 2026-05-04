package com.wanted.only_one.course.dao;

import com.wanted.only_one.global.config.JDBCTemplate;
import com.wanted.only_one.course.dto.LectureDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LectureDAO {

    private Connection conn;

    public LectureDAO() {
        try {
            this.conn = JDBCTemplate.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("[CourseDAO] DB 연결 실패: " + e.getMessage(), e);
            // 연결 실패 즉시 터뜨려서 null 상태로 넘어가지 않게 함
        }
    }

    // lecture.findByCourseId
    public List<LectureDTO> findByCourseId(long courseId) throws SQLException {
        String sql = "SELECT lecture_id, course_id, title FROM lectures WHERE course_id = ?";
        List<LectureDTO> list = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, courseId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // lecture.findById
    public LectureDTO findById(long lectureId) throws SQLException {
        String sql = "SELECT lecture_id, course_id, title FROM lectures WHERE lecture_id = ?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, lectureId);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    // lecture.insert
    public boolean insert(LectureDTO lecture) throws SQLException {
        String sql = "INSERT INTO lectures (course_id, title) VALUES (?, ?)";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, lecture.getCourseId());
            p.setString(2, lecture.getTitle());
            return p.executeUpdate() > 0;
        }
    }

    // lecture.delete
    public boolean delete(long lectureId) throws SQLException {
        String sql = "DELETE FROM lectures WHERE lecture_id = ?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, lectureId);
            return p.executeUpdate() > 0;
        }
    }

    private LectureDTO mapRow(ResultSet rs) throws SQLException {
        return new LectureDTO(
                rs.getLong("lecture_id"),
                rs.getLong("course_id"),
                rs.getString("title")
        );
    }
}

package com.wanted.only_one.course.dao;

import com.wanted.only_one.global.config.JDBCTemplate;
import com.wanted.only_one.course.dto.CourseDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    private Connection conn;

    public CourseDAO( Connection conn) {
        this.conn = conn;
    }

    public CourseDAO() {
        try {
            this.conn = JDBCTemplate.getConnection();
        } catch (SQLException e) {
            System.out.println("[CourseDAO] DB 연결 실패: " + e.getMessage());
        }
    }

    // course.insert
    public long insert(CourseDTO course) throws SQLException {
        String sql = "INSERT INTO courses (title, member_id) VALUES (?, ?)";
        try (PreparedStatement p = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, course.getTitle());
            p.setLong(2, course.getMemberId());
            p.executeUpdate();
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);
        }
        return -1;
    }

    // course.findAll - 강사 이름 포함
    public List<CourseDTO> findAll() throws SQLException {
        String sql =
                "SELECT c.course_id, c.title, c.member_id, m.name AS teacher_name " +
                        "FROM courses c " +
                        "JOIN members m ON c.member_id = m.member_id";
        List<CourseDTO> list = new ArrayList<>();
        try (Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) list.add(mapRowWithTeacher(rs));
        }
        return list;
    }


    // course.findByMemberId
    public List<CourseDTO> findByMemberId(long memberId) throws SQLException {
        String sql = "SELECT c.course_id, c.title, c.member_id, m.name AS teacher_name " +
                "FROM courses c JOIN members m ON c.member_id = m.member_id " +
                "WHERE c.member_id = ?";
        List<CourseDTO> list = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, memberId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) list.add(mapRowWithTeacher(rs));
        }
        return list;
    }

    // course.searchByTitle - 강사 이름 포함
    public List<CourseDTO> searchByTitle(String keyword) throws SQLException {
        String sql =
                "SELECT c.course_id, c.title, c.member_id, m.name AS teacher_name " +
                        "FROM courses c " +
                        "JOIN members m ON c.member_id = m.member_id " +
                        "WHERE c.title LIKE ?";
        List<CourseDTO> list = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, "%" + keyword + "%");
            ResultSet rs = p.executeQuery();
            while (rs.next()) list.add(mapRowWithTeacher(rs));
        }
        return list;
    }

    // course.searchByTitleWithRating - 별점 + 강사 이름 포함
    public List<CourseDTO> searchByTitleWithRating(String keyword) throws SQLException {
        String sql =
                "SELECT c.course_id, c.title, c.member_id, m.name AS teacher_name, " +
                        "ROUND(AVG(r.rating), 1) AS avg_rating, COUNT(r.review_id) AS review_count " +
                        "FROM courses c " +
                        "JOIN members m ON c.member_id = m.member_id " +
                        "LEFT JOIN course_reviews r ON c.course_id = r.course_id " +
                        "WHERE c.title LIKE ? " +
                        "GROUP BY c.course_id, c.title, c.member_id, m.name " +
                        "ORDER BY avg_rating DESC";
        List<CourseDTO> list = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, "%" + keyword + "%");
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                CourseDTO dto = mapRowWithTeacher(rs);
                dto.setAvgRating(rs.getDouble("avg_rating"));
                dto.setReviewCount(rs.getInt("review_count"));
                list.add(dto);
            }
        }
        return list;
    }

    // course.update
    public boolean update(CourseDTO course) throws SQLException {
        String sql = "UPDATE courses SET title = ? WHERE course_id = ?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, course.getTitle());
            p.setLong(2, course.getCourseId());
            return p.executeUpdate() > 0;
        }
    }

    // course.delete
    public boolean delete(long courseId) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setLong(1, courseId);
            return p.executeUpdate() > 0;
        }
    }

    private CourseDTO mapRowWithTeacher(ResultSet rs) throws SQLException {
        CourseDTO dto = new CourseDTO(
                rs.getLong("course_id"),
                rs.getString("title"),
                rs.getLong("member_id")
        );
        dto.setTeacherName(rs.getString("teacher_name"));
        return dto;
    }
}

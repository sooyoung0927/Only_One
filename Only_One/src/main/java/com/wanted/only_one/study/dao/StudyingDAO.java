package com.wanted.only_one.study.dao;

import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.global.utils.QueryUtil;
import com.wanted.only_one.study.dto.StudyingDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudyingDAO {


    private final Connection connection;

    public StudyingDAO(Connection connection) {
        this.connection = connection;
    }


    public List<CourseDTO> showcompletedCourseList(long memberId) throws SQLException {
        String query = QueryUtil.getQuery("showcompletedCourseList");
        List<CourseDTO> completedCourseList = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, memberId);
            ResultSet rset = pstmt.executeQuery();

            while(rset.next()){
                CourseDTO completedCourse = new CourseDTO();
                completedCourse.setTitle(rset.getString("title"));
                completedCourseList.add(completedCourse);
            }
        }
        return completedCourseList;
    }


    public List<CourseDTO> showMyStudyingList(long memberId,int menu) throws SQLException {
        String query = QueryUtil.getQuery("showMyStudyingList");
        List<CourseDTO> list = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setInt(2, menu);
            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                CourseDTO course = new CourseDTO();
                course.setTitle(rset.getString("title"));
                course.setTeacherName(rset.getString("teacher_name")); // 추가
                list.add(course);
            }
        }
        return list;
    }

    public void updateCourseStatus(long memberId, long courseId) throws SQLException {
        String query = QueryUtil.getQuery("updateCourseStatus");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, courseId);
            pstmt.executeUpdate();
        }
    }

    public boolean insertStudyingCourse(long memberId, long courseId) throws SQLException {
        String query = QueryUtil.getQuery("insertStudyingCourse");
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, courseId);
            pstmt.setLong(2, memberId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean existsStudyingCourse(long memberId, long courseId) throws SQLException {
        String query = QueryUtil.getQuery("existsStudyingCourse");
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }
}

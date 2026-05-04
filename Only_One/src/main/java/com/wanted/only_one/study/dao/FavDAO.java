package com.wanted.only_one.study.dao;

import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.global.utils.QueryUtil;
import com.wanted.only_one.study.dto.FavDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavDAO {

    private final Connection connection;

    public FavDAO(Connection connection) {
        this.connection = connection;
    }


// 전체 선택
    public List<FavDTO> showFavList(long memberId) throws SQLException{
        // 동작시킬 쿼리문 준비
        String query = QueryUtil.getQuery("showFavList");
        List<FavDTO> favList = new ArrayList<>();

        // 쿼리문 동작
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            ResultSet rset = pstmt.executeQuery();

            while(rset.next()){
                FavDTO course = new FavDTO(
                        rset.getString("course_title"),
                        rset.getString("member_name")
                );
                course.setCourse_id(rset.getLong("course_id")); // 추가
                favList.add(course);
            }
        }
        return favList;
    }

    public Long addFav(long memberId,long courseId) throws SQLException {
        String query = QueryUtil.getQuery("addFavList");

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1,memberId);
            pstmt.setLong(2, courseId);

            int affectedRows = pstmt.executeUpdate();

            if(affectedRows>0){
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()){
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    public Boolean deleteFav(long memberId, long courseId) throws SQLException {
        String query = QueryUtil.getQuery("deleteFavList");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, courseId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0 ? true : null;
        }
    }

    public boolean checkAlreadyFav(long memberId, long courseId) throws SQLException {
        String query = QueryUtil.getQuery("checkAlreadyFav");
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public boolean checkAlreadyStudying(long memberId, long courseId) throws SQLException {
        String query = QueryUtil.getQuery("checkAlreadyStudying");
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public List<CourseDTO> searchCourseByTitle(String keyword) throws SQLException {
        String query = QueryUtil.getQuery("searchCourseByTitle");
        List<CourseDTO> result = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                CourseDTO dto = new CourseDTO(
                        rs.getLong("course_id"),
                        rs.getString("title"),
                        rs.getLong("member_id")
                );
                dto.setTeacherName(rs.getString("teacher_name"));
                result.add(dto);
            }
        }
        return result;
    }
}

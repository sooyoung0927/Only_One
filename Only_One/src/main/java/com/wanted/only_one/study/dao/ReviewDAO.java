package com.wanted.only_one.study.dao;

import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.global.utils.QueryUtil;
import com.wanted.only_one.study.dto.ReviewDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {


    private final Connection connection;

    public ReviewDAO(Connection connection) {
        this.connection = connection;
    }

    public Long WriteReview(long memberId,String description, String content, Double rating) throws SQLException{

        String query = QueryUtil.getQuery("writeReview");

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1,memberId);
            pstmt.setString(2, content);
            pstmt.setDouble(3, rating);
            pstmt.setString(4, description);

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

    public List<ReviewDTO> showMyReviewList(long memberId) throws SQLException {
        // 동작시킬 쿼리문 준비
        String query = QueryUtil.getQuery("showMyReviewList");
        List<ReviewDTO> MyReviewList = new ArrayList<>();

        // 쿼리문 동작
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            ResultSet rset = pstmt.executeQuery();

            while(rset.next()){
                ReviewDTO MyReview = new ReviewDTO(
                        rset.getString("title"),
                        rset.getString("contents"),
                        rset.getDouble("rating")
                );
                MyReviewList.add(MyReview);
            }
        }
        return MyReviewList;
    }

    public List<ReviewDTO> ShowReviewInCourse(String description) throws SQLException {
        // 동작시킬 쿼리문 준비
        String query = QueryUtil.getQuery("ShowReviewInCourse");
        List<ReviewDTO> ReviewInCourse = new ArrayList<>();

        // 쿼리문 동작
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, description);
            ResultSet rset = pstmt.executeQuery();

            while(rset.next()){
                ReviewDTO review = new ReviewDTO(
                        rset.getString("title"),
                        rset.getString("contents"),
                        rset.getDouble("rating"),
                        rset.getString("teacher_name"),
                        rset.getString("review_writer")
                );
                ReviewInCourse.add(review);
            }
        }
        return ReviewInCourse;
    }

    public List<ReviewDTO> ShowReviewForTeacher(long courseId) throws SQLException {

        String query = QueryUtil.getQuery("ShowReviewForTeacher");
        List<ReviewDTO> ReviewForTeacher = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, courseId);
            ResultSet rset = pstmt.executeQuery();

            while(rset.next()){
                ReviewDTO reviewforteacher = new ReviewDTO(
                        rset.getString("title"),
                        rset.getString("contents"),
                        rset.getDouble("rating"),
                        rset.getString("review_writer")
                );
                ReviewForTeacher.add(reviewforteacher);
            }
        }
        return ReviewForTeacher;
    }

    public boolean checkExistingReview(long memberId,String description) throws SQLException {
        String query = QueryUtil.getQuery("existsReview");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setString(2, description);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // 1 이상이면 이미 작성한 강좌평 존재
            }
        }
        return false;
    }

    public boolean checkCourseExists(String description) throws SQLException {
        String query = QueryUtil.getQuery("checkCourseExists");
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, description);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}



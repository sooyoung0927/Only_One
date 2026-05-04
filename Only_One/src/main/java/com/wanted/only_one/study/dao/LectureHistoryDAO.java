package com.wanted.only_one.study.dao;

import com.wanted.only_one.global.utils.QueryUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LectureHistoryDAO {
    private final Connection connection;

    public LectureHistoryDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean AllLecturesCompleted(long memberId, long courseId) throws SQLException {
        String query = QueryUtil.getQuery("AllLecturesCompleted");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, courseId);
            pstmt.setLong(2, memberId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0; // 미수강 강의가 0개면 true
            }
        }
        return false;
    }

    public boolean insertLectureHistory(long memberId, long lectureId) throws SQLException {
        String query = QueryUtil.getQuery("insertLectureHistory");
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, lectureId);
            return pstmt.executeUpdate() > 0;
        }
    }
}

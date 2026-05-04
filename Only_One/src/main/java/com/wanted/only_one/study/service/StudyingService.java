package com.wanted.only_one.study.service;

import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.study.dao.LectureHistoryDAO;
import com.wanted.only_one.study.dao.StudyingDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyingService {

    private final Connection connection;
    private final StudyingDAO studyingDAO;
    private final LectureHistoryDAO lectureHistoryDAO;

    public StudyingService(Connection connection) {
        this.studyingDAO = new StudyingDAO(connection);
        this.lectureHistoryDAO = new LectureHistoryDAO(connection);
        this.connection = connection;
    }

    public List<CourseDTO> showMyStudyingList(long memberId, int menu) {
        try {
            return studyingDAO.showMyStudyingList(memberId, menu);
        } catch (SQLException e) {
            throw new RuntimeException("수강 강좌 조회 중 에러 발생 🚨");
        }
    }

    public void updateCourseStatus(long memberId, long courseId) {
        try {
            boolean allCompleted = lectureHistoryDAO.AllLecturesCompleted(memberId, courseId);
            if (allCompleted) {
                studyingDAO.updateCourseStatus(memberId, courseId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("강좌 상태 업데이트 중 에러 발생 🚨");
        }
    }

    public void completeLecture(long memberId, long lectureId, long courseId) {
        try {
            lectureHistoryDAO.insertLectureHistory(memberId, lectureId);
            updateCourseStatus(memberId, courseId);
        } catch (SQLException e) {
            throw new RuntimeException("강의 수강 처리 중 에러 발생 🚨");
        }
    }

    public void enrollCourse(long memberId, long courseId) {
        try {
            if (!studyingDAO.existsStudyingCourse(memberId, courseId)) {
                studyingDAO.insertStudyingCourse(memberId, courseId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("수강 신청 중 에러 발생 🚨");
        }
    }

    // 강의별 수강 상태 Map<lectureId, status> 반환
    public Map<Long, String> getLectureStatusMap(long memberId, long courseId) {
        String sql =
                "SELECT lh.lecture_id, lh.status " +
                        "FROM lecture_histories lh " +
                        "JOIN lectures l ON lh.lecture_id = l.lecture_id " +
                        "WHERE lh.member_id = ? AND l.course_id = ?";

        Map<Long, String> map = new HashMap<>();
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setLong(1, memberId);
            p.setLong(2, courseId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                map.put(rs.getLong("lecture_id"), rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("강의 상태 조회 중 에러 발생 🚨");
        }
        return map;
    }

    // 강좌 전체 강의 수 vs 완료된 강의 수 비교
    public boolean isAllLecturesComplete(long memberId, long courseId) {
        String totalSql = "SELECT COUNT(*) FROM lectures WHERE course_id = ?";
        String completedSql =
                "SELECT COUNT(*) FROM lecture_histories lh " +
                        "JOIN lectures l ON lh.lecture_id = l.lecture_id " +
                        "WHERE lh.member_id = ? AND l.course_id = ? " +
                        "AND (lh.status = 'COMPLETED' OR lh.status = '수강완료')";

        try {
            int total = 0;
            try (PreparedStatement p = connection.prepareStatement(totalSql)) {
                p.setLong(1, courseId);
                ResultSet rs = p.executeQuery();
                if (rs.next()) total = rs.getInt(1);
            }
            if (total == 0) return false;

            int completed = 0;
            try (PreparedStatement p = connection.prepareStatement(completedSql)) {
                p.setLong(1, memberId);
                p.setLong(2, courseId);
                ResultSet rs = p.executeQuery();
                if (rs.next()) completed = rs.getInt(1);
            }
            return total == completed;

        } catch (SQLException e) {
            throw new RuntimeException("강의 완료 여부 확인 중 에러 발생 🚨");
        }
    }
}

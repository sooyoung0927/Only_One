package com.wanted.only_one.study.service;

import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.study.dao.ReviewDAO;
import com.wanted.only_one.study.dao.StudyingDAO;
import com.wanted.only_one.study.dto.ReviewDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReviewService {

    // 강좌평은 수강 완료한 강좌에 대해서만 작성 가능
    // StudyingDAO는 수강 완료한 강의목록을 가져오기 위해 선언
    private final StudyingDAO studyingDAO;
    private final ReviewDAO reviewDAO;
    private final Connection connection;


    public ReviewService(Connection connection) {
        this.studyingDAO = new StudyingDAO(connection);
        this.reviewDAO = new ReviewDAO(connection);
        this.connection = connection;
    }

// 수강 완료한 강좌 조회
    public List<CourseDTO> showcompletedCourseList(long memberId) {
        try {
            return studyingDAO.showcompletedCourseList(memberId);
        } catch (SQLException e) {
            throw new RuntimeException("수강 완료 강좌 조회 중 에러 발생 🚨");
        }
    }

    public Boolean WriteReview(long memberId, String description, String content, Double rating) {
        try {
            connection.setAutoCommit(false);

            // 이미 작성한 강좌평이 있는지 확인
            boolean alreadyExists = reviewDAO.checkExistingReview(memberId,description);
            if (alreadyExists) {
                connection.rollback();
                return null; // = 이미 작성한 강좌평 있다
            }

            Long writereview = reviewDAO.WriteReview(memberId, description,content,rating);

            if(writereview == null){
                throw new SQLException("🚨해당 강좌를 찾을 수 없습니다 ");
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();

            } catch (SQLException ex) {
                throw new RuntimeException("롤백 중 에러 발생");
            }
            return false;
        }finally{
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<ReviewDTO> showMyReviewList(long memberId) {
        try {
            return reviewDAO.showMyReviewList(memberId);
        } catch (SQLException e) {
            throw new RuntimeException("강좌평 조회 중 에러 발생 🚨");
        }
    }

    public List<ReviewDTO> ShowReviewInCourse(String description) {
        try {
            return reviewDAO.ShowReviewInCourse(description);
        } catch (SQLException e) {
            throw new RuntimeException("강좌평 조회 중 에러 발생 🚨");
        }
    }

    public List<ReviewDTO> ShowReviewForTeacher(long courseId) {
        try {
            return reviewDAO.ShowReviewForTeacher(courseId);
        } catch (SQLException e) {
            throw new RuntimeException("강좌평 조회 중 에러 발생 🚨");
        }
    }

    public boolean checkCourseExists(String description) {
        try {
            return reviewDAO.checkCourseExists(description);
        } catch (SQLException e) {
            throw new RuntimeException("강좌 조회 중 에러 발생 🚨");
        }
    }
}

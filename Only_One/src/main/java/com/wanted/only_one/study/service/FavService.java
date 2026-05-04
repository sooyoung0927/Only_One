package com.wanted.only_one.study.service;

import com.wanted.only_one.course.dao.CourseDAO;
import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.study.dao.FavDAO;
import com.wanted.only_one.study.dto.FavDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FavService {

    private final FavDAO favDAO;
    private final Connection connection;
    private final CourseDAO courseDAO ;


    public FavService(Connection connection) {
        this.favDAO = new FavDAO(connection);
        this.connection = connection;
        this.courseDAO = new CourseDAO(connection);
    }

    // 선택 목록에 넣을 강좌 선택을 위한 전체 강좌 목록 조회
    public List<CourseDTO> showCourseList() {
        System.out.println("로그 찍는 용 : 전체 강좌 출력");
        try {
            return courseDAO.findAll(); // 종준님 강좌 조회 메서드 연결
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public int addFavList(long memberId, long courseId) {
        try {
            connection.setAutoCommit(false);

            if (favDAO.checkAlreadyFav(memberId, courseId)) {
                connection.rollback();
                return 2;
            }

            if (favDAO.checkAlreadyStudying(memberId, courseId)) {
                connection.rollback();
                return 3;
            }

            Long addedCourseId = favDAO.addFav(memberId, courseId);

            if (addedCourseId == null) {
                connection.rollback();
                return 0;
            }

            connection.commit();
            return 1;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("롤백 중 에러 발생");
            }
            return 0;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public List<FavDTO> showFavList(long memberId) {
        try {
            return favDAO.showFavList(memberId);
        } catch (SQLException e) {
            throw new RuntimeException("선택 목록 조회 중 에러 발생 🚨");
        }
    }

    public Boolean deleteFavList(long memberId, long courseId) {
        try {
            connection.setAutoCommit(false);

            Boolean result = favDAO.deleteFav(memberId, courseId);

            if (result == null) {
                connection.rollback();
                return null;
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
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<CourseDTO> searchCourseByTitle(String keyword) {
        try {
            return favDAO.searchCourseByTitle(keyword);
        } catch (SQLException e) {
            throw new RuntimeException("강좌 검색 중 에러 발생");
        }
    }
}
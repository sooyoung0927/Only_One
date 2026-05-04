package com.wanted.only_one.course.service;

import com.wanted.only_one.course.dao.LectureDAO;
import com.wanted.only_one.course.dto.LectureDTO;

import java.sql.SQLException;
import java.util.List;

public class LectureService {

    private LectureDAO lectureDAO = new LectureDAO();

    // 강좌별 강의 목록 조회
    public List<LectureDTO> getLectures(long courseId) throws SQLException {
        return lectureDAO.findByCourseId(courseId);
    }

    // 강의 등록
    public boolean addLecture(long courseId, String title) throws SQLException {
        return lectureDAO.insert(new LectureDTO(courseId, title));
    }

    // 강의 삭제
    public boolean deleteLecture(long lectureId) throws SQLException {
        return lectureDAO.delete(lectureId);
    }
}

package com.wanted.only_one.study.controller;

import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.study.dto.FavDTO;
import com.wanted.only_one.study.dto.ReviewDTO;
import com.wanted.only_one.study.service.FavService;
import com.wanted.only_one.study.service.ReviewService;
import com.wanted.only_one.study.service.StudyingService;

import java.util.List;
import java.util.Map;

public class StudyController {

    private final FavService favService;
    private final ReviewService reviewService;
    private final StudyingService studyingService;

    public StudyController(FavService favService, ReviewService reviewService, StudyingService studyingService) {
        this.favService = favService;
        this.reviewService = reviewService;
        this.studyingService = studyingService;
    }

    public List<CourseDTO> showCourseList() {
        return favService.showCourseList();
    }

    public int addFavList(long memberId, long courseId) {
        return favService.addFavList(memberId, courseId);
    }

    public List<FavDTO> showFavList(long memberId) {
        return favService.showFavList(memberId);
    }

    public List<CourseDTO> showcompletedCourseList(long memberId) {
        return reviewService.showcompletedCourseList(memberId);
    }

    public Boolean WriteReview(long memberId, String description, String content, Double rating) {
        return reviewService.WriteReview(memberId, description, content, rating);
    }

    public List<CourseDTO> showMyStudyingList(long memberId, int menu) {
        return studyingService.showMyStudyingList(memberId, menu);
    }

    public void updateCourseStatus(long memberId, long courseId) {
        studyingService.updateCourseStatus(memberId, courseId);
    }

    public void completeLecture(long memberId, long lectureId, long courseId) {
        studyingService.completeLecture(memberId, lectureId, courseId);
    }

    public void enrollCourse(long memberId, long courseId) {
        studyingService.enrollCourse(memberId, courseId);
    }

    // 강의별 수강 상태 Map<lectureId, status> 반환 → CourseController에서 사용
    public Map<Long, String> getLectureStatusMap(long memberId, long courseId) {
        return studyingService.getLectureStatusMap(memberId, courseId);
    }

    // 강좌 내 모든 강의 완료 여부 → CourseController에서 사용
    public boolean isAllLecturesComplete(long memberId, long courseId) {
        return studyingService.isAllLecturesComplete(memberId, courseId);
    }

    public List<ReviewDTO> showMyReviewList(long memberId) {
        return reviewService.showMyReviewList(memberId);
    }

    public List<ReviewDTO> ShowReviewInCourse(String description) {
        return reviewService.ShowReviewInCourse(description);
    }

    public List<ReviewDTO> ShowReviewForTeacher(long courseId) {
        return reviewService.ShowReviewForTeacher(courseId);
    }

    public boolean checkCourseExists(String description) {
        return reviewService.checkCourseExists(description);
    }

    public Boolean deleteFavList(long memberId, long courseId) {
        return favService.deleteFavList(memberId, courseId);
    }

    public List<CourseDTO> searchCourseByTitle(String keyword) {
        return favService.searchCourseByTitle(keyword);
    }
}

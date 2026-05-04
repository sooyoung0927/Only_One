package com.wanted.only_one.course.view;

import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.course.dto.LectureDTO;
import com.wanted.only_one.course.dto.SectionDTO;

import java.util.List;

public class CourseOutputView {

    // 강좌 목록 출력 (강사 이름 포함)
    public void printCourses(List<CourseDTO> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("등록된 강좌가 없습니다.");
            return;
        }

        System.out.println(" ");
        System.out.println("==========전체 강좌 리스트==========");
        for (int i = 0; i < list.size(); i++) {
            CourseDTO c = list.get(i);
            System.out.println((i + 1) + ". 강좌명 : " + c.getTitle());
            if (c.getTeacherName() != null)
                System.out.println("   강사명 : " + c.getTeacherName());
            System.out.println("---------------------------------");
        }
    }

    // 강좌 목록 + 별점 + 강사 이름 출력
    public void printCoursesWithRating(List<CourseDTO> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        System.out.println(" ");
        System.out.println("=================================");
        for (int i = 0; i < list.size(); i++) {
            CourseDTO c = list.get(i);
            System.out.println((i + 1) + ". 강좌명 : " + c.getTitle());
            if (c.getTeacherName() != null)
                System.out.println("   강사명 : " + c.getTeacherName());
            System.out.println("   별점   : " + (c.getAvgRating() == 0 ? "없음" : c.getAvgRating() + "점"));
            System.out.println("   리뷰수 : " + c.getReviewCount() + "개");
            System.out.println("---------------------------------");
        }
    }

    // 강좌 + 강의 목록 출력
    public void printCourseDetail(SectionDTO section) {
        if (section == null) {
            System.out.println("강좌 정보를 찾을 수 없습니다.");
            return;
        }
        System.out.println("=================================");
        System.out.println("강좌명 : " + section.getCourseTitle());
        System.out.println("---------------------------------");
        System.out.println("[강의 목록]");
        if (section.getLectures() == null || section.getLectures().isEmpty()) {
            System.out.println("  등록된 강의가 없습니다.");
        } else {
            for (int i = 0; i < section.getLectures().size(); i++)
                System.out.println("  " + (i + 1) + ". 강의명 : " + section.getLectures().get(i).getTitle());
        }
        System.out.println("=================================");
    }

    // 강사 강좌 전체 출력
    public void printAllCourseDetails(List<SectionDTO> list) {
        if (list == null || list.isEmpty()) { System.out.println("등록한 강좌가 없습니다."); return; }
        for (SectionDTO section : list) printCourseDetail(section);
    }

    // 강의 목록 출력
    public void printLectures(List<LectureDTO> list) {
        if (list == null || list.isEmpty()) { System.out.println("강의가 없습니다."); return; }
        System.out.println("=================================");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". 강의명 : " + list.get(i).getTitle());
            System.out.println("---------------------------------");
        }
    }

    public void printMessage(String msg) { System.out.println(msg); }
    public void printError(String msg)   { System.out.println("[오류] 올바른 번호를 입력해주세요. " + msg); }
    public void printSuccess(String msg) { System.out.println("[성공] " + msg); }
    public void printFail(String msg)    { System.out.println("[실패] " + msg); }
}

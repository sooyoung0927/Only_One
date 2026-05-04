package com.wanted.only_one;

import com.wanted.only_one.course.controller.CourseController;
import com.wanted.only_one.course.view.CourseInputView;
import com.wanted.only_one.course.view.CourseOutputView;
import com.wanted.only_one.global.config.JDBCTemplate;
import com.wanted.only_one.member.controller.AuthController;
import com.wanted.only_one.member.service.AuthService;
import com.wanted.only_one.member.view.MemberInputView;
import com.wanted.only_one.member.view.MemberOutputView;
import com.wanted.only_one.payments.PaymentController;
import com.wanted.only_one.payments.PaymentInputView;
import com.wanted.only_one.payments.PaymentOutputView;
import com.wanted.only_one.payments.PaymentService;
import com.wanted.only_one.study.controller.StudyController;
import com.wanted.only_one.study.service.FavService;
import com.wanted.only_one.study.service.ReviewService;
import com.wanted.only_one.study.service.StudyingService;
import com.wanted.only_one.study.view.StudyInputView;
import com.wanted.only_one.study.view.StudyOutputView;

import java.sql.Connection;

public class Application {
    public static void main(String[] args) {
        try (Connection con = JDBCTemplate.getConnection()) {
            System.out.println("✅ 시스템 연결 성공");

            // 1. 서비스 & 컨트롤러 & 뷰 조립 (Study)
            StudyController studyCtrl = new StudyController(new FavService(con), new ReviewService(con), new StudyingService(con));
            StudyInputView studyView = new StudyInputView(new StudyOutputView(), studyCtrl);

            // 2. 서비스 & 컨트롤러 & 뷰 조립 (Course)
            CourseInputView courseView = new CourseInputView(new CourseController(studyCtrl),
                    new CourseOutputView(), studyView,
                    new AuthController(new AuthService(con)), new MemberOutputView());

            // 3. 결제 조립
            PaymentInputView payView = new PaymentInputView(new PaymentController(new PaymentService(con)), new PaymentOutputView());

            // 4. 메인 메뉴 조립 (모든 뷰 주입)
            MemberInputView mainView = new MemberInputView(
                    new AuthController(new AuthService(con)),
                    new MemberOutputView(),
                    payView,
                    courseView,
                    studyView
            );

            mainView.displayMainMenu();

        } catch (Exception e) {
            System.err.println("🚨 실행 오류: " + e.getMessage());
        } finally {
            JDBCTemplate.close();
        }
    }
}
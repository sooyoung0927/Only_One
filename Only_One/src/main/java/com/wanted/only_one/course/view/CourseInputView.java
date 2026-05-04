package com.wanted.only_one.course.view;

import com.wanted.only_one.course.controller.CourseController;
import com.wanted.only_one.course.dto.CourseDTO;
import com.wanted.only_one.course.dto.LectureDTO;
import com.wanted.only_one.course.dto.SectionDTO;
import com.wanted.only_one.member.controller.AuthController;
import com.wanted.only_one.member.dto.MemberDTO;
import com.wanted.only_one.member.view.MemberOutputView;
import com.wanted.only_one.study.view.StudyInputView;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CourseInputView {

    private final CourseController controller;
    private final CourseOutputView outputView;
    private final StudyInputView studyInputView;
    private final Scanner sc = new Scanner(System.in);
    private final AuthController authController;
    private final MemberOutputView memberOutputView;

    public CourseInputView(CourseController controller, CourseOutputView outputView,
                           StudyInputView studyInputView, AuthController authController,
                           MemberOutputView memberOutputView) {
        this.controller       = controller;
        this.outputView       = outputView;
        this.studyInputView   = studyInputView;
        this.authController   = authController;
        this.memberOutputView = memberOutputView;
    }

    // ── 학생용 ───────────────────────────────────────

    public void showAllCourses() throws SQLException {
        outputView.printMessage("\n--- 강좌 목록 전체 조회 ---");
        outputView.printCourses(controller.showAllCourses());
    }

    public void searchCourse() throws SQLException {
        System.out.println("\n=================================");
        System.out.println("           강좌 검색");
        System.out.println("=================================");
        System.out.print("검색할 강좌 키워드 입력 : ");
        String keyword = sc.nextLine().trim();
        if (keyword.isEmpty()) { outputView.printMessage("검색어를 입력해주세요."); return; }

        List<CourseDTO> list = controller.searchCourseWithRating(keyword);
        if (list == null || list.isEmpty()) {
            outputView.printMessage("\n[안내] '" + keyword + "' 검색 결과가 없습니다.");
        } else {
            outputView.printMessage("\n--- '" + keyword + "' 검색 결과 ---");
            outputView.printCoursesWithRating(list);
        }
    }

    public void courseSelection(long memberId) throws SQLException {
        while (true) {
            List<CourseDTO> courseList = controller.showAllCourses();
            if (courseList == null || courseList.isEmpty()) {
                outputView.printMessage("\n[안내] 현재 수강 가능한 강좌가 없습니다.");
                return;
            }
            outputView.printCourses(courseList);

            System.out.println("=================================");
            System.out.println("          강좌 수강하기");
            System.out.println("=================================");
            System.out.println("1. 수강할 강좌 선택");
            System.out.println("2. 강좌평 조회");
            System.out.println("3. 이전으로");
            System.out.print("번호를 입력해주세요 : ");

            int menu = inputInt();
            switch (menu) {
                case 1:
                    try {
                        if (!controller.hasPaid(memberId)) {
                            outputView.printMessage("=================================");
                            outputView.printMessage("  결제 후 수강 신청이 가능합니다.");
                            outputView.printMessage("  결제 메뉴에서 먼저 결제해주세요.");
                            outputView.printMessage("=================================");
                            break;
                        }
                    } catch (SQLException e) {
                        outputView.printFail("결제 정보를 확인할 수 없습니다.");
                        break;
                    }
                    long courseId = selectCourseByNumber(courseList);
                    if (courseId > 0) studyLecture(memberId, courseId);
                    break;
                case 2:
                    studyInputView.ShowReviewInCourse();
                    break;
                case 3:
                    return;
                default:
                    outputView.printError("");
            }
        }
    }

    private long selectCourseByNumber(List<CourseDTO> courseList) throws SQLException {
        outputView.printCourses(courseList);
        System.out.print("\n수강할 강좌 번호를 입력해주세요 (0: 뒤로가기) : ");
        int num = inputInt();
        if (num == 0) return -1;
        if (num < 1 || num > courseList.size()) {
            outputView.printMessage("올바른 번호를 입력해주세요.");
            return -1;
        }

        long courseId = courseList.get(num - 1).getCourseId();
        SectionDTO detail = controller.findJoin(courseId);
        if (detail == null) { outputView.printMessage("강좌 정보를 찾을 수 없습니다."); return -1; }
        outputView.printCourseDetail(detail);

        System.out.println("=================================");
        System.out.println("1. 수강하기");
        System.out.println("2. 뒤로가기");
        System.out.println("=================================");
        System.out.print("번호를 입력해주세요 : ");
        int choice = inputInt();
        return (choice == 1) ? courseId : -1;
    }

    // ── 강의 수강 루프 ────────────────────────────────
    // 탈출 조건 1 : 강의 번호 선택 후 3번 누르면 수강완료 메시지 출력 후 강좌 선택 화면으로
    // 탈출 조건 2 : 0 입력 → 강좌 선택 화면으로 그냥 나가기
    private void studyLecture(long memberId, long courseId) throws SQLException {
        controller.enrollCourse(memberId, courseId);

        while (true) {
            SectionDTO detail = controller.findJoin(courseId);
            if (detail == null) { outputView.printMessage("강좌 정보를 찾을 수 없습니다."); return; }

            List<LectureDTO> lectures = detail.getLectures();
            if (lectures == null || lectures.isEmpty()) {
                outputView.printMessage("등록된 강의가 없습니다.");
                return;
            }

            Map<Long, String> statusMap = controller.getLectureStatusMap(memberId, courseId);

            // 강의 목록 + 상태 출력
            System.out.println("=================================");
            System.out.println("강좌명 : " + detail.getCourseTitle());
            System.out.println("---------------------------------");
            System.out.println("[강의 목록]");
            for (int i = 0; i < lectures.size(); i++) {
                LectureDTO lec = lectures.get(i);
                String status = statusMap.getOrDefault(lec.getLectureId(), "미수강");
                System.out.println("  " + (i + 1) + ". " + lec.getTitle() + "  [" + status + "]");
            }
            System.out.println("=================================");

            // 모든 강의 완료 체크 → 탈출
            boolean allDone = true;
            for (LectureDTO lec : lectures) {
                String s = statusMap.getOrDefault(lec.getLectureId(), "미수강");
                if (!"COMPLETED".equals(s) && !"수강완료".equals(s)) {
                    allDone = false;
                    break;
                }
            }
            if (allDone && !statusMap.isEmpty()) {
                outputView.printSuccess("수강완료 되셨습니다.");
                controller.updateCourseStatus(memberId, courseId);
                return;
            }

            System.out.println("1. 수강하기");
            System.out.println("2. 뒤로가기");
            System.out.println("=================================");
            System.out.print("번호를 입력해주세요 : ");

            int menu = inputInt();
            if (menu == 2) return;
            if (menu != 1) { outputView.printError(""); continue; }

            System.out.print("강의 번호를 입력해주세요 : ");
            int num = inputInt();
            if (num < 1 || num > lectures.size()) {
                outputView.printMessage("올바른 번호를 입력해주세요. (1 ~ " + lectures.size() + ")");
                continue;
            }

            long lectureId = lectures.get(num - 1).getLectureId();
            String currentStatus = statusMap.getOrDefault(lectureId, "미수강");

            if ("COMPLETED".equals(currentStatus) || "수강완료".equals(currentStatus)) {
                outputView.printMessage("이미 수강 완료한 강의입니다.");
                continue;
            }

            controller.completeLecture(memberId, lectureId, courseId);
            outputView.printSuccess("강의 수강 완료!");
            // while 처음으로 → 최신 상태 다시 출력 → 전부 완료면 탈출
        }
    }


    // ── 강사용 ───────────────────────────────────────

    public boolean teacherCourseMenu(long memberId, String email, MemberDTO member) throws SQLException {
        while (true) {
            List<CourseDTO> courseList = controller.T_showAllCourses(memberId);

            System.out.println("=================================");
            System.out.println("             메인메뉴");
            System.out.println("=================================");
            System.out.println("1. 나의 강좌 전체 조회");
            System.out.println("2. 강좌/강의 등록");
            System.out.println("3. 강좌 수정");
            System.out.println("4. 강좌 삭제");
            System.out.println("5. 마이페이지");
            System.out.println("6. 로그아웃");
            System.out.println("7. 회원탈퇴");
            System.out.print("번호를 입력해주세요 : ");

            int menu = inputInt();

            if ((menu == 1 || menu == 3 || menu == 4) && (courseList == null || courseList.isEmpty())) {
                outputView.printMessage("\n[안내] 등록된 강좌가 없습니다. 먼저 2번을 눌러 강좌를 등록해주세요.");
                continue;
            }

            switch (menu) {
                case 1: viewTeacherCourseDetail(memberId, courseList); break;
                case 2: createCourse(memberId); break;
                case 3: updateCourse(memberId, courseList); break;
                case 4: deleteCourse(memberId, courseList); break;
                case 5: displayTeacherMyPage(member); break;
                case 6:
                    try {
                        boolean logoutResult = authController.signOut(memberId);
                        memberOutputView.printsignOutResult(logoutResult);
                    } catch (Exception e) {
                        outputView.printError("로그아웃 중 오류 발생");
                    }
                    return false;
                case 7:
                    try {
                        boolean deleteResult = authController.getOut(email);
                        memberOutputView.printDeleteMemberResult(deleteResult);
                    } catch (Exception e) {
                        outputView.printError("회원탈퇴 중 오류 발생");
                    }
                    return true;
                default:
                    outputView.printError("");
            }
        }
    }

    private void displayTeacherMyPage(MemberDTO member) {
        while (true) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("           마이페이지");
            System.out.println("=================================");
            System.out.println("1. 내 정보 보기");
            System.out.println("2. 비밀번호 변경");
            System.out.println("3. 나가기");
            System.out.println("=================================");
            System.out.print("메뉴 선택 : ");

            int menu = inputInt();
            switch (menu) {
                case 1:
                    System.out.println("=================================");
                    System.out.println("아이디 : " + member.getEmail());
                    System.out.println("이름   : " + member.getName());
                    System.out.println("역할   : " + member.getRole());
                    System.out.println("가입일 : " + member.getEnrolledAt());
                    System.out.println("=================================");
                    break;
                case 2: resetTeacherPassword(); break;
                case 3: return;
                default: outputView.printError("");
            }
        }
    }

    private void resetTeacherPassword() {
        System.out.println();
        System.out.println("=================================");
        System.out.println("         비밀번호 변경");
        System.out.println("=================================");
        System.out.print("이메일 : ");
        String email = sc.nextLine().replaceAll("\\s", "");

        while (true) {
            System.out.print("새 비밀번호 (특수기호 포함) : ");
            String newPassword = sc.nextLine().replaceAll("\\s", "");

            String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
            boolean hasSpecial = false;
            for (char c : newPassword.toCharArray()) {
                if (specialChars.indexOf(c) >= 0) { hasSpecial = true; break; }
            }
            if (!hasSpecial) { outputView.printMessage("특수기호는 필수입니다. 다시 입력해주세요."); continue; }

            try {
                boolean result = authController.resetPassword(email, newPassword);
                if (result) outputView.printMessage("비밀번호가 변경되었습니다.");
                else outputView.printMessage("비밀번호 변경에 실패하였습니다.");
            } catch (Exception e) {
                outputView.printError("비밀번호 변경 중 오류 발생");
            }
            break;
        }
    }

    private void viewTeacherCourseDetail(long memberId, List<CourseDTO> courseList) throws SQLException {
        outputView.printCourses(courseList);
        System.out.print("\n상세 조회할 강좌 번호 (0: 뒤로가기) : ");
        int num = inputInt();
        if (num == 0) return;
        if (num < 1 || num > courseList.size()) { outputView.printMessage("올바른 번호를 입력해주세요."); return; }

        long courseId = courseList.get(num - 1).getCourseId();
        SectionDTO detail = controller.findJoin(courseId);
        if (detail == null) { outputView.printMessage("강좌 정보를 찾을 수 없습니다."); return; }
        outputView.printCourseDetail(detail);

        System.out.println("=================================");
        System.out.println("1. 수강생 보기");
        System.out.println("2. 강좌평 보기");
        System.out.println("3. 이전으로");
        System.out.println("=================================");
        System.out.print("번호를 입력해주세요 : ");

        int menu = inputInt();
        if (menu == 1) showStudentList(courseId);
        else if (menu == 2) studyInputView.ShowReviewForTeacher(courseId);
    }

    private void createCourse(long memberId) throws SQLException {
        outputView.printMessage("\n--- 강좌 등록 ---");
        System.out.print("강좌 제목 : ");
        String title = sc.nextLine().trim();
        if (title.isEmpty()) { outputView.printFail("강좌 제목을 입력해주세요."); return; }

        long courseId = controller.addCourse(title, memberId);
        if (courseId == -1) { outputView.printFail("강좌 등록에 실패했습니다."); return; }
        outputView.printSuccess("강좌 등록 완료!");

        outputView.printMessage("\n--- 강의 등록 ('0' 입력 시 종료) ---");
        while (true) {
            System.out.print("강의 제목 : ");
            String lTitle = sc.nextLine().trim();
            if (lTitle.equals("0")) { outputView.printMessage("강의 등록을 완료했습니다."); break; }
            if (lTitle.isEmpty()) { outputView.printFail("강의 제목을 입력해주세요. ('0' 입력 시 종료)"); continue; }
            if (controller.addLecture(courseId, lTitle))
                outputView.printSuccess("강의 '" + lTitle + "' 등록 완료!");
            else outputView.printFail("강의 등록 실패.");
        }
    }

    private void updateCourse(long memberId, List<CourseDTO> courseList) throws SQLException {
        outputView.printCourses(courseList);
        System.out.print("\n수정할 강좌 번호 (0: 뒤로가기) : ");
        int num = inputInt();
        if (num == 0) return;
        if (num < 1 || num > courseList.size()) { outputView.printMessage("올바른 번호를 입력해주세요."); return; }

        long courseId = courseList.get(num - 1).getCourseId();
        System.out.print("새 강좌 제목 : ");
        String newTitle = sc.nextLine().trim();
        if (newTitle.isEmpty()) { outputView.printFail("제목을 입력해주세요."); return; }

        if (controller.updateCourse(courseId, newTitle, memberId))
            outputView.printSuccess("강좌 수정 완료!");
        else outputView.printFail("강좌 수정 실패");
    }

    private void deleteCourse(long memberId, List<CourseDTO> courseList) throws SQLException {
        outputView.printCourses(courseList);
        System.out.print("\n삭제할 강좌 번호 (0: 뒤로가기) : ");
        int num = inputInt();
        if (num == 0) return;
        if (num < 1 || num > courseList.size()) { outputView.printMessage("올바른 번호를 입력해주세요."); return; }

        long courseId = courseList.get(num - 1).getCourseId();
        System.out.print("정말 삭제하시겠습니까? (Y/N) : ");
        if (!sc.nextLine().trim().equalsIgnoreCase("Y")) { outputView.printMessage("삭제를 취소했습니다."); return; }

        if (controller.deleteCourse(courseId))
            outputView.printSuccess("강좌 삭제 완료!");
        else outputView.printFail("강좌 삭제 실패.");
    }

    private void showStudentList(long courseId) {
        outputView.printMessage("\n--- 현재 수강생 목록 ---");
        try {
            List<MemberDTO> students = controller.getEnrolledStudents(courseId);
            if (students == null || students.isEmpty()) {
                outputView.printMessage("현재 수강 중인 학생이 없습니다.");
            } else {
                System.out.println("-------------------------------------------");
                System.out.printf("%-15s | %-25s%n", "이름", "이메일");
                System.out.println("-------------------------------------------");
                for (MemberDTO student : students) {
                    System.out.printf("%-15s | %-25s%n", student.getName(), student.getEmail());
                }
                System.out.println("-------------------------------------------");
            }
        } catch (SQLException e) {
            outputView.printError("데이터 조회 중 오류 발생");
        }
    }

    private int inputInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("숫자만 입력해주세요 : "); }
        }
    }
}
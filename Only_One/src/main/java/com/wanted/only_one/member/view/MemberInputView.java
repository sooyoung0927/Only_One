package com.wanted.only_one.member.view;

import com.wanted.only_one.course.view.CourseInputView;
import com.wanted.only_one.member.controller.AuthController;
import com.wanted.only_one.member.dto.MemberDTO;
import com.wanted.only_one.payments.PaymentInputView;
import com.wanted.only_one.study.view.StudyInputView;

import java.sql.SQLException;
import java.util.Scanner;


public class MemberInputView {

    private final AuthController authController;
    private final MemberOutputView outputView;
    private final PaymentInputView payInputView;
    private final CourseInputView courseInputView; // 주입 추가
    private final StudyInputView studyInputView;   // 주입 추가
    private final Scanner sc = new Scanner(System.in);

    private String loggedInEmail;
    private MemberDTO loggedInMember;

    public MemberInputView(AuthController authController,
                           MemberOutputView outputView,
                           PaymentInputView payInputView,
                           CourseInputView courseInputView,
                           StudyInputView studyInputView) {
        this.authController = authController;
        this.outputView = outputView;
        this.payInputView = payInputView;
        this.courseInputView = courseInputView;
        this.studyInputView = studyInputView;
    }

    // 메인 메뉴
    public void displayMainMenu() {
        String[] lines = {
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿",
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿",
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿",
                "⣿⣿⣿⣿⣿⣿⠿⠛⠻⠿⣿⣿⣿⣿⣿⣿⣿⣿⠻⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠻⠻⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿",
                "⣿⣿⣿⣿⡟⠁⣠⣤⣄⠀⢹⡟⠛⠻⠛⠻⣿ ⠀⡞⠛⣻⣿⠛⢛⣿⣿⡟⠁⣠⣤⣤⠀⢘⡿⠛⠻⠛⠛⣿⣿⠿⠻⣿⣿⣿⣿⣿",
                "⣿⣿⣿⡿⠀⢰⣿⣿⡟⠀⢸⠃⠀⣴⡆⠀⣾⠀⢠⣇⠀⣺⠃⢀⣾⣿⣿⠀⢠⣿⣿⡿⠀⢰⠇⠀⣴⡆⠀⢼⠁⠠⠛⠂⠀⣿⣿⣿⣿⣿",
                "⣿⣿⣿⣧⠀⠘⠿⠛⠁⣠⡟⠀⢸⣿⠀⢠⡇⠀⣼⣗⠀⠂⢠⣾⣿⣿⣿⠀⠈⠿⠟⠁⣤⡾⠀⢰⣿⠅⠀⡇⠀⠶⠶⠒⢲⣿⣿⣿⣿",
                "⣿⣿⣿⣿⣷⣤⣤⣴⣾⣿⣧⣤⣿⣷⣤⣼⣤⣤⡿⠟⠀⢠⣿⣿⣿⣿⣿⣷⣤⣤⣤⣿⣿⣧⣤⣿⣿⣤⣼⣿⣦⣤⣤⣶⣿⣿⣿⣿",
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣤⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿",
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿",
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿"
        };

        for (int i = 0; i < lines.length; i ++) {
            System.out.println(lines[i]);
        }

        while (true) {
            System.out.println("╔═══════════════════════════════╗");
            System.out.println("║      ✨  ONLY  ONE  ✨       ║");
            System.out.println("║  only one에 오신 걸 환영합니다   ║");
            System.out.println("╠═══════════════════════════════╣");
            System.out.println("║  1. 회원가입                   ║");
            System.out.println("║  2. 로그인                     ║");
            System.out.println("║  3. 종료                       ║");
            System.out.println("╚═══════════════════════════════╝");
            System.out.print("메뉴 선택 : ");

            int menu = inputInt();

            switch (menu) {
                case 1:
                    displaySignUpMenu();
                    break;
                case 2:
                    displayLoginMenu();
                    break;
                case 3:
                    outputView.printMessage("== 프로그램을 종료합니다. ==");
                    System.exit(0);
                    break;
                default:
                    outputView.printError("올바른 메뉴를 선택해주세요.");
            }
        }
    }

    // 회원가입 메뉴
    private void displaySignUpMenu() {
        while (true) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("             회원가입");
            System.out.println("=================================");
            System.out.println("1. 학생 회원가입");
            System.out.println("2. 강사 회원가입");
            System.out.println("3. 이전으로");
            System.out.println("=================================");
            System.out.print("메뉴 선택 : ");

            int menu = inputInt();

            switch (menu) {
                case 1:
                    signUp("STUDENT");
                    break;
                case 2:
                    signUp("TEACHER");
                    break;
                case 3:
                    return;
                default:
                    outputView.printError("올바른 메뉴를 선택해주세요.");
            }
        }
    }

    // 회원가입 처리 - 동의
    private boolean signUp(String role) {
        while (true) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("         개인정보 수집 동의");
            System.out.println("=================================");
            System.out.println("개인정보 수집 및 이용에 동의하십니까?");
            System.out.println("1. 예");
            System.out.println("2. 아니오");
            System.out.print("선택 : ");

            int agree = inputInt();
            switch (agree) {
                case 1:
                    if(enterInfo(role)){
                        return true;
                    }
                    return false;
                case 2:
                    outputView.printMessage("개인정보 수집에 동의하지 않으면 가입할 수 없습니다.");
                    return false;
                default:
                    System.out.println(" ");
                    outputView.printError("========올바른 메뉴를 선택해주세요.");
            }
        }
    }

    // 회원가입 정보 입력--------trim 추가했습니다
    private boolean enterInfo(String role) {
        boolean result = true;
        while (true) {
            System.out.print("이름을 입력하십시오 : ");
            String name = sc.nextLine().trim();

            System.out.print("이메일을 입력하십시오 : ");
            String email = sc.nextLine().replaceAll("\\s","");
            if (!email.endsWith("@lms.com")) {
                outputView.printError("이메일은 @lms.com 형식이어야 합니다.");
                continue;
            }
            boolean emailresult = authController.emailMix(email);
            if (emailresult) {
                if (authController.isBlacklistedEmail(email)) {
                    outputView.printError("\n블랙리스트에 등록된 이메일입니다. 가입이 불가능합니다.");
                } else {
                    outputView.printError("\n이미 존재하는 이메일입니다.");
                }
                continue;
            }
            System.out.print("비밀번호를 입력하십시오 (특수기호 포함) : ");
            String password = sc.nextLine().replaceAll("\\s","");
            boolean pwdResult = authController.pwdInclude(password);
            if (!pwdResult) {
                outputView.printError("\n비밀번호는 특수기호, 대문자, 소문자를 모두 포함해야 합니다.");
                System.out.println(" ");
                continue;
            }
            result = authController.signUp(name, email, password, role);
            outputView.printSignUpResult(result);
            break;
        }
        if (result) {
            displayLoginMenu();
        }
        return false;
    }

    // 로그인 메뉴
    public boolean displayLoginMenu() {
        while (true) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("              로그인");
            System.out.println("=================================");
            System.out.println("1. 학생 로그인");
            System.out.println("2. 강사 로그인");
            System.out.println("3. 이전으로");
            System.out.println("=================================");
            System.out.print("메뉴 선택 : ");

            int menu = inputInt();

            switch (menu) {
                case 1:
                    if(studentLogin()) {
                        return true;
                    }
                    break;
                case 2:
                    if(teacherLogin()) {
                        return true;
                    }
                    break;
                case 3:
                    return true;
                default:
                    outputView.printError("올바른 메뉴를 선택해주세요.");
            }
        }
    }

    // 학생 로그인 처리
    private boolean studentLogin() {
        System.out.println("이메일을 입력하십시오");
        System.out.print("이메일 : ");
        String email = sc.nextLine().replaceAll("\\s","");

        System.out.println("비밀번호를 입력하십시오");
        System.out.print("비밀번호 : ");
        String password = sc.nextLine().replaceAll("\\s","");

        MemberDTO result = authController.signIn(email, password);
        if (result != null && !result.getRole().equals("STUDENT")) {
            System.out.println("학생 계정이 아닙니다.");
            authController.signOut(result.getMemberId());
            outputView.printSignInResult(false);
            return false;
        }
        outputView.printSignInResult(result != null);

        if (result != null) {
            this.loggedInEmail = email;
            this.loggedInMember = result;
            studyInputView.setMember(result);
            return selectMenu();
        }
        return false;
    }

    // 강사 로그인 처리
    private boolean teacherLogin() {
        System.out.println("이메일을 입력하십시오");
        System.out.print("이메일 : ");
        String email = sc.nextLine().replaceAll("\\s","");
        boolean emailRs = authController.emailMix(email); // 네 원본에 있던 로직

        System.out.println("비밀번호를 입력하십시오");
        System.out.print("비밀번호 : ");
        String password = sc.nextLine().replaceAll("\\s","");

        MemberDTO result = authController.signIn(email, password);
        if (result != null && !result.getRole().equals("TEACHER")) {
            System.out.println("강사 계정이 아닙니다.");
            authController.signOut(result.getMemberId());
            outputView.printSignInResult(false);
            return false;
        }
        outputView.printSignInResult(result != null);

        if (result != null) {
            this.loggedInEmail = email;
            this.loggedInMember = result;
            studyInputView.setMember(result);
            try {
                courseInputView.teacherCourseMenu(result.getMemberId(),email,result);
                return false;
            } catch (SQLException e) {
                outputView.printError("강사 메뉴 로드 중 오류 발생");
            }
        }
        return false;
    }

    // 비밀번호 초기화
    private void resetPassword() {
        System.out.println();
        System.out.println("=================================");
        System.out.println("         비밀번호 초기화");
        System.out.println("=================================");
        System.out.println("본인 확인을 위해 이메일을 입력해주세요");
        System.out.print("이메일 : ");
        String email = sc.nextLine().replaceAll("\\s", "");

        while (true) {
            System.out.print("새 비밀번호 : ");
            String newPassword = sc.nextLine().replaceAll("\\s", "");

            if (!authController.pwdInclude(newPassword)) {
                outputView.printError("특수기호는 필수입니다. 다시 입력해주세요.");
                continue;
            }


            boolean result = authController.resetPassword(email, newPassword);
            outputView.printResetPasswordResult(result);
            break;
        }
    }

    // 숫자 입력 예외처리
    private int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("숫자만 입력해주세요 : ");
            }
        }
    }

    public boolean selectMenu() {
        while (true) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("             메인메뉴");
            System.out.println("=================================");
            System.out.println("0. 결제");
            System.out.println("1. 강좌 전체 조회");
            System.out.println("2. 강좌 선택 목록");
            System.out.println("3. 강좌 수강");
            System.out.println("4. 강좌평 작성");
            System.out.println("5. 강좌 검색");
            System.out.println("6. 마이페이지");
            System.out.println("7. 로그아웃");
            System.out.println("8. 회원탈퇴");
            System.out.println("=================================");
            System.out.print("메뉴 선택 : ");

            int menu = inputInt();

            switch (menu) {
                case 0:
                    payInputView.pay();
                    break;
                case 1:
                    try { courseInputView.showAllCourses(); } catch (SQLException e) {}
                    break;
                case 2:
                    studyInputView.ChooseFav();
                    break;
                case 3:
                    try { courseInputView.courseSelection(loggedInMember.getMemberId()); } catch (SQLException e) {}
                    break;
                case 4:
                    studyInputView.Review();
                    break;
                case 5:
                    try {
                        courseInputView.searchCourse();
                    } catch (SQLException e) {
                        System.out.println("[오류] 강좌 검색 중 문제가 발생했습니다: " + e.getMessage());
                    }
                    break;
                case 6:
                    displayStudentMyPage(loggedInMember);
                    break;
                case 7:
                    boolean out = logout(loggedInEmail);
                    loggedInEmail = null;
                    loggedInMember = null;
                    if(out){
                        return false;
                    } else {
                        System.out.println("============나 죽어...============");
                    } System.exit(0);
                    break;
                case 8:
                    boolean kill = getOut(loggedInEmail);
                    loggedInEmail = null;
                    loggedInMember = null;
                    if(kill){
                        return true;
                    } else {
                        System.out.println("============나 죽어...============");
                    } System.exit(0);
                    break;
                default:
                    outputView.printError("올바른 메뉴를 선택해주세요.");
            }
        }
    }

    private void displayStudentMyPage(MemberDTO loggedInMember) {
        while (true) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("            마이페이지");
            System.out.println("=================================");
            System.out.println("1. 내 정보 보기");
            System.out.println("2. 비밀번호 변경");
            System.out.println("3. 즐겨찾기 강좌 보기");
            System.out.println("4. 수강 강좌 보기");
            System.out.println("5. 결제 내역 조회");
            System.out.println("6. 나가기");
            System.out.println("=================================");
            System.out.print("메뉴 선택 : ");

            int menu = inputInt();

            switch (menu) {
                case 1:
                    outputView.printMemberInfo(loggedInMember);
                    break;
                case 2:
                    resetPassword();
                    break;
                case 3:
                    studyInputView.showFavList();
                    break;
                case 4:
                    studyInputView.MyStudying();
                    break;
                case 5:
                    payInputView.showPayment();
                    break;
                case 6:
                    return;
                default:
                    outputView.printError("올바른 메뉴를 선택해주세요.");
            }
        }
    }


    public boolean getOut(String email) {
        try {
            boolean kill = authController.getOut(email);
            if(kill) {
                System.out.println("=================================");
                System.out.println("    결국 포기한거야? 나약한 녀석...   ");
                System.out.println("=================================");
                return kill;
            } else {
                return kill;
            }
        } catch (SQLException e) {
            outputView.printError("들어올 땐 마음대로였지만, 나갈 땐 아니란다.");
            return false;
        }
    }

    public boolean logout(String email) {
        try {
            boolean out = authController.logout(email);
            if(out) {
                System.out.println("=================================");
                System.out.println("        로그아웃 되었습니다!!       ");
                System.out.println("=================================");
                return out;
            } else {
                return out;
            }
        } catch (SQLException e) {
            outputView.printError("들어올 땐 마음대로 였지만, 나갈 땐 아니란다");
            return false;
        }
    }
}
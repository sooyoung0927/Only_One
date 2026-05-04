package com.wanted.only_one.member.view;

import com.wanted.only_one.member.dto.MemberDTO;

public class MemberOutputView {
    // 회원가입 결과 출력
    public void printSignUpResult(boolean result) {
        System.out.println("=================================");
        if (result) {
            System.out.println(" 회원가입이 완료되었습니다.");
            System.out.println("로그인 화면으로 이동합니다.");
        } else {
            System.out.println(" 회원가입에 실패하였습니다.");
        }
        System.out.println("=================================");
    }
    // 로그인 결과 출력
    public void printSignInResult(boolean result) {
        if (result) {
            System.out.println(" 로그인 성공하였습니다.");
        } else {
            System.out.println(" 로그인에 실패하였습니다.");
        }
        System.out.println("=================================");
    }
    // 로그아웃 결과 출력
    public void printsignOutResult(boolean result) {
        if (result) {
            System.out.println(" 로그아웃 성공하였습니다.");
            System.out.println(" 로그인 화면으로 이동하겠습다");
        } else {
            System.out.println("로그아웃 실패하였습니다.");
        }
        System.out.println("=================================");
    }
    // 회원 정보 출력
    public void printMemberInfo(MemberDTO member) {
        System.out.println(" 내 정보 보기");
        System.out.println("=================================");
        if (member == null) {
            System.out.println(" 회원 정보를 찾을 수 없습니다.");
        } else {
            System.out.println("아이디 : " + member.getEmail());
            System.out.println("이름 : " + member.getName());
            System.out.println("역할 : " + member.getRole());
            System.out.println("가입일 : " + member.getEnrolledAt());
        }
        System.out.println("=================================");
    }

    // 비밀번호 수정 결과
    public void printUpdatePasswordResult(boolean result) {
        if (result) {
            System.out.println(" 비밀번호가 수정되었습니다.");
        } else {
            System.out.println(" 비밀번호 수정에 실패하였습니다.");
        }
        System.out.println("=================================");
    }
    // 회원 탈퇴 결과
    public void printDeleteMemberResult(boolean result) {
        if (result) {
            System.out.println(" 회원탈퇴가 완료되었습니다.");
            System.out.println("이용해주셔서 감사합니다.");
        } else {
            System.out.println(" 회원탈퇴에 실패하였습니다.");
        }
        System.out.println("=================================");
    }

    // 블랙리스트 결과
    public void printBlacklistResult() {
        System.out.println(" 중복 로그인이 감지되었습니다.");
        System.out.println("로그인 화면으로 이동합니다.");
        System.out.println("=================================");
    }

    // 비밀번호 초기화 결과
    public void printResetPasswordResult(boolean result) {
        if (result) {
            System.out.println(" 비밀번호가 초기화되었습니다.");
        } else {
            System.out.println(" 비밀번호 초기화에 실패하였습니다.");
        }
        System.out.println("=================================");
    }

    // 정보수집 동의 거부
    public void printAgreeReject() {
        System.out.println("정보수집 동의를 거부하셨습니다.");
        System.out.println("메인 화면으로 이동합니다.");
        System.out.println("=================================");
    }

    // 에러 메시지 출력
    public void printError(String message) {
        System.out.println( message);

    }

    public void printMessage(String s) {
        System.out.println( s);
    }
}





package com.wanted.only_one.member.controller;

import com.wanted.only_one.member.dto.MemberDTO;
import com.wanted.only_one.member.service.MemberService;

public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    // 회원 정보 조회
    public MemberDTO findMember(long memberId) {
        return service.findMember(memberId);
    }

    // 비밀번호 수정
    public boolean updatePassword(long memberId, String newPassword) {
        return service.updatePassword(memberId, newPassword);
    }

    // 회원 탈퇴
    public boolean deleteMember(long memberId) {
        return service.deleteMember(memberId);
    }
}

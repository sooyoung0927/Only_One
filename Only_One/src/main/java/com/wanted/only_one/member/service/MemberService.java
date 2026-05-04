package com.wanted.only_one.member.service;

import com.wanted.only_one.global.config.JDBCTemplate;
import com.wanted.only_one.member.dao.MemberDAO;
import com.wanted.only_one.member.dto.MemberDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class MemberService {

    private MemberDAO memberDAO;
    private Connection connection;

    public MemberService(Connection connection) {
        this.memberDAO = new MemberDAO(connection);
        this.connection = connection;
    }

    // 회원 정보 조회
    public MemberDTO findMember(long memberId) {
        Connection conn = null;
        try {
            conn = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(conn);

            return memberDAO.findById(memberId);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    //  비번 수정
    public boolean updatePassword(long memberId, String newPassword) {
        Connection conn = null;

        try {
            conn = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(conn);

            // 회원 존재 확인
            MemberDTO member = memberDAO.findById(memberId);
            if (member == null) {
                System.out.println("존재하지 않는 회원입니다.");
                return false;
            }

            return memberDAO.updatePassword(memberId, newPassword);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 회원탈퇴
    public boolean deleteMember(long memberId) {
        Connection conn = null;

        try {
            conn = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(conn);

            // 회원 존재 확인
            MemberDTO member = memberDAO.findById(memberId);
            if (member == null) {
                System.out.println("존재하지 않는 회원입니다.");
                return false;
            }

            return memberDAO.deleteMember(memberId);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
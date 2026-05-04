package com.wanted.only_one.member.dao;

import com.wanted.only_one.global.utils.QueryUtil;
import com.wanted.only_one.member.dto.MemberDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {

    private final Connection con;

    public MemberDAO(Connection con) {
        this.con = con;
    }

    public boolean emailMix(String email) {
        String sql = "SELECT COUNT(*) FROM members WHERE email = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 회원 조회
    public MemberDTO findById(long memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    // 이메일, 비밀번호로 회원 조회
    public MemberDTO findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM members WHERE BINARY email = ? AND BINARY password = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    // 비밀번호 수정
    public boolean updatePassword(long memberId, String newPassword) {
        String sql = "UPDATE members SET password = ? WHERE member_id = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setLong(2, memberId);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    // 회원 삭제
    public boolean deleteMember(long memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, memberId);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    // 이메일 중복 확인
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM members WHERE email = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    // 회원가입
    public boolean insertMember(String name, String email, String password, String role) {
        String sql = "INSERT INTO members (name, email, password, role) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    // 중복 로그인 카운트 + 1
    public void updateAccCount(long memberId) {
        String sql = "UPDATE members SET acc_count = acc_count + 1 WHERE member_id = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, memberId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 이메일로 비밀번호 초기화
    public boolean resetPasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE members SET password = ? WHERE email = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    private MemberDTO mapRow(ResultSet rs) throws SQLException {
        return new MemberDTO(
                rs.getLong("member_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getTimestamp("enrolled_at").toLocalDateTime(),
                rs.getInt("acc_count")
        );
    }

    // 로그인중 이메일 패스워드 중복 확인
    public boolean pwdCheck(String email, String password) {
        String sql = "SELECT COUNT(*) FROM members WHERE email = ?";
        String sql2 = QueryUtil.getQuery("member.pwdCheck");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            pstmt = con.prepareStatement(sql2);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            rs2 = pstmt.executeQuery();

            if (rs.next() && rs2.next()) {
                return rs.getInt(1) > 0 && rs2.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs2 != null) rs2.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean getOut(String email) {
        String sql = QueryUtil.getQuery("member.logout");
        String sql2 = QueryUtil.getQuery("member.kill");
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;


        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            int rs = pstmt.executeUpdate();

            pstmt2 = con.prepareStatement(sql2);
            pstmt2.setString(1, email);
            int rs2 = pstmt2.executeUpdate();

            if(rs>0 && rs2>0) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("==============================");
            System.out.println("       넌 이미 내꺼야...");
            System.out.println("==============================");
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt2 != null) pstmt2.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }
}
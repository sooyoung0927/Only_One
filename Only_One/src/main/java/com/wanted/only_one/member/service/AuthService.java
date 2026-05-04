package com.wanted.only_one.member.service;

import com.wanted.only_one.global.config.JDBCTemplate;
import com.wanted.only_one.member.dao.BlacklistDAO;
import com.wanted.only_one.member.dao.ConnectionDAO;
import com.wanted.only_one.member.dao.MemberDAO;
import com.wanted.only_one.member.dto.MemberDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthService {

    public AuthService(Connection connection) {

    }

    // 1. 회원가입
    public boolean signUp(String name, String email, String password, String role) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(con);

            if (memberDAO.existsByEmail(email)) {
                System.out.println("이미 사용 중인 이메일입니다.");
                return false;
            }

            return memberDAO.insertMember(name, email, password, role);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 2. 로그인
    public MemberDTO signIn(String email, String password) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(con);
            BlacklistDAO blacklistDAO = new BlacklistDAO(con);
            ConnectionDAO connectionDAO = new ConnectionDAO(con);

            MemberDTO member = memberDAO.findByEmailAndPassword(email, password);
            if (member == null) {
                System.out.println("이메일 또는 비밀번호가 틀렸습니다.");
                return null;
            }

            if (blacklistDAO.existsBlacklist(member.getMemberId())) {
                System.out.println("블랙리스트 회원입니다. 로그인이 불가합니다.");
                return null;
            }

            if (connectionDAO.existsConnection(member.getMemberId())) {
                memberDAO.updateAccCount(member.getMemberId());

                if (member.getAccCount() + 1 >= 3) {
                    blacklistDAO.insertBlacklist(member.getMemberId());
                    System.out.println("중복 로그인 3회 이상으로 블랙리스트 처리되었습니다.");
                } else {
                    System.out.println("이미 접속 중인 계정입니다.");
                }
                return null;
            }

            boolean connected = connectionDAO.insertConnection(member.getMemberId());
            return connected ? member : null;

        } catch (SQLException e) {
            System.out.println("로딩 중 오류 발생");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 로그아웃
    public boolean signOut(long memberID) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            ConnectionDAO connectionDAO = new ConnectionDAO(con);

            return connectionDAO.deleteConnection(memberID);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 블랙리스트 추가
    public boolean addBlacklist(long memberId) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            BlacklistDAO blacklistDAO = new BlacklistDAO(con);

            return blacklistDAO.insertBlacklist(memberId);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 비밀번호 초기화
    public boolean resetPassword(String email, String newPassword) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(con);

            if (!memberDAO.existsByEmail(email)) {
                System.out.println("존재하지 않는 이메일입니다.");
                return false;
            }

            return memberDAO.resetPasswordByEmail(email, newPassword);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean emailMix(String email) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(con);

            return memberDAO.emailMix(email);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean pwdCheck(String email, String password) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(con);

            return memberDAO.pwdCheck(email, password);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean logout(String email) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            ConnectionDAO connectionDAO = new ConnectionDAO(con);

            return connectionDAO.logout(email);

        } catch (SQLException e) {
            System.out.println("🤬🤬🤬🤬어딜 나가?");
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getOut(String email) {
        Connection con = null;

        try {
            con = JDBCTemplate.getConnection();
            MemberDAO memberDAO = new MemberDAO(con);

            return memberDAO.getOut(email);

        } catch (SQLException e) {
            System.out.println("🤬🤬🤬🤬어딜 나가?");
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isBlacklistedEmail(String email) {
        Connection con = null;
        try {
            con = JDBCTemplate.getConnection();
            BlacklistDAO blacklistDAO = new BlacklistDAO(con);
            return blacklistDAO.existsBlacklistByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
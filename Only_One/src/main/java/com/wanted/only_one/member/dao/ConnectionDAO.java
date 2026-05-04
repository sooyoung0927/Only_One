package com.wanted.only_one.member.dao;

import com.wanted.only_one.global.utils.QueryUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


// 접속 확인하는거
public class ConnectionDAO {
    private final Connection con;

    public ConnectionDAO(Connection con) {
        this.con = con;
    }

    public boolean insertConnection(long memberId) {
        String sql = "INSERT INTO connection_status (member_id) VALUES (?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, memberId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteConnection(long memberId) {
        String sql = "DELETE FROM connection_status WHERE member_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, memberId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsConnection(long memberId) {
        String sql = "SELECT COUNT(*) FROM connection_status WHERE member_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, memberId);

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

    public boolean logout(String email) {
        String sql = QueryUtil.getQuery("member.logout");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("들어올 땐 마음대로였지만 나갈 땐 아니란다.");
            e.printStackTrace();
            return false;
        }
    }
}

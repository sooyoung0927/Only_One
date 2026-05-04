package com.wanted.only_one.payments;

import com.wanted.only_one.global.utils.QueryUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class    PaymentDAO {

    private static Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    public static boolean payingMoney(String email) throws SQLException {
        String query = QueryUtil.getQuery("payment.payMoney");
        String date = QueryUtil.getQuery("payment.checkPay");

        try (PreparedStatement check = connection.prepareStatement(date)) {
            check.setString(1, email);

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    Timestamp lastPayDate = rs.getTimestamp("last_payed_at");

                    if (lastPayDate != null) {
                        LocalDateTime lastPayedAt = lastPayDate.toLocalDateTime();
                        LocalDateTime next = lastPayedAt.plusMonths(1);
                        LocalDateTime now = LocalDateTime.now();

                        if (now.isBefore(next)) {
                            System.out.println("================================================");
                            System.out.println("  아직 이용 기간이 남아 있어 중복 결제가 불가능합니다.");
                            return false;
                        }
                    }
                }
            }
        }
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);

            int result = pstmt.executeUpdate();
            return result > 0;
        }

    }

    public boolean refund(String email) throws SQLException {
        String query = QueryUtil.getQuery("payment.getRefund");
        String date = QueryUtil.getQuery("payment.checkPay");

        try (PreparedStatement check = connection.prepareStatement(date)) {
            check.setString(1, email);

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    Timestamp lastPayDate = rs.getTimestamp("last_payed_at");

                    if (lastPayDate != null) {
                        LocalDateTime lastPayedAt = lastPayDate.toLocalDateTime();
                        LocalDateTime next = lastPayedAt.plusDays(3);
                        LocalDateTime now = LocalDateTime.now();

                        if (now.isAfter(next)) {
                            System.out.println("================================================");
                            System.out.println("      환불 가능 기간이 지나 환불이 불가합니다.");
                            return false;
                        }
                    }
                }
            }
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);

            int result = pstmt.executeUpdate();
            return result > 0;
        }
    }

    public List<PaymentDTO> findPayment(String payEmail) throws SQLException {
        String query = QueryUtil.getQuery("payment.findPayment");
        List<PaymentDTO> payList = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, payEmail);

            ResultSet rset = pstmt.executeQuery();


            while(rset.next()) {

                PaymentDTO payment = new PaymentDTO(
                        rset.getTimestamp("결제일"),
                        rset.getLong("결제금액"),
                        rset.getTimestamp("만료일")
                    );
                    payList.add(payment);

            }
            return payList;
        }


    }

    public boolean checkEmail(String value) throws SQLException {
        String query = QueryUtil.getQuery("payment_checkEmail");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, value);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

}

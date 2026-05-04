package com.wanted.only_one.payments;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PaymentService {

    private PaymentDAO paymentDAO;
    private Connection connection;

    public PaymentService(Connection connection) {
        this.paymentDAO = new PaymentDAO(connection);
        this.connection = connection;
    }

    public static boolean payingMoney(String email) {
        try {
            return PaymentDAO.payingMoney(email);
        } catch (SQLException e) {
            throw new RuntimeException("결제에 실패하셨습니다!!");
        }

    }

    public boolean refund(String email) {
        try {
            return paymentDAO.refund(email);
        } catch (SQLException e) {
            throw new RuntimeException("응 환불 안해줘~~~");
        }
    }

    public List<PaymentDTO> findMyPayment(String payEmail) {

        try {
            return paymentDAO.findPayment(payEmail);
        } catch (SQLException e) {
            throw new RuntimeException("결제 내역 조회 중 Error 발생!!" + e);
        }
    }

    public boolean checkEmail(String value) throws SQLException {
        return paymentDAO.checkEmail(value);
    }
}

package com.wanted.only_one.payments;

import java.util.List;

public class PaymentOutputView  {

    public void printMessage(String s) {
        System.out.println(s);
    }

    public void printPayments(List<PaymentDTO> payList) {
        if (payList == null || payList.isEmpty()) {
            System.out.println("=================================");
            System.out.println("     아직 결제를 하지 않았습니다.");
            System.out.println("=================================");
            return;
        }
        System.out.println("=============결제 내역=============");
        for (PaymentDTO payment : payList) {
            System.out.print("결제일: " + payment.getPayed_at());
            System.out.println(" / 결제금액: " + payment.getPrice());
            System.out.println(" / 만료일: " + payment.getExpireDate());
        }
    }

    public void printError(String s) {
        System.out.println(s);

    }

}

package org.taxreport.binance.fiat.payments.response;

import lombok.Data;

@Data
public class PaymentsDataResponse {
    public enum Status {
        PROCESSING("Processing"), COMPLETED("Completed"), FAILED("Failed"), REFUNDED("Refunded");

        public final String code;

        Status(String code) {
            this.code = code;
        }

        public static boolean isSuccess(String code) {
            return code.equals(COMPLETED.code);
        }
    }

    private String orderNo;
    private double sourceAmount;
    private String fiatCurrency;
    private double obtainAmount;
    private String cryptoCurrency;
    private double totalFee;
    private double price;
    private String status;
    private long createTime;
    private long updateTime;
}

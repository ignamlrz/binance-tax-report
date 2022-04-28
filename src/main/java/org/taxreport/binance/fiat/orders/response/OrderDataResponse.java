package org.taxreport.binance.fiat.orders.response;

import lombok.Data;

@Data
public class OrderDataResponse {
    public enum Status {
        PROCESSING("Processing"), FAILED("Failed"), SUCCESSFUL("Successful"), FINISHED("Finished"),
        REFUNDING("Refunding"), REFUNDED("Refunded"), REFUND_FAILED("Refund Failed"),
        ORDER_PARTIAL_CREDIT_STOPPED("Order Partial credit Stopped");

        public final String code;

        Status(String code) {
            this.code = code;
        }

        public static boolean isSuccess(String code) {
            return code.equals(SUCCESSFUL.code) || code.equals(FINISHED.code);
        }
    }

    private String orderNo;
    private String fiatCurrency;
    private double indicatedAmount;
    private double amount;
    private double totalFee;
    private String method;
    private String status;
    private long createTime;
    private long updateTime;


}

package org.taxreport.binance.fiat.payments.response;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.taxreport.binance.util.data.StaticData;

import java.util.Arrays;
import java.util.Date;

@Data
public class PaymentsResponse extends StaticData {
    private static final Logger log = LogManager.getLogger();

    private String code;
    private String message;
    private PaymentsDataResponse[] data;
    private int total;
    private boolean success;

    public double getTotalSourceAmount() {
        return Arrays.stream(data).map(p -> {
            if(!PaymentsDataResponse.Status.isSuccess(p.getStatus()))  return 0.0;
            return p.getSourceAmount();
        }).reduce(0.0, (a, b) -> a + b);
    }

    public double getTotalSourceAmountApplyingFee() {
        return Arrays.stream(data).map(p -> {
            if(!PaymentsDataResponse.Status.isSuccess(p.getStatus()))
            {
                log.debug(new Date(p.getCreateTime()) + ": "+ p.getStatus() + " - " + p.getSourceAmount());
                return 0.0;
            }
            return p.getSourceAmount() - p.getTotalFee();
        }).reduce(0.0, (a, b) -> a + b);
    }

    public double getTotalObtainAmount() {
        return Arrays.stream(data).map(p -> {
            if(!PaymentsDataResponse.Status.isSuccess(p.getStatus()))  return 0.0;
            return p.getObtainAmount();
        }).reduce(0.0, (a, b) -> a + b);
    }

    public double getTotalObtainAmountWithoutFee() {
        return Arrays.stream(data).map(p -> {
            if(!PaymentsDataResponse.Status.isSuccess(p.getStatus()))  return 0.0;
            return p.getSourceAmount() / p.getPrice();
        }).reduce(0.0, (a, b) -> a + b);
    }

    public double getTotalFee() {
        return Arrays.stream(data).map(p -> {
            if(!PaymentsDataResponse.Status.isSuccess(p.getStatus()))  return 0.0;
            return p.getTotalFee();
        }).reduce(0.0, (a, b) -> a + b);
    }

    @Override
    public boolean isSearchable(long beginTime, long endTime) {
        return false;
    }

    @Override
    public Object search(long beginTime, long endTime) {
        return null;
    }
}

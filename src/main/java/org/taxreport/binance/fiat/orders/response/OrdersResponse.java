package org.taxreport.binance.fiat.orders.response;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.taxreport.binance.util.data.StaticData;

import java.util.Arrays;
import java.util.Date;

@Data()
public class OrdersResponse extends StaticData {
    private static final Logger log = LogManager.getLogger();

    private String code;
    private String message;
    private OrderDataResponse[] data;
    private int total;
    private boolean success;

    public double calcTotalIndicatedAmount() {
        return Arrays.stream(data).map(o -> {
            if(!OrderDataResponse.Status.isSuccess(o.getStatus()))  return 0.0;
            return o.getIndicatedAmount();
        }).reduce(0.0, (a, b) -> a + b);
    }

    public double calcTotalAmount() {
        return Arrays.stream(data).map(o -> {
            if(!OrderDataResponse.Status.isSuccess(o.getStatus())) {
                log.debug(new Date(o.getCreateTime()) + ": "+ o.getStatus() + " - " + o.getAmount());
                return 0.0;
            }
            return o.getAmount();
        }).reduce(0.0, (a, b) -> a + b);
    }

    public double calcTotalFee() {
        return Arrays.stream(data).map(o -> {
            if(!OrderDataResponse.Status.isSuccess(o.getStatus()))  return 0.0;
            return o.getTotalFee();
        }).reduce(0.0, (a, b) -> a + b);
    }


    @Override
    public boolean isSearchable(long beginTime, long endTime) {
        if(beginTime > endTime) return false;
        if(beginTime < this.beginTime || this.endTime < beginTime)  return false;
        if(this.endTime < endTime)  return false;
        return true;
    }

    @Override
    public OrderDataResponse[] search(long beginTime, long endTime) {
        if(!isSearchable(beginTime, endTime)) return null;
        return Arrays.stream(data).filter(data -> {
            if(beginTime <= data.getCreateTime() && data.getCreateTime() <= endTime) {
                return true;
            } else {
                return false;
            }
        }).toArray(OrderDataResponse[]::new);
    }
}

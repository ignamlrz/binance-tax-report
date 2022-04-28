package org.taxreport.binance.fiat.orders;

import com.binance.connector.client.impl.SpotClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.taxreport.binance.fiat.orders.response.OrdersResponse;
import org.taxreport.binance.util.data.StaticData;

import java.util.LinkedHashMap;

import static org.taxreport.binance.config.Credentials.API_KEY;
import static org.taxreport.binance.config.Credentials.SECRET_KEY;

public class Orders {
    private static final Logger log = LogManager.getLogger();

    public enum Type {
        DEPOSIT("0"), WITHDRAW("1");

        private final String code;

        Type(String code) {
            this.code = code;
        }
    }

    public static OrdersResponse betweenDates(long beginTime, long endTime, Type type) throws JsonProcessingException {
        log.trace("beginning method");
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        parameters.put("transactionType", type.code);
        parameters.put("beginTime", beginTime);
        parameters.put("endTime", endTime);

        // Read local file
        var data= StaticData.read(OrdersResponse.class, getPrefix(type.code));
        if(data != null && data.isSearchable(beginTime, endTime)) {
            return data;
        }

        //  Make call setting begin and end time, and then write data
        return getFiatOrders(parameters);
    }

    private static OrdersResponse getFiatOrders(LinkedHashMap<String,Object>parameters) throws JsonProcessingException {
        var client = new SpotClientImpl(API_KEY, SECRET_KEY);
        String response = client.createFiat().orders(parameters);
        var result = new ObjectMapper().readValue(response, OrdersResponse.class);
        result.setBeginTime((long) parameters.get("beginTime"));
        result.setEndTime((long) parameters.get("endTime"));
        log.trace("result: " + result);

        StaticData.write(result, getPrefix((String) parameters.get("transactionType")));
        log.trace("finishing method");
        return result;
    }

    private static String getPrefix(String code) {
        return (code == Type.DEPOSIT.code) ? "FiatDeposit" : "FiatWithdraw";
    }
}

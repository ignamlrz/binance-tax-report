package org.taxreport.binance.fiat.payments;

import com.binance.connector.client.impl.SpotClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.taxreport.binance.fiat.payments.response.PaymentsResponse;

import java.util.LinkedHashMap;

import static org.taxreport.binance.config.Credentials.API_KEY;
import static org.taxreport.binance.config.Credentials.SECRET_KEY;

public class Payments {
    private static final Logger log = LogManager.getLogger();

    public enum Type {
        BUY("0"), SELL("1");

        private final String code;

        Type(String code) {
            this.code = code;
        }
    }

    public static PaymentsResponse betweenDates(long beginTime, long endTime, Payments.Type type) throws JsonProcessingException {
        log.trace("beginning method");
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        parameters.put("transactionType", type.code);
        parameters.put("beginTime", beginTime);
        parameters.put("endTime", endTime);

        var client = new SpotClientImpl(API_KEY, SECRET_KEY);
        String response = client.createFiat().payments(parameters);
        var result = new ObjectMapper().readValue(response, PaymentsResponse.class);
        log.trace("result: " + result);
        log.trace("finishing method");
        return result;
    }
}

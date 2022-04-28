package org.taxreport.binance.fiat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.taxreport.binance.fiat.payments.Payments;
import org.taxreport.binance.fiat.payments.response.PaymentsResponse;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.*;

public class PaymentsTests {
    private static final String json = "{\n" +
            "    \"code\": \"000000\",\n" +
            "    \"message\": \"success\",\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"orderNo\": \"3d1831ed00fe419b8b9c55f164630f4e\",\n" +
            "            \"sourceAmount\": \"346.96\",\n" +
            "            \"fiatCurrency\": \"EUR\",\n" +
            "            \"obtainAmount\": \"400.0\",\n" +
            "            \"cryptoCurrency\": \"USDT\",\n" +
            "            \"totalFee\": \"6.94\",\n" +
            "            \"price\": \"0.85006043\",\n" +
            "            \"status\": \"Processing\",\n" +
            "            \"createTime\": 1627575523000,\n" +
            "            \"updateTime\": 1627575563000\n" +
            "        },\n" +
            "        {\n" +
            "            \"orderNo\": \"3d1831ed00fe419b8b9c55f164630f4f\",\n" +
            "            \"sourceAmount\": \"100\",\n" +
            "            \"fiatCurrency\": \"EUR\",\n" +
            "            \"obtainAmount\": \"99.0\",\n" +
            "            \"cryptoCurrency\": \"USDT\",\n" +
            "            \"totalFee\": \"1.00\",\n" +
            "            \"price\": \"1\",\n" +
            "            \"status\": \"Completed\",\n" +
            "            \"createTime\": 1627575523000,\n" +
            "            \"updateTime\": 1627575563000\n" +
            "        }\n" +
            "    ],\n" +
            "    \"total\": 28,\n" +
            "    \"success\": true\n" +
            "}";

    @Test
    public void mapJsonToModel() throws JsonProcessingException {
        var result = new ObjectMapper().readValue(json, PaymentsResponse.class);
        var data = result.getData()[0];

        assertEquals("3d1831ed00fe419b8b9c55f164630f4e", data.getOrderNo());
        assertEquals("EUR", data.getFiatCurrency());
        assertEquals("USDT", data.getCryptoCurrency());
        assertEquals(346.96, data.getSourceAmount());
        assertEquals(400.0, data.getObtainAmount());
        assertEquals(6.94, data.getTotalFee());
        assertEquals(0.85006043, data.getPrice());
        assertEquals("Processing", data.getStatus());
        assertEquals(1627575523000L, data.getCreateTime());
        assertEquals(1627575563000L, data.getUpdateTime());

        assertEquals("000000", result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals(28, result.getTotal());
        assertTrue(result.isSuccess());
    }

    @Test
    public void getPayments2021() throws JsonProcessingException {
        var beginTime = new GregorianCalendar(2021, Calendar.JANUARY, 1).getTimeInMillis();
        var endTime = new GregorianCalendar(2022, Calendar.JANUARY, 1).getTimeInMillis();
        var shopping = Payments.betweenDates(beginTime, endTime, Payments.Type.BUY);
        var sales = Payments.betweenDates(beginTime, endTime, Payments.Type.SELL);
        assertTrue(shopping.isSuccess());
        assertTrue(sales.isSuccess());
    }

    @Test
    public void checkGetTotals() throws JsonProcessingException {
        var result = new ObjectMapper().readValue(json, PaymentsResponse.class);

        assertEquals(100.0, result.getTotalSourceAmount());
        assertEquals(99.0, result.getTotalSourceAmountApplyingFee());
        assertEquals(99.0, result.getTotalObtainAmount());
        assertEquals(100.0, result.getTotalObtainAmountWithoutFee());
        assertEquals(1.0, result.getTotalFee());
    }
}

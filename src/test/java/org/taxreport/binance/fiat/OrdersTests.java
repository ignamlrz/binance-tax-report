package org.taxreport.binance.fiat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.taxreport.binance.fiat.orders.Orders;
import org.taxreport.binance.fiat.orders.response.OrdersResponse;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.*;

public class OrdersTests {
    String json = "{\n" +
            "   \"beginTime\": 11544654,\n" +
            "   \"endTime\": 14564798,\n" +
            "   \"code\": \"000000\",\n" +
            "   \"message\": \"success\",\n" +
            "   \"data\": [\n" +
            "   {\n" +
            "      \"orderNo\":\"4b1bda928c064fab9e3b4ef1167a504\",\n" +
            "      \"fiatCurrency\": \"EUR\",\n" +
            "      \"indicatedAmount\": \"102.00\",\n" +
            "      \"amount\": \"100.98\",\n" +
            "      \"totalFee\": \"1.02\",\n" +
            "      \"method\": \"Card\",\n" +
            "      \"status\": \"Refunded\",\n" +
            "      \"createTime\": 1648314759000,\n" +
            "      \"updateTime\": 1648315231000 \n" +
            "   },\n" +
            "   {\n" +
            "      \"orderNo\":\"4b1bda928c064fab9e3b4ef1167a504\",\n" +
            "      \"fiatCurrency\": \"EUR\",\n" +
            "      \"indicatedAmount\": \"100.0\",\n" +
            "      \"amount\": \"99.0\",\n" +
            "      \"totalFee\": \"1.0\",\n" +
            "      \"method\": \"Card\",\n" +
            "      \"status\": \"Successful\",\n" +
            "      \"createTime\": 1648314759000,\n" +
            "      \"updateTime\": 1648315231000 \n" +
            "   }\n" +
            "   ],\n" +
            "   \"total\": 1,\n" +
            "   \"success\": true\n" +
            "}\n";

    @Test
    public void mapJsonToModel() throws JsonProcessingException {
        var result = new ObjectMapper().readValue(json, OrdersResponse.class);

        assertEquals("000000", result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals(1, result.getTotal());
        assertTrue(result.isSuccess());
        assertEquals(11544654L, result.getBeginTime());
        assertEquals(14564798L, result.getEndTime());

        var data = result.getData()[0];

        assertEquals("4b1bda928c064fab9e3b4ef1167a504", data.getOrderNo());
        assertEquals("EUR", data.getFiatCurrency());
        assertEquals(102.00, data.getIndicatedAmount());
        assertEquals(100.98, data.getAmount());
        assertEquals(1.02, data.getTotalFee());
        assertEquals("Card", data.getMethod());
        assertEquals("Refunded", data.getStatus());
        assertEquals(1648314759000L, data.getCreateTime());
        assertEquals(1648315231000L, data.getUpdateTime());
    }

    @Test
    public void getDepositJanuary2021() throws JsonProcessingException {
        var beginTime = new GregorianCalendar(2019, Calendar.JANUARY, 1).getTimeInMillis();
        var endTime = new GregorianCalendar(2022, Calendar.JANUARY, 1).getTimeInMillis();
        var deposits = Orders.betweenDates(beginTime, endTime, Orders.Type.DEPOSIT);
        var withdraws = Orders.betweenDates(beginTime, endTime, Orders.Type.WITHDRAW);
        assertEquals(beginTime, deposits.getBeginTime());
        assertEquals(endTime, deposits.getEndTime());
        assertEquals(beginTime, withdraws.getBeginTime());
        assertEquals(endTime, withdraws.getEndTime());
        assertTrue(deposits.isSuccess());
        assertTrue(withdraws.isSuccess());
    }

    @Test
    public void checkGetTotals() throws JsonProcessingException {
        var result = new ObjectMapper().readValue(json, OrdersResponse.class);

        assertEquals(100.0, result.calcTotalIndicatedAmount());
        assertEquals(99.0, result.calcTotalAmount());
        assertEquals(1.0, result.calcTotalFee());
    }

}

package org.taxreport.binance.util.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IOManagerTests {
    @Test
    public void storeAndRead() {
        String json = "{\n" +
                "    \"code\": \"000000\",\n" +
                "    \"message\": \"success\",\n" +
                "    \"total\": 28,\n" +
                "    \"success\": true\n" +
                "}";
        IOManager.write("test", json);
        String result = IOManager.read("test");
        assertEquals(json, result);
    }
}

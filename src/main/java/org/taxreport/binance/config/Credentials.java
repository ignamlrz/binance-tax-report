package org.taxreport.binance.config;

public class Credentials {
    public static final String API_KEY = System.getenv("BINANCE_API_KEY");
    public static final String SECRET_KEY = System.getenv("BINANCE_SECRET_KEY");
    public static final String TESTNET_BASE_URL = "https://testnet.binance.vision";
    public static final String TESTNET_API_KEY = System.getenv("BINANCE_API_KEY_TESTNET");
    public static final String TESTNET_SECRET_KEY = System.getenv("BINANCE_API_KEY_TESTNET");
}

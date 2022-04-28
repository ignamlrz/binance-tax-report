package org.taxreport.binance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.taxreport.binance.commons.Report;

import static org.taxreport.binance.config.MainConfig.FINANCIAL_YEAR;

public class Main {
    private static final Logger log = LogManager.getLogger();

    public static void main(String ...args) {
        log.info("Init tax report from binance for year " + FINANCIAL_YEAR);
        Report.main(args);
        log.info("Finish tax report from binance");
    }
}

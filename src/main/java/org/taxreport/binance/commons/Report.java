package org.taxreport.binance.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.taxreport.binance.fiat.orders.Orders;
import org.taxreport.binance.fiat.payments.Payments;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.taxreport.binance.config.MainConfig.FINANCIAL_YEAR;

public class Report {
    private static final Logger log = LogManager.getLogger();

    public static void main(String ...args) {
        log.info("Downloading deposits and withdraws");
        try {
            getTotalMoneyDeposited();
            // TODO Create util that check static data obtained (deposits, withdraw, etc) from binance is stored
            //  on files and the date is contained, and if not do a call to fetch that data, and then store. Result:
            //      - Should call one time getDeposits, getWithdraws, etc. This is static data that not change over time.
            //        Next call not should be fetch info from binance, should load from file
            //      - This data could extend over a class NoChangeData, which contain start and end time, and also contain
            //        an abstract method for read/save static data
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static double getTotalMoneyDeposited() throws JsonProcessingException {
        // Select interval time
        var beginTime = new GregorianCalendar(FINANCIAL_YEAR - 3, Calendar.MAY, 23).getTimeInMillis();
        var endTime = new GregorianCalendar(FINANCIAL_YEAR + 1, Calendar.JANUARY, 1).getTimeInMillis();

        //  Fetch fiat orders
        var deposits = Orders.betweenDates(beginTime, endTime, Orders.Type.DEPOSIT);
        var withdraws = Orders.betweenDates(beginTime, endTime, Orders.Type.WITHDRAW);
        var totalOrders = deposits.calcTotalAmount() - withdraws.calcTotalAmount();
        log.info("Total Invested doing Orders: " + totalOrders);

        //  Fetch fiat payments
        var shopping = Payments.betweenDates(beginTime, endTime, Payments.Type.BUY);
        var sales = Payments.betweenDates(beginTime, endTime, Payments.Type.SELL);
        var totalPayments = shopping.getTotalSourceAmountApplyingFee() - sales.getTotalSourceAmountApplyingFee();
        log.info("Total Invested doing Payments: " + totalPayments);

        //  Add all
        var total = totalOrders + totalPayments;
        log.info("Total Invested: " + total);

        return total;
    }
}
